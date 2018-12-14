package com.push.handle;


import com.push.cache.DeviceRedirectPositionCache;
import com.push.cache.PositionCache;
import com.push.model.Device;
import com.push.model.Position;
import com.push.util.BytesConverter;
import com.push.util.Helper;
import com.push.util.Tools;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.List;

/**
 * 主推送类
 */
@Component
public class PushHandler extends TcrIoHandler {
	private static Hashtable<String, PushHandler> hashTable = new Hashtable<String, PushHandler>();
	private Logger log = LoggerFactory.getLogger(getClass());
	private static IoSession session;
	@Resource
	private DeviceRedirectPositionCache cache;
	@Resource
	private PositionCache positionCache;
	@Resource
	private DeviceCache deviceCache;

    /**
     * 建立连接
     */
	public void connect() {
		final NioSocketConnector connector = new NioSocketConnector();
		connector.setHandler(this);
		DemuxingProtocolCodecFactory pcf = new DemuxingProtocolCodecFactory();
		// 自定义编码器
		pcf.addMessageEncoder(String.class, new MessEncoder());
		// 自定义解码器
		pcf.addMessageDecoder(new MessDecoder());
		ProtocolCodecFilter codec = new ProtocolCodecFilter(pcf);
		connector.getFilterChain().addLast("codec", codec);// 指定编码过滤器
		ConnectFuture connFuture = connector.connect(new InetSocketAddress(Helper.get("transport.tcp.ip"), Tools.toInteter(Helper.get("transport.tcp.port"))));
		connFuture.awaitUninterruptibly();
		connector.addListener(new IoListener() {
			//断线重连
			@Override
			public void sessionDestroyed(IoSession arg0) throws Exception {
				for (;;) {
					try {
						Thread.sleep(5000);
						ConnectFuture future = connector.connect(new InetSocketAddress(Helper.get("transport.tcp.ip"), Tools.toInteter(Helper.get("transport.tcp.port"))));
						future.awaitUninterruptibly();// 等待连接创建成功
						IoSession session = future.getSession();// 获取会话
						if (session.isConnected()) {
							TransMessage tm = new TransMessage();
							// 登录
							session.setAttribute("ip", Helper.get("transport.tcp.local.ip"));
							PushHandler.session = session;
							log.info("断线重连[" + connector.getDefaultRemoteAddress().getHostName() + ":" + connector.getDefaultRemoteAddress().getPort() + "]成功");
							break;
						}
					} catch (Exception ex) {
					}
				}
			}
		});
		
		if (!connFuture.isConnected()) {
			log.info("---------not connected exception -----------", this.getClass().getName());
		}
		Boolean b = connFuture.isConnected();
		log.info("connect sucess:{}", b);
		IoSession session = connFuture.getSession();
		this.session = session;
		log.info("connect {} for tcp", this.getClass().getName());
		HandlerService.set("CarRental", this);
	}

	@Override
	public void writePosition(Position pos) {
		log.info("writePosition===========>");
	}
	
	/**
	 * 10分钟发一次
	 */
	@Scheduled(cron = "0 */10 * * *  ? ")
	public void sentPosition(){
		log.info("sentPosition===========>");
		List<Device> listDevice = deviceCache.getCars();
		TransMessage tm = new TransMessage();
		int count =0;
		for (int i = 0; i < listDevice.size(); i++) {
			if(listDevice.get(i) != null){
				Position pos = positionCache.getPosition(listDevice.get(i).getSn());
				if(pos==null)
				    continue;
				//组装定位信息
				try {
				    // 定位
					Device device = deviceCache.getDevice(pos.getDeviceSn());
				    /**定位方式
				     * A：实时
				     * 其他 正常 
				     **/
					//过滤非京牌
					if(device != null && device.getVehicleNo().contains("京")){
						
					    if("A".equalsIgnoreCase(pos.getMode())){
					    	count++;
					    	//实时
					    	String str2 = tm.transPositionMes(device,pos);
					    	if(str2 != null){
					    		log.info("实时===========>{}",device.getVehicleNo()+device.getSn());
					    		session.write(BytesConverter.converString(str2));
					    	}
					    	
					    }else{
					    	count++;
					    	//补发
					    	log.info("补发===========>{}",device.getVehicleNo()+device.getSn());
					    	String str3 = tm.transPositionReisMes(device,pos);
					    	if(str3 != null){
					    		session.write(BytesConverter.converString(str3));
					    	}
					    }
						
					}
					
				} catch (Exception e1) {
					log.debug("组装定位信息失败");
					e1.printStackTrace();
				}
		 }	
		}
		log.info("上传位置个数{}",String.valueOf(count));
	}

	@Override
	public void inputClosed(IoSession var1) {


	}

}

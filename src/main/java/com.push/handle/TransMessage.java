package com.push.handle;

import com.push.model.Device;
import com.push.model.Position;
import com.push.util.BytesConverter;
import com.push.util.Helper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * @since 数据组装类 --登录 注册 定位 补发
 * int、String等 ---> 16进制----10进制byte数组 ---发送
 */
public class TransMessage {
	private Logger log = LoggerFactory.getLogger(getClass());
	// 头标识
	private static String startFlag = "5B";
	// 数据头--数据长度
	private static String MSG_LENGTH = "";
	private static String headerData = "";
	// 登录体
	private static String loginBody = "";
	private static String CRC_CODE = "D383";
	private static String EndFlag = "5D";

	// 转换登录数据
	public String transLoginMes() {
		String loginMes = "";
		MSG_LENGTH = "00000048";// 登录长度
		try {
			// 头数据信息 param flag 子业务类型标识
			// String MSG_ID ="1001"; //4097 登录
			// String MSG_ID ="1200"; //4608 交换数据
			String headerDatas = MSG_LENGTH + transHeaderDataMsg("1001");
			// 登录信息
			String transLoginMsg = transLoginMsg();
			// 头数据长度字段
			// System.out.println("登录信息==》" + startFlag + headerDatas +
			// transLoginMsg + CRC_CODE + EndFlag);
			loginMes = startFlag + headerDatas + transLoginMsg + CRC_CODE + EndFlag;
		} catch (Exception e) {
			log.info("---------转换登录数据失败 -----------", this.getClass().getName());
			e.printStackTrace();
		}
		return loginMes;
	}

	// 转换车辆注册数据
	public String carRegisterMes(Device device) {
		String carRegisterStr = "";
		MSG_LENGTH = "0000006B";// 长度
		try {
			String headerDatas = transHeaderDataMsg("1200");
			headerDatas = MSG_LENGTH + headerDatas;
			// 1201子业务类型标识(实时)
			String carRegister = tranCarRegister(device,"1201");
			if(StringUtils.isBlank(carRegister)){
				return  null;
			}
			carRegisterStr = startFlag + headerDatas + carRegister + CRC_CODE + EndFlag;
		} catch (Exception e) {
			log.info("---------转换车辆注册数据失败 -----------"+device.getVehicleNo());
			e.printStackTrace();
		}
		return carRegisterStr;

	}

	// 转换定位数据
	public String transPositionMes(Device device, Position pos) {
		String posStr = "";
		String headerDatas = "";
		MSG_LENGTH = "0000005F";// 定位长度
		try {
			// 1200子业务类型标识(补录)
			headerDatas = MSG_LENGTH + transHeaderDataMsg("1200");
			// 定位数据组装 65
			String ps =toPositionStr1(pos);
			if(ps == null){
				log.info("---------经纬度不在规定范围内 -----------", this.getClass().getName()+"device.getVehicleNo()=="+device.getVehicleNo());
				return null;
			}
			
			posStr = toPositionStr0(device,pos)+ps;
		} catch (Exception e) {
			log.info("---------转换定位数据失败 --------1---", this.getClass().getName()+"device.getVehicleNo()=="+device.getVehicleNo());
			e.printStackTrace();
		}
		posStr = startFlag + headerDatas + posStr + CRC_CODE + EndFlag;
		return posStr;
	}
	// 转换定位补发数据
	public String transPositionReisMes(Device device ,Position pos) {
		String posStr = "";
		String headerDatas = "";
		MSG_LENGTH = "000000B2";// 定位长度
		try {
			headerDatas = MSG_LENGTH + transHeaderDataMsg("1200");
			// 定位数据组装
			String ps =toPositionStr1(pos);
			if(ps == null){
				log.info("---------经纬度不在规定范围内 -----------", this.getClass().getName()+"device.getVehicleNo()=="+device.getVehicleNo());
				return null;
			}
			posStr = toPositionReisMes(device,pos)+ps;
			
		} catch (Exception e) {
			log.info("---------转换定位补发数据失败 -----------", this.getClass().getName()+"device.getVehicleNo()=="+device.getVehicleNo());
			e.printStackTrace();
		}
		posStr = startFlag + headerDatas + posStr + CRC_CODE + EndFlag;
		return posStr;
	}

	public String toPositionReisMes(Device device,Position pos) throws Exception {
		// 车牌号 OctetString 21
		String vehicle_no = "";
		if(device!=null && StringUtils.isNotEmpty(device.getVehicleNo())){
			vehicle_no = BytesConverter.writeString(device.getVehicleNo(), 42);
		}else{
			vehicle_no = BytesConverter.writeString("0", 42);
		}
		// 车辆颜色，按照 JT/T415-2006中5.4.12的规定 BYTE 1
		String vehicle_color = "02";
		// 子业务类型标识 Uint16_t 2 实时上传信息
		String data_type = "1203";
		// 后续数据长度 Uint32_t 4
		String data_length = String.valueOf(BytesConverter.writeInteger(74, 8));
		// 该数据包里包含的卫星定位数据个数，1<=GNSS_CNT<=5 ---------》凯步设备一次传一个
		String gnss_cnt = String.valueOf(BytesConverter.writeInteger(1, 2));
		return vehicle_no+vehicle_color+data_type+data_length+gnss_cnt;

	}

	// 组装头数据信息
	private String transHeaderDataMsg(String flag) throws Exception {
		// 报文序列号 32位 4字节
		String msg_sn = BytesConverter.writeInteger(1, 8);
		// System.out.println("报文序列号 32位 4字节=" + MSG_SN);
		// 业务数据类型 16位 2字节 //4608=1200(业务数据类型) 4097 登录
		/**
		 * 子业务类型标识 判断是登录消息还是推送数据消息
		 */
		String msg_id = flag;
		// System.out.println("MSG_ID=" + MSG_ID);
		// 下级平台接入码，上级平台给下级平台分配的唯一标识号 32位 4字节
		String msg_gnsscenterid = BytesConverter.writeInteger(1145, 8);
		// System.out.println("下级平台接入码32位 4字节=" + MSG_GNSSCENTERID);
		// 协议版本号标识，上下级平台之间采用的标准协议版本编号；长度为3个字节来表示，0x01 0x02 0x0F表示的版本号是
		// v1.2.15，依此类推 单字节
		String version_flag = "01020F";
		// String VERSION_FLAG = BytesConverter.writeString("100", 6);// 版本号100
		// 报文加密标识位b: 0表示报文不加密，1表示报文加密 单字节
		String encrypt_flag = "00";
		// System.out.println("ENCRYPT_FLAG==" + ENCRYPT_FLAG);
		// 数据加密的密钥，长度为4个字节
		String encrypt_key = BytesConverter.writeInteger(0, 8);
		// System.out.println("ENCRYPT_KEY==" + ENCRYPT_KEY);
		// 数据头
		headerData = msg_sn + msg_id + msg_gnsscenterid + version_flag + encrypt_flag + encrypt_key;
		log.info("头数据：{}",headerData);
		return headerData;
	}

	// 组装登录信息
	private String transLoginMsg() {
		// 用户名 4
		String userId = "";
		// 密码 8
		String password = "";
		// 提供下级平台服务器的IP地址 32
		String down_link_ip = "";
		// 提供下级平台服务器的端口号 2
		String down_link_port = "";
		try {
			userId = BytesConverter.writeInteger(Integer.valueOf(Helper.get("transport.tcp.user")), 8);
			password = BytesConverter.writeString(Helper.get("transport.tcp.pw"), 16);
			down_link_ip = BytesConverter.writeString(Helper.get("transport.tcp.local.ip"), 64);
			down_link_port = BytesConverter.writeInteger(15426, 4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loginBody = userId + password + down_link_ip + down_link_port;
		log.info("组装登录信息：{}",loginBody);
		return loginBody;
	}

	// 车辆注册信息
	String tranCarRegister(Device device,String flag) throws Exception {
		// 21 OctetString 车牌号
		String vehicle_no = "";
		if(device != null && StringUtils.isNotEmpty(device.getVehicleNo())){
			vehicle_no = BytesConverter.writeString(device.getVehicleNo(), 42);
		}else{
			return  null;
		}

		// 1 BYTE 车辆颜色，按照JT/T415-2006中5.4.12的规定
		// String VEHICLE_COLOR = BytesConverter.writeString("1", 2);
		String vehicle_color ="";
		if(StringUtils.isNotEmpty(device.getVehicleColor())){
			vehicle_color=device.getVehicleColor();
		}else {
			vehicle_color = "02";
		}

		// 2 Uint16_t 子业务类型标识(实时？补录？)
		String data_typebytes = flag;
		// 4 Uint32_t 后续数据长度
		String data_length = String.valueOf(BytesConverter.writeInteger(0, 8));
		// 11 BYTES 平台唯一编码---00000000244
		String plateform_id = BytesConverter.writeString("00000000244", 22);
		// 11 BYTES 车载终端厂商唯一编码
		String producer_id = BytesConverter.writeString("0", 22);
		// 8 BYTES 车载终端型号，不足8位时以“\0”终结
		String terminal_model_type = BytesConverter.writeString("0", 16);
		// 7 BYTES 车载终端编号，大写字母和数字组成
		String terminal_id = BytesConverter.writeString("0", 14);
		// 12 OctetString 车载终端SIM卡电话号码。号码不足12 位，则在前面补充数字0
		String terminal_simcode = BytesConverter.writeString("0", 24);
		// 6 OctetString 该业户ID为下级平台存储的租赁业户在运输局的备案号----北汽福斯特000851
		String owers_id = BytesConverter.writeString("000851", 12);

		String regStr = vehicle_no + vehicle_color + data_typebytes + data_length + plateform_id + producer_id
				        + terminal_model_type + terminal_id + terminal_simcode + owers_id;

		log.info("车辆注册信息：{}",regStr);
		return regStr;

	}

	// 表12 实时上传车辆定位信息消息数据体
	String toPositionStr0(Device device,Position pos) throws Exception {
		// 车牌号 OctetString 21
		String vehicle_no = "";
		if(device != null && StringUtils.isNotEmpty(device.getVehicleNo())){
			vehicle_no = BytesConverter.writeString(device.getVehicleNo(), 42);
		}else{
			return  null;
		}
		// 车辆颜色，按照 JT/T415-2006中5.4.12的规定 BYTE 1
		String vehicle_color = "02";
		// 子业务类型标识 Uint16_t 2 实时上传信息
		String data_type = "1202";
		// 后续数据长度 Uint32_t 4
		String data_length = String.valueOf(BytesConverter.writeInteger(0, 8));
		log.info("实时上传车辆定位信息消息数据体：{}",vehicle_no + vehicle_color + data_type + data_length);
		return vehicle_no + vehicle_color + data_type + data_length ;

	}

	// 表13 车辆定位信息数据体
	String toPositionStr1(Position pos) throws Exception {

		// 该字段标识传输的定位信息是否使用国家测绘局批准的地图保密插件进行加密加密标识：1-已加密，0-未加密 BYTE 1
		String excrypt = "00";
		// 日月年（dmyy），年的表示是先将年转换成两位十六进制数，如2009表示为0x07 0xD9 BYTES 4
		String date = "";
		// 时分秒（hms）BYTES 3
		String time = "";
		// 经度，单位为 1*10-6度 Uint32_t 4
		String lon = "";
		// 纬度，单位为 1*10-6度 Uint32_t 4
		String lat = "";
		// 速度，指卫星定位车载终端设备上传的行车速度信息，为必填项，单位为千米每小时（km/h） 2 Uint16_t
		String vec1 = "";
		// 行驶记录速度，指车辆行驶记录设备上传的行车速度信息，单位为千米每小时（km/h） 2 Uint16_t
		String vec2 = "";
		// 车辆当前总里程数，指车辆上传的行车里程数，单位为千米（km） 4 Uint32_t
		String vec3 = "";
		// 方向，0~359，单位为度(o)，正北为0，顺时针 2 Uint16_t
		String direction = "";
		// 海拔高度，单位为米（m） 2 Uint16_t
		String altitude = "";
		// 车辆状态，二进制表示:B31B30……B2B1B0，具体定义见下表14 4 Uint32_t
		String state = "";
		// 报警状态，二进制表示，0表示正常，1表示报警：B31B30……B2B1B0，具体定义见下表15 4 Uint32_t
		String alarm = "";
		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
//		c.setTimeInMillis(pos.getSystime());
		
		// 日
		String dayTem = toString(Integer.toHexString(c.get(Calendar.DAY_OF_MONTH)), 2);
		// 月
		String monthTem = toString(Integer.toHexString(c.get(Calendar.MONTH) + 1), 2);
		// 年
		String yearTem = toString(Integer.toHexString(c.get(Calendar.YEAR)), 4);
		date = toString(dayTem + monthTem + yearTem, 8);
		// 时
		String hourTem = toString(Integer.toHexString(c.get(Calendar.HOUR_OF_DAY)), 2);
		// 分
		String minTem = toString(Integer.toHexString(c.get(Calendar.MINUTE)), 2);
		// 秒
		String secTem = toString(Integer.toHexString(c.get(Calendar.SECOND)), 2);
		time = hourTem + minTem + secTem;
		DecimalFormat formater = new DecimalFormat("#############0");
		
//		交通部经纬应该是  73--136   纬度 3-54超过这个范围就是错的
		if( Double.valueOf(pos.getLng()) >136 || Double.valueOf(pos.getLng()) < 73){
			log.error("经度不在交通部规定范围之内设备号==="+pos.getDeviceSn()+"经度==="+pos.getLng());
			return null;
		}
		
		if( Double.valueOf(pos.getLat()) >54 || Double.valueOf(pos.getLat()) < 3){
			log.error("纬度不在交通部规定范围之内设备号==="+pos.getDeviceSn()+"纬度==="+pos.getLat());
			return null;
		}

		lon = toString(Integer.toHexString(Integer.valueOf(formater.format(pos.getLng() * 1000000))), 8);
		// 纬度
		lat = toString(Integer.toHexString(Integer.valueOf(formater.format(pos.getLat() * 1000000))), 8);
		vec1 = toString(Integer.toHexString(Integer.valueOf(formater.format(pos.getSpeed() / 1000))), 4);
		vec2 = toString(Integer.toHexString(Integer.valueOf(formater.format(0))), 4);
		vec3 = toString(Integer.toHexString(Integer.valueOf(formater.format(0))), 8);
		direction = toString(Integer.toHexString(Integer.valueOf(formater.format(3256 / 1000))), 4);
		altitude = toString(Integer.toHexString(Integer.valueOf(formater.format(pos.getDirection()))), 4);
		state = toString(Integer.toHexString(Integer.valueOf(formater.format(0))), 8);
		alarm = toString(Integer.toHexString(Integer.valueOf(formater.format(0))), 8);

		String str =  excrypt + date + time + lon + lat + vec1 + vec2 + vec3 + direction + altitude + state + alarm;
		return str;
	}


	/**
	 * 补齐长度
	 */
	public static String toString(String value, int size) {
		while (value.length() < size) {
			value = "0" + value;
		}

		if (value.length() > size) {
			value = value.substring(0, size);
		}
		return value;
	}

	public static void main(String[] args)  {
		String LON ="40.009878";
		String LAT ="40.009878";
		if( Double.valueOf(LON) >136 || Double.valueOf(LON) < 73){
			System.out.println("12");
		}
		if( Double.valueOf(LAT) >54 || Double.valueOf(LAT) < 3){
			System.out.println("124r4");
		}
		System.out.println(1);
	}

}

package com.push.handle;

import com.push.model.Device;
import com.push.model.Position;
import com.push.util.BytesConverter;
import com.push.util.Helper;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.AbstractIoSession;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * mina IoHandler
 */
@Component
public abstract class TcrIoHandler implements IoHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private DeviceCache deviceCache;

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        logger.info("new session created!");
        // 设置IoSession闲置时间，参数单位是秒
        session.getConfig().setIdleTime(IdleStatus.READER_IDLE, 10);
    }

    // 当连接打开是调用；
    @Override
    public void sessionOpened(IoSession session) {
        logger.info("登录（sessionOpened）=========");
        TransMessage tm = new TransMessage();

        /**
         * 登录
         */
        session.setAttribute("ip", Helper.get("transport.tcp.local.ip"));
        String str = tm.transLoginMes();
        session.write(BytesConverter.converString(str));

        /**
         * 注册
         * 1：从内存中捞出所有车辆信息
         * 2：注册
         */
        List<Device> listDevice = new ArrayList<>();
        listDevice = deviceCache.getCars();
        for (Device d : listDevice) {
            if (d != null) {
                logger.info("车牌号=========" + d.getVehicleNo());
                String str1 = tm.carRegisterMes(d);
                session.write(BytesConverter.converString(str1));
            }
        }
        logger.info("注册完毕=========》");

    }

    // 当连接关闭时调用；
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        logger.info("new session close!");
    }

    // 当连接进入空闲状态时调用；
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        logger.info(" sessionIdle ------!");
//        TransMessage tm = new TransMessage();
//        /**
//         * 注册
//         * 1：从内存中捞出所有车辆信息
//         * 2：注册
//         */
//        List<Device> listDevice = new ArrayList<>();
//        listDevice = deviceCache.getCars();
//        for (Device d : listDevice) {
//            if (d != null) {
//                logger.info("车牌号=========" + d.getVehicleNo());
//                String str1 = tm.carRegisterMes(d);
//                session.write(BytesConverter.converString(str1));
//            }
//        }
//        logger.info("注册完毕=========》");

    }

    // 当实现IoHandler的类抛出异常时调用；
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
        logger.info("exceptionCaught====>" + cause.getMessage());
        session.close(true);
    }

    // 当接收了一个消息时调用；
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        System.out.println("messageReceived 当接收了一个消息时调用");
    }

    // 当一个消息被(IoSession#write)发送出去后调用；
    @Override
    public void messageSent(IoSession session, Object message) {
        ((AbstractIoSession) session).getProcessor().flush(session);
    }

    public abstract void writePosition(Position pos);
}

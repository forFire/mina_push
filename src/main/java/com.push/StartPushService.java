package com.push;

import com.push.handle.PushHandler;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;


/**
 * 启动类
 */
public class StartPushService {

	private Logger log = LoggerFactory.getLogger(getClass());
	private AbstractApplicationContext context = new FileSystemXmlApplicationContext("conf/context.xml");

	private static final Object obj = new Object();
	private static StartPushService pushService;

	private StartPushService() {
	}

	public static StartPushService getInstance() {
		synchronized (obj) {
			pushService = new StartPushService();
		}
		return pushService;
	}

	private void start() {
		log.info("start push service .");
		PushHandler handler = this.getContext().getBean(PushHandler.class);
		handler.connect();
	}

	public AbstractApplicationContext getContext() {
		return context;
	}

	public static void main(String args[]) {
		PropertyConfigurator.configure("conf/log4j.properties");
		StartPushService pushService = StartPushService.getInstance();
		pushService.start();
	}
}

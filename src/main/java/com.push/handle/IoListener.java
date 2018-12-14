package com.push.handle;

import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class IoListener implements IoServiceListener{  
    @Override  
    public void serviceActivated(IoService arg0) throws Exception {  
    	System.out.println("serviceActivated"); 
    }  
    @Override  
    public void serviceDeactivated(IoService arg0) throws Exception {  
        System.out.println("serviceDeactivated"); 
    }  
    @Override  
    public void serviceIdle(IoService arg0, IdleStatus arg1) throws Exception {  
    	System.out.println("serviceIdle"); 
    }  
    @Override  
    public void sessionCreated(IoSession arg0) throws Exception {  
    	System.out.println("sessionCreated"); 
    }  
  
    @Override  
    public void sessionDestroyed(IoSession arg0) throws Exception {  
    	System.out.println("sessionDestroyed"); 
    }
    @Override
    public void sessionClosed(IoSession arg0) throws Exception {
        System.out.println("sessionClosed");
    }

}
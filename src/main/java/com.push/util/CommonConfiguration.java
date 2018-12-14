package com.push.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class CommonConfiguration {

    private static final String name       = "/application.properties";

    private static final String local_name = "conf/application.properties";

    private Logger              log        = LoggerFactory.getLogger (getClass ());

    private Properties          properties;
    
    

    public CommonConfiguration () {

        properties = new Properties ();
        InputStream inStream = null;

        try {
            if (inStream == null) {
                inStream = new FileInputStream (new File (local_name));
            }

        }
        catch (IOException e) {
            log.error ("load commonconfig from conf/application.properties error : {}", e.getMessage ());
        }
        try {
            if (inStream == null)
                inStream = this.getClass ().getResourceAsStream (name);
            if (inStream != null) {
                this.properties.load (inStream);
                inStream.close ();
            }
        }
        catch (Exception e) {
            log.error ("load commonconfig from application.properties in class path , error : {}", e.getMessage ());
        }
    }

    protected Properties getProperties () {

        return this.properties;
    }

}

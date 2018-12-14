package com.push.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Helper extends CommonConfiguration {

    private static Properties  prop;
    private static Logger      logger = Logger.getLogger (Helper.class);
    private static InputStream inputStream;

    static {
        try {
            Helper helper = new Helper ();
            prop = helper.getProperties ();
        }
        catch (Exception ex) {
            try {
                if (inputStream != null) {
                    inputStream.close ();
                }
            }
            catch (IOException e) {
                e.printStackTrace ();
            }
            ex.printStackTrace ();
            logger.error (ex, ex);
        }
    }

    public Helper () {

        super ();
    }

    public static String get (String key) {

        return (String) prop.getProperty (key);
    }

    /**
     * 是否分表
     * 
     * @return
     */
    public static boolean isDividedTable () {

        String str = get ("divided_table");
        if ("yes".equalsIgnoreCase (str)) {
            return true;
        }
        return false;
    }
}

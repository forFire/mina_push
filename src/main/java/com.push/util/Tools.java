package com.push.util;

public class Tools {

    public static boolean checkNull (Object obj) {

        return obj == null;
    }

    public static boolean checkNotNull (Object obj) {

        return obj != null;
    }

    public static boolean checkEmpty (Object obj) {

        return checkNull (obj) ? true : obj.toString ().isEmpty ();
    }

    public static boolean checkNotEmpty (Object obj) {

        return checkNull (obj) ? false : !obj.toString ().isEmpty ();
    }

    public static long toLong (Object obj) {

        try {

            return Long.parseLong (obj.toString ());
        }
        catch (Exception e) {
            return 0;
        }
    }

    public static int toInteter (Object obj) {

        try {

            return Integer.parseInt (obj.toString ());
        }
        catch (Exception e) {
            return 0;
        }
    }
}

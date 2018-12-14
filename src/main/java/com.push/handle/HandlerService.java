package com.push.handle;

import java.util.Hashtable;

public class HandlerService {

    private static Hashtable <String, TcrIoHandler> hashTable = new Hashtable <String, TcrIoHandler> ();

    public static TcrIoHandler getTcrIoHandler (String name) {

        return hashTable.get (name);
    }

    public static void set (String name, TcrIoHandler ioHandler) {

        hashTable.put (name, ioHandler);
    }

}

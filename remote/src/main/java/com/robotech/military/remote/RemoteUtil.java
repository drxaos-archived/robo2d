package com.robotech.military.remote;

import com.robotech.military.api.internal.Request;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class RemoteUtil {

    public static Object request(String rid, String method, Object... args) {
        Request request = new Request(rid, method, args);
        return sendObject(request);
    }

    public static Object sendObject(Object obj) {
        URLConnection conn = null;
        Object reply = null;
        try {

            // open URL connection
            URL url = new URL("http://localhost:3741/remote");
            conn = url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            // send object
            ObjectOutputStream objOut = new ObjectOutputStream(conn.getOutputStream());
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        // recieve reply
        try {
            ObjectInputStream objIn = new ObjectInputStream(conn.getInputStream());
            reply = objIn.readObject();
            objIn.close();
        } catch (Exception ex) {
            // it is ok if we get an exception here
            // that means that there is no object being returned
            System.out.println("No Object Returned");
            if (!(ex instanceof EOFException))
                ex.printStackTrace();
            System.err.println("*");
        }
        return reply;
    }
}

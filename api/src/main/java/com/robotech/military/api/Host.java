package com.robotech.military.api;

import java.lang.reflect.Method;

public class Host {

    IO io;

    public Host(IO io) {
        this.io = io;
    }

    public void debug(String msg) {
        io.set("debug", msg);
        System.out.println(msg);
    }

    public <T> T getDevice(Class<T> type) {
        try {
            T dev = type.getConstructor(IO.class).newInstance(io);
            Method ready = type.getMethod("ready");
            return (ready != null && ready.invoke(dev) == Boolean.FALSE) ? null : dev;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

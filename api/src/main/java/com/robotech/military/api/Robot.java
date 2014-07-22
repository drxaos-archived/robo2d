package com.robotech.military.api;

import java.lang.reflect.Method;

public class Robot {

    IO io;

    public Robot(IO io) {
        this.io = io;
    }

    public Double getEnergy() {
        return Double.valueOf(io.get("robot/energy"));
    }

    public String getUid() {
        return io.get("robot/uid");
    }

    public void debug(String msg) {
        io.set("debug", msg);
    }

    public <T> T getDevice(Class<T> type) {
        try {
            T dev = type.getConstructor(IO.class).newInstance(io);
            Method ready = type.getMethod("ready");
            return (ready != null && ready.invoke(dev) == Boolean.FALSE) ? null : dev;
        } catch (Exception e) {
            return null;
        }
    }
}

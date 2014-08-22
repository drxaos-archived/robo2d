package com.robotech.military.api;

public class Direct {
    IO io;

    public Direct(IO io) {
        this.io = io;
    }

    public boolean ready() {
        return true;
    }

    public String get(String key) {
        return io.get(key);
    }

    public void set(String key, String value) {
        io.set(key, value);
    }
}

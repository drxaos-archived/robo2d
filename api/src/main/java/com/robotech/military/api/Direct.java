package com.robotech.military.api;

import java.io.Serializable;

public class Direct implements Serializable {
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

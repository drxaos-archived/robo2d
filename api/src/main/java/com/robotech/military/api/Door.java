package com.robotech.military.api;

public class Door {
    IO io;
    String key = "main";

    public Door(IO io) {
        this.io = io;
    }

    public boolean ready() {
        return true;
    }

    public void select(String key) {
        this.key = key;
    }

    public void open() {
        io.set("door/" + key + "/control", "open");
    }

    public void close() {
        io.set("door/" + key + "/control", "close");
    }
}

package com.robotech.military.api;

import java.io.Serializable;

public class Radio implements Serializable {

    IO io;

    public Radio(IO io) {
        this.io = io;
    }

    public boolean ready() {
        return io.get("radio/ready") != null;
    }

    public void setChannel(byte channel) {
        io.set("radio/channel", "" + channel);
    }

    public void broadcast(String message) {
        io.set("radio/output", message);
    }

    public String listen() {
        return io.get("radio/input");
    }
}

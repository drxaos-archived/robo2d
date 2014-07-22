package com.robotech.military.api;

public class Chassis {
    IO io;

    public Chassis(IO io) {
        this.io = io;
    }

    public boolean ready() {
        return io.get("chassis/ready") != null;
    }

    public void setLeftAcceleration(Double percent) {
        io.set("chassis/left", "" + percent);
    }

    public void setRightAcceleration(Double percent) {
        io.set("chassis/right", "" + percent);
    }

}

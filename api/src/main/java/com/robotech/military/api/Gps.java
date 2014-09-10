package com.robotech.military.api;

import java.io.Serializable;

public class Gps implements Serializable {
    IO io;

    public Gps(IO io) {
        this.io = io;
    }

    public boolean ready() {
        return io.get("gps/ready") != null;
    }

    public Point getPosition() {
        return new Point(Float.valueOf(io.get("gps/posx")), Float.valueOf(io.get("gps/posy")));
    }

    public Float getAngle() {
        return Float.valueOf(io.get("gps/angle"));
    }

    public Long getTime() {
        return Long.valueOf(io.get("gps/time"));
    }
}

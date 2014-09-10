package com.robotech.military.api;

import java.io.Serializable;

public class Radar implements Serializable {

    public static final String UNKNOWN = "unknown";
    public static final String EMPTY = "empty";
    public static final String WALL = "wall";
    public static final String ENEMY = "enemy";
    public static final String MATE = "mate";

    IO io;

    public Radar(IO io) {
        this.io = io;
    }

    public boolean ready() {
        return io.get("radar/ready") != null;
    }

    public void rotate(double angle) {
        io.set("radar/angle", "" + angle);
    }

    public Float distance() {
        return Float.valueOf(io.get("radar/distance"));
    }

    public String type() {
        return io.get("radar/type");
    }
}

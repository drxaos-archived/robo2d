package com.robotech.military.api.map;


import com.robotech.military.api.Player;

import java.awt.geom.Point2D;
import java.util.List;

public interface Obj {

    public static enum Type {
        BOT,
        STATION,
        WALL,
        WATER,
        ENERGY_CELL,
        MINE
    }

    Player getOwner();

    Type getType();

    List<Point2D> getVertices();
}

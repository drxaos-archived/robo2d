package robo2d.game.impl;

import robo2d.game.api.Player;
import robo2d.game.api.map.Obj;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.List;

public class WallImpl implements Obj {

    List<Point2D> vertices;

    public WallImpl(List<Point2D> vertices) {
        this.vertices = Collections.unmodifiableList(vertices);
    }

    @Override
    public Player getOwner() {
        return null;
    }

    @Override
    public Type getType() {
        return Type.WALL;
    }

    @Override
    public List<Point2D> getVertices() {
        return vertices;
    }
}

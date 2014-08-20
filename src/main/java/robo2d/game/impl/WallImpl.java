package robo2d.game.impl;

import robo2d.game.box2d.Box;
import robo2d.game.box2d.Physical;
import robo2d.game.box2d.PolygonUtil;
import robo2d.game.box2d.StaticBox;
import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WallImpl implements Physical {

    StaticBox box;
    List<Point2D> vertices;
    String type;

    public WallImpl(List<Point2D> vertices) {
        this(vertices, null);
    }

    public WallImpl(List<Point2D> vertices, String type) {
        this.type = type == null ? "rock" : type;
        PolygonUtil.clockwise(vertices);
        this.vertices = Collections.unmodifiableList(new ArrayList<Point2D>(vertices));

        ArrayList<KPoint> kPoints = new ArrayList<KPoint>();
        for (Point2D vertice : this.vertices) {
            kPoints.add(new KPoint(vertice.getX(), vertice.getY()));
        }

        box = new StaticBox(new KPolygon(kPoints), new KPoint(0, 0), 0);
    }

    public String getType() {
        return type;
    }

    public List<Point2D> getVertices() {
        return vertices;
    }

    public List<KPolygon> getTriangulation() {
        return box.getTriangulated();
    }

    @Override
    public Box getBox() {
        return box;
    }
}

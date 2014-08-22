package robo2d.game.impl.dynamics;

import robo2d.game.box2d.Box;
import robo2d.game.box2d.Physical;
import robo2d.game.box2d.PolygonUtil;
import robo2d.game.box2d.StaticBox;
import robo2d.game.impl.ControllerImpl;
import robo2d.game.impl.Dynamic;
import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DoorImpl implements Physical, Dynamic {

    StaticBox box;
    List<Point2D> vertices;
    String type;

    ControllerImpl controller;
    String controllerKey, controllerOpen, controllerClose;
    int elevation;
    int threshold = 5;

    public DoorImpl(List<Point2D> vertices) {
        this(vertices, null, null);
    }

    public DoorImpl(List<Point2D> vertices, String type, Integer elevation) {
        this.type = type == null ? "door" : type;
        this.elevation = elevation == null ? 100 : elevation;
        PolygonUtil.clockwise(vertices);
        this.vertices = Collections.unmodifiableList(new ArrayList<Point2D>(vertices));

        ArrayList<KPoint> kPoints = new ArrayList<KPoint>();
        for (Point2D vertice : this.vertices) {
            kPoints.add(new KPoint(vertice.getX(), vertice.getY()));
        }

        box = new StaticBox(new KPolygon(kPoints), new KPoint(0, 0), 0);
    }

    public int getElevation() {
        return elevation;
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

    public void setController(ControllerImpl controller, String key, String open, String close) {
        this.controller = controller;
        this.controllerKey = key;
        this.controllerOpen = open;
        this.controllerClose = close;
    }

    @Override
    public void init() {
        if (this.elevation < threshold) {
            box.body.setActive(false);
        } else {
            box.body.setActive(true);
        }
    }

    @Override
    public void update() {
        if (controller != null) {
            String state = controller.getComputer().getStateString(controllerKey);
            if (controllerClose.equals(state)) {
                if (elevation < 100) {
                    elevation += 1;
                    if (elevation >= threshold) {
                        box.body.setActive(true);
                    }
                }
            } else if (controllerOpen.equals(state)) {
                if (elevation > 0) {
                    elevation -= 1;
                    if (elevation < threshold) {
                        box.body.setActive(false);
                    }
                }
            }
        }
    }
}

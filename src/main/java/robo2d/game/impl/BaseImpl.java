package robo2d.game.impl;

import com.jme3.math.FastMath;
import robo2d.game.box2d.Box;
import robo2d.game.box2d.Physical;
import robo2d.game.box2d.StaticBox;
import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class BaseImpl implements Physical, Enterable {

    public static final float SIZE = 6;

    StaticBox box;
    KPoint pos;
    float angle;

    PlayerImpl enteredPlayer;

    public KPoint getPos() {
        return pos;
    }

    public float getAngle() {
        return angle;
    }

    public BaseImpl(KPoint pos, float angle) {
        this.pos = pos;
        this.angle = angle;

        ArrayList<KPoint> kPoints = new ArrayList<KPoint>();

        kPoints.add(new KPoint(1f, 1f));
        kPoints.add(new KPoint(1f, -1f));
        kPoints.add(new KPoint(-1f, -1f));
        kPoints.add(new KPoint(-1f, 1f));
        kPoints.add(new KPoint(-1f / 4f, 1f));
        kPoints.add(new KPoint(-1f / 4f, 7f / 9f));
        kPoints.add(new KPoint(-8f / 9f, 7f / 9f));
        kPoints.add(new KPoint(-8f / 9f, -8f / 9f));
        kPoints.add(new KPoint(8f / 9f, -8f / 9f));
        kPoints.add(new KPoint(8f / 9f, 7f / 9f));
        kPoints.add(new KPoint(1f / 4f, 7f / 9f));
        kPoints.add(new KPoint(1f / 4f, 1f));
        KPolygon polygon = new KPolygon(kPoints);
        polygon.scale(SIZE * 0.7);
        polygon.rotate(-FastMath.PI / 2);

        box = new StaticBox(polygon, pos, angle);
    }

    @Override
    public Box getBox() {
        return box;
    }

    @Override
    public boolean canEnter(PlayerImpl player) {
        return true;
    }

    @Override
    public void enter(PlayerImpl player) {
        enteredPlayer = player;
    }

    @Override
    public Point2D exit() {
        enteredPlayer = null;
        return getBox().getPosition();
    }
}

package robo2d.game.impl;

import com.jme3.math.FastMath;
import robo2d.game.box2d.Box;
import robo2d.game.box2d.Physical;
import robo2d.game.box2d.StaticBox;
import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class BaseImpl implements Physical, Enterable {

    public static final float SIZE = 6;

    StaticBox box;
    List<Point2D> vertices;
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

        this.vertices = new ArrayList<Point2D>();

        ArrayList<KPoint> kPoints = new ArrayList<KPoint>();
        for (int i = 0; i < 4; i++) {
            float x = FastMath.sin(FastMath.PI / 4 + FastMath.PI / 2 * i) * SIZE;
            float y = FastMath.cos(FastMath.PI / 4 + FastMath.PI / 2 * i) * SIZE;
            vertices.add(new Point2D.Float(x, y));
            kPoints.add(new KPoint(x, y));
        }

        box = new StaticBox(new KPolygon(kPoints), pos, angle);
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

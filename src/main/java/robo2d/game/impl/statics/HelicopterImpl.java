package robo2d.game.impl.statics;

import com.jme3.math.FastMath;
import com.robotech.game.box.Box;
import com.robotech.game.Physical;
import com.robotech.game.box.StaticBox;
import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;

import java.util.ArrayList;

public class HelicopterImpl implements Physical {

    public static final float SIZE = 6;

    StaticBox box;
    KPoint pos;
    float angle;

    public KPoint getPos() {
        return pos;
    }

    public Double getAngle() {
        return (double) angle - FastMath.PI / 2;
    }

    public HelicopterImpl(KPoint pos, float angle) {
        this.pos = pos;
        this.angle = angle + FastMath.PI;

        ArrayList<KPoint> kPoints = new ArrayList<KPoint>();

        kPoints.add(new KPoint(1.5f, 3f));
        kPoints.add(new KPoint(1.5f, -3f));
        kPoints.add(new KPoint(-2f, -3f));
        kPoints.add(new KPoint(-2f, 3f));
        KPolygon polygon = new KPolygon(kPoints);
        //polygon.scale(SIZE * 0.05);

        box = new StaticBox(polygon, pos, angle + FastMath.PI / 2);
    }

    @Override
    public Box getBox() {
        return box;
    }
}

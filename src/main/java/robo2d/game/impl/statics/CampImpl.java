package robo2d.game.impl.statics;

import com.jme3.math.FastMath;
import com.robotech.game.box.Box;
import com.robotech.game.Physical;
import com.robotech.game.box.StaticBox;
import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;

import java.util.ArrayList;

public class CampImpl implements Physical {

    public static final float SIZE = 6;

    public static KPolygon polygon;
    static {
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
        polygon = new KPolygon(kPoints);
        polygon.scale(SIZE * 0.7);
        polygon.rotate(-FastMath.PI / 2);
    }

    StaticBox box;
    KPoint pos;
    float angle;

    public KPoint getPos() {
        return pos;
    }

    public Double getAngle() {
        return (double) angle;
    }

    public CampImpl(KPoint pos, float angle) {
        this.pos = pos;
        this.angle = angle;
        box = new StaticBox(polygon, pos, angle);
    }

    @Override
    public Box getBox() {
        return box;
    }
}

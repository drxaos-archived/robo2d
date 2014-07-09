package robo2d.game.box2d;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Box {
    public BodyDef bodyDef;
    public List<FixtureDef> fixtureDefs = new ArrayList<FixtureDef>();
    public Body body;

    public double getAngle() {
        if (body == null) {
            return 0d;
        }
        return (double) body.getAngle();
    }

    public Point2D getPosition() {
        if (body == null) {
            return new Point2D.Double();
        }
        Vec2 vec2 = body.getPosition();
        return new Point2D.Double(vec2.x, vec2.y);
    }

    public void setPosition(float x, float y) {
        if (body == null) {
            return;
        }
        body.applyForce(new Vec2(x, y).sub(body.getPosition()), body.getPosition());
    }

    public Vec2 getPositionVec2() {
        if (body == null) {
            return new Vec2();
        }
        return body.getPosition();
    }

    public boolean hasPoint(Vec2 point) {
        if (body == null) {
            return false;
        }
        for (Fixture f = body.getFixtureList(); f != null; f = f.getNext()) {
            if (f.testPoint(point)) {
                return true;
            }
        }
        return false;
    }

    public void resetPosition(float x, float y) {
        if (body == null) {
            return;
        }
        body.setType(BodyType.STATIC);
        body.setTransform(new Vec2(x, y), 0);
        body.setType(BodyType.DYNAMIC);
    }
}

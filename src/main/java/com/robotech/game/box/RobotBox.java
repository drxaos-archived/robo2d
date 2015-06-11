package com.robotech.game.box;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import straightedge.geom.KPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RobotBox extends Box {

    BodyDef bodyDef;
    List<FixtureDef> fixtureDefs = new ArrayList<FixtureDef>();

    public RobotBox(String uid, float x, float y, float angle) {
        float size = 1;
        {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(size, size);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.friction = 0.8f;
            fixtureDef.density = 1f;

            fixtureDefs.add(fixtureDef);
        }

        {
            PolygonShape shape = new PolygonShape();
            shape.set(new Vec2[]{
                    new Vec2(-0.8f * size, 0),
                    new Vec2(0, 1.2f * size),
                    new Vec2(0.8f * size, 0),
            }, 3);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.friction = 0.8f;
            fixtureDef.density = 1f;

            fixtureDefs.add(fixtureDef);
        }

        bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(x, y);
        bodyDef.angle = angle;
        bodyDef.allowSleep = false;
        bodyDef.linearDamping = 10f;
        bodyDef.angularDamping = 30f;
        bodyDef.userData = uid;
    }

    public double getAngle() {
        if (body == null) {
            return 0d;
        }
        return (double) body.getAngle() - Math.PI / 2;
    }

    public void applyForces(String uid, Map<KPoint, Vec2> forces) {
        if (body == null) {
            return;
        }
        float size = getSize(uid);
        float a = body.getAngle();
        for (Map.Entry<KPoint, Vec2> e : forces.entrySet()) {

            Vec2 worldVector = body.getWorldVector(e.getValue());
            Vec2 point = new Vec2((float) e.getKey().getX(), (float) e.getKey().getY());
            point.normalize();
            point.mul(size);
            Vec2 worldPoint = body.getWorldPoint(point);

            body.applyForce(worldVector, worldPoint);
        }
    }
}

package robo2d.game.box2d;

import com.robotech.game.box.Box;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import straightedge.geom.KPoint;

public class PlayerBox extends Box {

    public static double SIZE = 0.5;

    public PlayerBox(KPoint position) {

        {
            CircleShape shape = new CircleShape();
            shape.setRadius((float) SIZE);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.friction = 0.8f;
            fixtureDef.density = 0.1f;

            fixtureDefs.add(fixtureDef);
        }

        bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set((float) position.getX(), (float) position.getY());
        bodyDef.angle = 0f;
        bodyDef.allowSleep = false;
        bodyDef.linearDamping = 10f;
        bodyDef.angularDamping = 30f;
    }

    public void setPosition(float x, float y) {
        if (body == null) {
            return;
        }
        body.applyForce(new Vec2(x, y).sub(body.getPosition()).mul(40), body.getPosition());
    }
}

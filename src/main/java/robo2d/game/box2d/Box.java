package robo2d.game.box2d;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

import java.util.ArrayList;
import java.util.List;

public class Box {
    public BodyDef bodyDef;
    public List<FixtureDef> fixtureDefs = new ArrayList<FixtureDef>();
}

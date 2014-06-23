package robo2d.game;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import robo2d.game.box2d.Physical;
import robo2d.game.impl.PlayerImpl;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public List<PlayerImpl> players = new ArrayList<PlayerImpl>();
    public List<Physical> physicals = new ArrayList<Physical>();
    public World worldBox;

    public void start() {
        for (Physical physical : physicals) {
            Body body = worldBox.createBody(physical.getBox().bodyDef);
            for (FixtureDef fixtureDef : physical.getBox().fixtureDefs) {
                body.createFixture(fixtureDef);
            }
        }
    }
}

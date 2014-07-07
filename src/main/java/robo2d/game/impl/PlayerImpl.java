package robo2d.game.impl;

import robo2d.game.api.Player;
import robo2d.game.box2d.Box;
import robo2d.game.box2d.Physical;
import robo2d.game.box2d.PlayerBox;
import straightedge.geom.KPoint;

public class PlayerImpl implements Player, Physical {
    String name;
    PlayerBox box;
    float initAngle;

    public PlayerImpl(String name, KPoint position, float angle) {
        this.name = name;
        this.initAngle = angle;
        box = new PlayerBox(position);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Box getBox() {
        return box;
    }

    public float getInitAngle() {
        return initAngle;
    }
}

package robo2d.game.impl;

import robo2d.game.api.Player;
import robo2d.game.box2d.Box;
import robo2d.game.box2d.Physical;
import robo2d.game.box2d.PlayerBox;
import straightedge.geom.KPoint;

public class PlayerImpl implements Player, Physical {
    String name;
    PlayerBox box;

    public PlayerImpl(String name) {
        this.name = name;
        box = new PlayerBox(new KPoint(0, 0));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Box getBox() {
        return box;
    }
}

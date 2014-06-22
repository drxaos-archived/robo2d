package robo2d.game.impl;

import robo2d.game.api.Player;

public class PlayerImpl implements Player {
    String name;
    Runnable program;

    @Override
    public String getName() {
        return name;
    }
}

package robo2d.game.impl;

import robo2d.game.api.Player;

public class PlayerImpl implements Player {
    String name;
    Class<? extends RobotProgram> program;

    public PlayerImpl(String name, Class<? extends RobotProgram> program) {
        this.name = name;
        this.program = program;
    }

    @Override
    public String getName() {
        return name;
    }
}

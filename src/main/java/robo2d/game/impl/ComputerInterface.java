package robo2d.game.impl;

import com.robotech.military.api.Robot;

public interface ComputerInterface {
    void deploy();

    void halt();

    Robot getRobotForDebug();
}

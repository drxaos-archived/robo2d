package robo2d.testbed.tests.base;

import robo2d.game.api.Chassis;
import robo2d.game.api.Radar;
import robo2d.game.impl.RobotProgram;
import straightedge.geom.KPoint;

import java.util.ArrayList;


public class BaseTestProgram extends RobotProgram {

    Chassis chassis;
    Radar radar;

    @Override
    public void program() {
        chassis = robot.getEquipment(Chassis.class);
        radar = robot.getEquipment(Radar.class);

        if (chassis == null || radar == null) {
            robot.debug("fail");
            return;
        }

        chassis.setLeftAcceleration(100d);
        chassis.setLeftAcceleration(30d);
    }
}

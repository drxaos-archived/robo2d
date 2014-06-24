package robo2d.testbed.tests.move;

import robo2d.game.api.Chassis;
import robo2d.game.api.Radar;
import robo2d.game.impl.RobotProgram;


public class EngineTestProgram extends RobotProgram {

    String out = "------";

    Chassis chassis;
    Radar radar;

    @Override
    public void program() {
        Runnable angleDebug = new Runnable() {
            @Override
            public void run() {
                robot.debug(out + " / " + radar.getAngle());
            }
        };

        robot.debug("init");

        chassis = robot.getEquipment(Chassis.class);
        radar = robot.getEquipment(Radar.class);

        if (chassis == null || radar == null) {
            robot.debug("fail");
            return;
        }

        out = ("-F--F-");
        chassis.setLeftAcceleration(100d);
        chassis.setRightAcceleration(100d);
        cycle(1000, angleDebug);
        out = ("------");
        chassis.setLeftAcceleration(0d);
        chassis.setRightAcceleration(0d);
        cycle(1000, angleDebug);
        out = ("----F-");
        chassis.setLeftAcceleration(0d);
        chassis.setRightAcceleration(100d);
        cycle(1000, angleDebug);
        out = ("------");
        chassis.setLeftAcceleration(0d);
        chassis.setRightAcceleration(0d);
        cycle(1000, angleDebug);
        out = ("-F----");
        chassis.setLeftAcceleration(100d);
        chassis.setRightAcceleration(0d);
        cycle(1000, angleDebug);
        out = ("------");
        chassis.setLeftAcceleration(0d);
        chassis.setRightAcceleration(0d);
        cycle(1000, angleDebug);
        out = ("-B--F-");
        chassis.setLeftAcceleration(-100d);
        chassis.setRightAcceleration(100d);
        cycle(1000, angleDebug);
        out = ("------");
        chassis.setLeftAcceleration(0d);
        chassis.setRightAcceleration(0d);
        cycle(1000, angleDebug);
    }
}

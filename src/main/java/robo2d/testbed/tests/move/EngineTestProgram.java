package robo2d.testbed.tests.move;

import robo2d.game.api.Chassis;
import robo2d.game.api.Radar;
import robo2d.game.impl.RobotProgram;


public class EngineTestProgram extends RobotProgram {

    String out = "------";

    Chassis chassis;
    Radar radar;

    Double waitAngle;

    @Override
    public void program() {
        Runnable angleWaiter = new Runnable() {
            @Override
            public void run() {
                String dbg = out + " / " + String.format("%.3f", radar.getAngle());

                if (waitAngle != null) {
                    double dif = Math.abs(differenceAngle(radar.getAngle(), waitAngle));
                    dbg += " / DIF:" + dif;
                    if (dif < 0.1) {
                        throw new Interrupt();
                    }
                }
                robot.debug(dbg);
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
        waitAngle = null;
        cycle(angleWaiter, 1000);
        out = ("------");
        chassis.setLeftAcceleration(0d);
        chassis.setRightAcceleration(0d);
        waitAngle = null;
        cycle(angleWaiter, 1000);
        out = ("----F-");
        chassis.setLeftAcceleration(0d);
        chassis.setRightAcceleration(100d);
        waitAngle = 0d;
        cycle(angleWaiter, 10000);
        out = ("------");
        chassis.setLeftAcceleration(0d);
        chassis.setRightAcceleration(0d);
        waitAngle = null;
        cycle(angleWaiter, 1000);
        out = ("-F----");
        chassis.setLeftAcceleration(100d);
        chassis.setRightAcceleration(0d);
        waitAngle = Math.PI / 2;
        cycle(angleWaiter, 10000);
        out = ("------");
        chassis.setLeftAcceleration(0d);
        chassis.setRightAcceleration(0d);
        waitAngle = null;
        cycle(angleWaiter, 1000);
        out = ("-B--F-");
        chassis.setLeftAcceleration(-100d);
        chassis.setRightAcceleration(100d);
        waitAngle = -Math.PI / 2;
        cycle(angleWaiter, 10000);
        out = ("------");
        chassis.setLeftAcceleration(0d);
        chassis.setRightAcceleration(0d);
        waitAngle = null;
        cycle(angleWaiter, 1000);
    }
}

package robo2d.testbed.tests.waypoints;

import robo2d.game.api.Chassis;
import robo2d.game.api.Radar;
import robo2d.game.impl.RobotProgram;
import straightedge.geom.KPoint;

import java.util.ArrayList;


public class WayTestProgram extends RobotProgram {

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

        ArrayList<KPoint> way = new ArrayList<KPoint>();
        way.add(new KPoint(0, -10));
        way.add(new KPoint(12, -5));
        way.add(new KPoint(12, 0));
        way.add(new KPoint(-12, 0));
        way.add(new KPoint(-12, 5));
        way.add(new KPoint(0, 10));
        way.add(new KPoint(15, 5));
//        way.add(new KPoint(15, -5));
        way.add(new KPoint(0, -10));

        for (KPoint wayPoint : way) {
            while (!moveSmooth(wayPoint, 4000)) {
                stop();
                KPoint failPoint = radar.getPosition();
                robot.debug(wayPoint);
                robot.debug("Please drag me to that point!");
                while (true) {
                    // save energy
                    KPoint newPoint = radar.getPosition();
                    if (distance(newPoint, failPoint) > 0.2) {
                        break;
                    }
                    robot.waitForStep();
                }
            }
            robot.debug(null);
        }
    }
}

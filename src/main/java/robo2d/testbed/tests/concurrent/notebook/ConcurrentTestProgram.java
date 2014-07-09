package robo2d.testbed.tests.concurrent.notebook;

import com.robotech.military.api.Chassis;
import com.robotech.military.api.Radar;
import com.robotech.military.os.RobotProgram;
import straightedge.geom.KPoint;

import java.awt.geom.Point2D;


public class ConcurrentTestProgram extends RobotProgram {

    Chassis chassis;
    Radar radar;

    @Override
    public void program() {
        robot.debug("init");

        chassis = robot.getEquipment(Chassis.class);
        radar = robot.getEquipment(Radar.class);

        if (chassis == null || radar == null) {
            robot.debug("fail");
            return;
        }

        double x = 100 * Math.random();
        double y = 100 * Math.random();

        robot.debug("move");
        robot.debug(new KPoint(x, y));
        driver.moveSmooth(new Point2D.Double(x, y), 10000);
        driver.stop();
        robot.debug(null);

        robot.debug("sleep");
        sleep(2000);
    }
}

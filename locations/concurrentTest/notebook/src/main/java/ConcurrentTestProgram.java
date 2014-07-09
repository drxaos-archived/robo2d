import com.robotech.military.api.Chassis;
import com.robotech.military.api.Program;
import com.robotech.military.api.Radar;
import com.robotech.military.api.Robot;
import straightedge.geom.KPoint;

import java.awt.geom.Point2D;

public class ConcurrentTestProgram implements Program {

    Chassis chassis;
    Radar radar;

    @Override
    public void run(Robot robot) {
        robot.debug("init");

        Driver driver = new Driver(robot);
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
        driver.sleep(2000);
    }
}

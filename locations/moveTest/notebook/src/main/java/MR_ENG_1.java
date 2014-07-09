import com.robotech.military.api.Chassis;
import com.robotech.military.api.Program;
import com.robotech.military.api.Radar;
import com.robotech.military.api.Robot;

import java.awt.geom.Point2D;


public class MR_ENG_1 implements Program {

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

        robot.debug("Move smooth to 15,40");
        robot.debug(new Point2D.Float(15, 40));
        driver.moveSmooth(new Point2D.Float(15, 40), 20000);
        driver.stop();
        robot.debug("Move to 25,30");
        robot.debug(new Point2D.Float(25, 30));
        driver.move(new Point2D.Float(25, 30), false, 20000);
        robot.debug("Move precise to 5,20");
        robot.debug(new Point2D.Float(5, 20));
        driver.move(new Point2D.Float(5, 20), true, 20000);
        robot.debug(null);

        robot.debug("Rotate to 0");
        driver.rotate(0, false, 10000);
        driver.sleep(1000);
        robot.debug("Forward 10");
        driver.forward(10, false, 10000);
        driver.sleep(1000);

        robot.debug("Test engines");

        chassis.setLeftAcceleration(100d);
        chassis.setRightAcceleration(100d);
        driver.sleep(1000);
        chassis.setLeftAcceleration(0d);
        chassis.setRightAcceleration(100d);
        driver.sleep(1000);
        chassis.setLeftAcceleration(100d);
        chassis.setRightAcceleration(0d);
        driver.sleep(1000);
        chassis.setLeftAcceleration(50d);
        chassis.setRightAcceleration(-100d);
        driver.sleep(1000);
        chassis.setLeftAcceleration(-100d);
        chassis.setRightAcceleration(-50d);
        driver.sleep(1000);
        chassis.setLeftAcceleration(-100d);
        chassis.setRightAcceleration(100d);
        driver.sleep(1000);
        chassis.setLeftAcceleration(0d);
        chassis.setRightAcceleration(0d);
        driver.sleep(1000);
    }

}

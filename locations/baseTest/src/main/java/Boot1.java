import com.robotech.military.api.*;

public class Boot1 implements Program {

    Robot robot;
    Chassis chassis;
    Radar radar;

    @Override
    public void run(Robot robot) {
        this.robot = robot;
        chassis = robot.getChassis();
        radar = robot.getRadar();

        if (chassis == null || radar == null) {
            robot.debug("fail");
            return;
        }

        //while (true) {
            Driver1 driver = new Driver1(robot);
            //driver.sleep(30000);
            driver.moveSmooth(new Point(20, 20), 50000);
            driver.stop();
            robot.waitForStep();
        //}
    }
}

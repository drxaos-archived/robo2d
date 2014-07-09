package robo2d.testbed.tests.base.notebook;

import com.robotech.military.api.Chassis;
import com.robotech.military.api.Radar;
import com.robotech.military.os.RobotProgram;


public class MR_BS_1 extends RobotProgram {

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

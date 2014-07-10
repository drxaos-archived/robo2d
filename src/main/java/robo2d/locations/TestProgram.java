package robo2d.locations;

import com.robotech.military.api.Chassis;
import com.robotech.military.api.Program;
import com.robotech.military.api.Radar;
import com.robotech.military.api.Robot;

public class TestProgram implements Program {

    Robot robot;
    Chassis chassis;
    Radar radar;

    @Override
    public void init(Robot robot) {
        this.robot = robot;
        chassis = robot.getEquipment(Chassis.class);
        radar = robot.getEquipment(Radar.class);
    }

    @Override
    public void run() {
        if (chassis == null || radar == null) {
            return;
        }

        while (true) {
            chassis.setLeftAcceleration(30d + Math.random() * 30);
            chassis.setRightAcceleration(70d + Math.random() * 30);
        }
    }
}

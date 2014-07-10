import com.robotech.military.api.Chassis;
import com.robotech.military.api.Program;
import com.robotech.military.api.Radar;
import com.robotech.military.api.Robot;

public class MR_BS_1 implements Program {

    Chassis chassis;
    Radar radar;

    @Override
    public void run(Robot robot) {
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

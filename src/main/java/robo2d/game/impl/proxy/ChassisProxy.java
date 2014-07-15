package robo2d.game.impl.proxy;

import com.robotech.military.api.Chassis;

public class ChassisProxy implements Chassis {
    Chassis chassis;

    public ChassisProxy(Chassis chassis) {
        this.chassis = chassis;
    }

    public void setLeftAcceleration(Double aDouble) {
        chassis.setLeftAcceleration(aDouble);
    }

    public void setRightAcceleration(Double aDouble) {
        chassis.setRightAcceleration(aDouble);
    }

    public Boolean isWorking() {
        return chassis.isWorking();
    }

    public Double getRightSpeed() {
        return chassis.getRightSpeed();
    }

    public Double getLeftSpeed() {
        return chassis.getLeftSpeed();
    }
}

package robo2d.game.impl.proxy;

public class Chassis implements com.robotech.military.api.Chassis {
    com.robotech.military.api.Chassis chassis;

    public Chassis(com.robotech.military.api.Chassis chassis) {
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

package robo2d.game.impl.proxy;

public class Robot implements com.robotech.military.api.Robot {
    com.robotech.military.api.Robot robot;

    public Robot(com.robotech.military.api.Robot robot) {
        this.robot = robot;
    }

    @Override
    public com.robotech.military.api.Chassis getChassis() {
        return new Chassis(robot.getChassis());
    }

    @Override
    public com.robotech.military.api.Computer getComputer() {
        return new Computer(robot.getComputer());
    }

    @Override
    public com.robotech.military.api.Radar getRadar() {
        return new Radar(robot.getRadar());
    }

    @Override
    public com.robotech.military.api.Radio getRadio() {
        return new Radio(robot.getRadio());
    }

    @Override
    public com.robotech.military.api.Turret getTurret() {
        return new Turret(robot.getTurret());
    }

    @Override
    public com.robotech.military.api.Extention[] getExtentions() {
        com.robotech.military.api.Extention[] extentions = robot.getExtentions();
        Extention[] wrapped = new Extention[extentions.length];
        for (int i = 0; i < extentions.length; i++) {
            wrapped[i] = new Extention(extentions[i]);
        }
        return wrapped;
    }

    @Override
    public Double getEnergy() {
        return robot.getEnergy();
    }

    @Override
    public String getUid() {
        return robot.getUid();
    }

    @Override
    public Long getTime() {
        return robot.getTime();
    }

    @Override
    public void waitForStep() {
        robot.waitForStep();
    }

    @Override
    public void debug(Object o) {
        robot.debug(o);
    }
}

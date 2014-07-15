package robo2d.game.impl.proxy;

import com.robotech.military.api.*;

public class RobotProxy implements Robot {
    Robot robot;

    public RobotProxy(Robot robot) {
        this.robot = robot;
    }

    @Override
    public Chassis getChassis() {
        return new ChassisProxy(robot.getChassis());
    }

    @Override
    public Computer getComputer() {
        return robot.getComputer();
    }

    @Override
    public Radar getRadar() {
        return robot.getRadar();
    }

    @Override
    public Radio getRadio() {
        return robot.getRadio();
    }

    @Override
    public Turret getTurret() {
        return robot.getTurret();
    }

    @Override
    public Extention[] getExtentions() {
        return robot.getExtentions();
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

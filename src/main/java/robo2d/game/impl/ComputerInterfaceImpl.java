package robo2d.game.impl;

import bluej.Main;
import com.robotech.military.api.*;
import net.sf.lipermi.exception.LipeRMIException;

public class ComputerInterfaceImpl implements ComputerInterface {

    private Robot robot;

    protected ComputerInterfaceImpl(com.robotech.military.api.Robot robot) {
        this.robot = robot;
    }

    @Override
    public void deploy() {
        Main.bluejListener.deploy();
    }

    @Override
    public void halt() {
        Main.bluejListener.halt();
    }

    @Override
    public Robot getRobotForDebug() {
        halt();
        try {
            Terminal.callHandler.exportObject(Robot.class, robot);
            Terminal.callHandler.exportObject(Chassis.class, robot.getChassis());
            Terminal.callHandler.exportObject(Computer.class, robot.getComputer());
            Terminal.callHandler.exportObject(Radar.class, robot.getRadar());
            Terminal.callHandler.exportObject(Radio.class, robot.getRadio());
            Terminal.callHandler.exportObject(Turret.class, robot.getTurret());
            for (Extention extention : robot.getExtentions()) {
                Terminal.callHandler.exportObject(Extention.class, extention);
            }
        } catch (LipeRMIException e) {
            e.printStackTrace();
        }
        return robot;
    }

}

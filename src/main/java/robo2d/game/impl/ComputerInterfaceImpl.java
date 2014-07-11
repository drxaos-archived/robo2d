package robo2d.game.impl;

import bluej.Main;
import robo2d.game.impl.proxy.RobotProxy;

import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;

public class ComputerInterfaceImpl implements ComputerInterface {

    private com.robotech.military.api.Robot robot;
    private RobotProxy robotProxy;

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
    public com.robotech.military.api.Robot getRobotForDebug() {
        halt();
        try {
            robotProxy = new RobotProxy(robot);
            Remote stub = UnicastRemoteObject.exportObject(robotProxy, 0);
            Terminal.registry.rebind(robotProxy.getClass().getCanonicalName() + "#" + System.identityHashCode(robotProxy), stub);
            return robotProxy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

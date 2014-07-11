package robo2d.game.impl.proxy;

import com.robotech.military.api.*;
import robo2d.game.impl.Terminal;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RobotProxy implements Robot, Remote {
    private Robot robot;

    private <T> T publish(T proxy) {
        try {
            Remote stub = UnicastRemoteObject.exportObject((Remote) proxy, 0);
            Terminal.registry.rebind(proxy.getClass().getCanonicalName() + "#" + System.identityHashCode(proxy), stub);
            return proxy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public RobotProxy(com.robotech.military.api.Robot robot) {
        this.robot = robot;
    }

    @Override
    public Chassis getChassis() throws RemoteException {
        return publish(new ChassisProxy(robot.getChassis()));
    }

    @Override
    public Computer getComputer() throws RemoteException {
        return publish(new ComputerProxy(robot.getComputer()));
    }

    @Override
    public Radar getRadar() throws RemoteException {
        return publish(new RadarProxy(robot.getRadar()));
    }

    @Override
    public Radio getRadio() throws RemoteException {
        return publish(new RadioProxy(robot.getRadio()));
    }

    @Override
    public Turret getTurret() throws RemoteException {
        return publish(new TurretProxy(robot.getTurret()));
    }

    @Override
    public Extention[] getExtentions() throws RemoteException {
        Extention[] extentions = robot.getExtentions();
        Extention[] proxies = new Extention[extentions.length];
        for (int i = 0; i < extentions.length; i++) {
            proxies[i] = publish(new ExtentionProxy(extentions[i]));
        }
        return extentions;
    }

    @Override
    public Double getEnergy() throws RemoteException {
        return robot.getEnergy();
    }

    @Override
    public String getUid() throws RemoteException {
        return robot.getUid();
    }

    @Override
    public Long getTime() throws RemoteException {
        return robot.getTime();
    }

    @Override
    public void waitForStep() throws RemoteException {
        robot.waitForStep();
    }

    @Override
    public void debug(Object o) throws RemoteException {
        robot.debug(o);
    }
}

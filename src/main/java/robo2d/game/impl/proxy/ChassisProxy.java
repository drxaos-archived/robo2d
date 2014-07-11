package robo2d.game.impl.proxy;

import com.robotech.military.api.Chassis;

import java.rmi.Remote;
import java.rmi.RemoteException;

public class ChassisProxy implements Chassis, Remote {
    private Chassis chassis;

    public ChassisProxy(Chassis chassis) {
        this.chassis = chassis;
    }

    @Override
    public void setLeftAcceleration(Double aDouble) throws RemoteException {
        chassis.setLeftAcceleration(aDouble);
    }

    @Override
    public void setRightAcceleration(Double aDouble) throws RemoteException {
        chassis.setRightAcceleration(aDouble);
    }

    @Override
    public Double getLeftSpeed() throws RemoteException {
        return chassis.getLeftSpeed();
    }

    @Override
    public Double getRightSpeed() throws RemoteException {
        return chassis.getRightSpeed();
    }

    @Override
    public Boolean isWorking() throws RemoteException {
        return chassis.isWorking();
    }
}

package robo2d.game.impl.proxy;

import com.robotech.military.api.Turret;

import java.rmi.Remote;
import java.rmi.RemoteException;

public class TurretProxy implements Turret, Remote {
    Turret turret;

    @Override
    public void shock() throws RemoteException {
        turret.shock();
    }

    @Override
    public void fire() throws RemoteException {
        turret.fire();
    }

    public TurretProxy(Turret turret) {
        this.turret = turret;
    }
}

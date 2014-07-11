package robo2d.game.impl.proxy;

import com.robotech.military.api.Robot;
import robo2d.game.impl.ComputerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ComputerInterfaceProxy {
    ComputerInterface computerInterface;

    public ComputerInterfaceProxy() throws Exception {
        Registry registry = LocateRegistry.getRegistry(null, 7099);
        computerInterface = (ComputerInterface) registry.lookup("ComputerInterface");
    }

    public void deploy() {
        try {
            computerInterface.deploy();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Robot getRobotForDebug() {
        try {
            return computerInterface.getRobotForDebug();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void halt() {
        try {
            computerInterface.halt();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

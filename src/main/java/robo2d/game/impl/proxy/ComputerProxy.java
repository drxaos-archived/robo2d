package robo2d.game.impl.proxy;

import com.robotech.military.api.Computer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public class ComputerProxy implements Computer, Remote {
    Computer computer;

    public ComputerProxy(Computer computer) {
        this.computer = computer;
    }

    public void saveFile(String s, String s2) throws RemoteException {
        computer.saveFile(s, s2);
    }

    public String loadFile(String s) throws RemoteException {
        return computer.loadFile(s);
    }
}

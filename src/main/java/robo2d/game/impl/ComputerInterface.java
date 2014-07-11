package robo2d.game.impl;

import com.robotech.military.api.Robot;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ComputerInterface extends Remote {
    void deploy() throws RemoteException;

    void halt() throws RemoteException;

    Robot getRobotForDebug() throws RemoteException;
}

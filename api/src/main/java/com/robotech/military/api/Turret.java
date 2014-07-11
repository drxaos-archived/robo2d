package com.robotech.military.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Turret extends Remote {

    void shock() throws RemoteException;

    void fire() throws RemoteException;
}

package com.robotech.military.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Chassis extends Remote {

    void setLeftAcceleration(Double percent) throws RemoteException;

    void setRightAcceleration(Double percent) throws RemoteException;

    Double getLeftSpeed() throws RemoteException;

    Double getRightSpeed() throws RemoteException;

    Boolean isWorking() throws RemoteException;
}

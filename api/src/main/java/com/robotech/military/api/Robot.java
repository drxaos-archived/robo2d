package com.robotech.military.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Robot extends Remote {

    Chassis getChassis() throws RemoteException;

    Computer getComputer() throws RemoteException;

    Radar getRadar() throws RemoteException;

    Radio getRadio() throws RemoteException;

    Turret getTurret() throws RemoteException;

    Extention[] getExtentions() throws RemoteException;

    Double getEnergy() throws RemoteException;

    String getUid() throws RemoteException;

    Long getTime() throws RemoteException;

    void waitForStep() throws RemoteException;

    void debug(Object dbg) throws RemoteException;
}

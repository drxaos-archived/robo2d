package com.robotech.military.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Radio extends Remote {

    void setChannel(Double frequency) throws RemoteException;

    void broadcast(Double data) throws RemoteException;

    Double listen() throws RemoteException;
}

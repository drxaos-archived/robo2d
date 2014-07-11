package com.robotech.military.api;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Extention extends Remote {
    Serializable invoke(String method, Serializable... args) throws RemoteException;
}

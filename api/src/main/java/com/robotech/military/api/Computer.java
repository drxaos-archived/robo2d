package com.robotech.military.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Computer extends Remote {

    void saveFile(String fileName, String content) throws RemoteException;

    String loadFile(String fileName) throws RemoteException;

}

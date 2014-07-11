package com.robotech.military.api;

import java.rmi.RemoteException;

public interface Program {
    void run(Robot robot) throws RemoteException;
}

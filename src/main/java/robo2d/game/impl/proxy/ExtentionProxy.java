package robo2d.game.impl.proxy;

import com.robotech.military.api.Extention;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class ExtentionProxy implements Extention, Remote {
    Extention extention;

    @Override
    public Serializable invoke(String s, Serializable... serializables) throws RemoteException {
        return extention.invoke(s, serializables);
    }

    public ExtentionProxy(Extention extention) {
        this.extention = extention;
    }
}

package robo2d.game.impl.proxy;

import com.robotech.military.api.Radio;

import java.rmi.Remote;
import java.rmi.RemoteException;

public class RadioProxy implements Radio, Remote {
    Radio radio;

    @Override
    public void setChannel(Double aDouble) throws RemoteException {
        radio.setChannel(aDouble);
    }

    @Override
    public void broadcast(Double aDouble) throws RemoteException {
        radio.broadcast(aDouble);
    }

    @Override
    public Double listen() throws RemoteException {
        return radio.listen();
    }

    public RadioProxy(Radio radio) throws RemoteException {
        this.radio = radio;
    }
}

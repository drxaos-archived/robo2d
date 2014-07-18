package robo2d.game.impl.proxy;

import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.Client;
import robo2d.game.impl.ComputerInterface;

import java.io.IOException;

public class Remote implements ComputerInterface {

    private Client client;
    private ComputerInterface computerInterface;

    public Remote() {
        CallHandler callHandler = new CallHandler();
        String remoteHost = "localhost";
        int portWasBinded = 4455;

        try {
            client = new Client(remoteHost, portWasBinded, callHandler);
            this.computerInterface = (ComputerInterface) client.getGlobal(ComputerInterface.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deploy() {
        computerInterface.deploy();
    }

    @Override
    public void halt() {
        computerInterface.halt();
    }

    @Override
    public com.robotech.military.api.Robot getRobotForDebug() {
        return new Robot(computerInterface.getRobotForDebug());
    }
}

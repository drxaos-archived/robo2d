package robo2d.game.impl;

import com.robotech.military.api.Program;
import com.robotech.military.internal.LocalConnection;

import java.io.Serializable;

public class ProgramWrapperHelper implements Runnable, Serializable {
    transient com.robotech.military.api.Host host;
    transient static ClassLoader classLoader;

    public ProgramWrapperHelper(String uid, ClassLoader classLoader) {
        this.host = new com.robotech.military.api.Host(new LocalConnection(uid));
        ProgramWrapperHelper.classLoader = classLoader;
    }

    @Override
    public void run() {
        try {
            Class<?> boot = Class.forName("Boot", true, classLoader);
            ((Program) boot.newInstance()).run(host);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

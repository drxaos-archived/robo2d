package com.robotech.military.internal;

import com.robotech.military.api.Program;
import com.robotech.military.api.Robot;

public class Launcher {

    public static void main(String[] args) {
        LocalConnection localConnection = new LocalConnection(args.length > 0 ? args[0] : "debug");
        Robot robot = new Robot(localConnection);
        try {
            Program boot = (Program) Class.forName("Boot").newInstance();
            boot.run(robot);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            localConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

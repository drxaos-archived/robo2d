package com.robotech.military.remote;

import com.robotech.military.api.Program;
import com.robotech.military.remote.gen.Robot;

public class Loader {

    public static void main(String args[]) throws Exception {
        Robot robot = new Robot(args[1]);
        Program program = (Program) Class.forName(args[0]).getConstructor(String.class).newInstance();
        program.run(robot);
    }

}

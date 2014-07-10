package com.robotech.military.remote;

import com.robotech.military.api.Program;
import com.robotech.military.remote.gen.Robot;

import java.lang.reflect.Constructor;

public class Loader {

    public static void main(String args[]) throws Exception {
        Robot robot = new Robot(args[1]);
        Constructor<?> constructor = Class.forName(args[0]).getConstructor();
        Program program = (Program) constructor.newInstance();
        program.run(robot);
    }

}

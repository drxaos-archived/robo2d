package robo2d.game.impl;

import robo2d.game.api.Robot;

abstract public class RobotProgram implements Runnable {
    protected Robot robot;

    @Override
    final public void run() {
        while (true) {
            program();
        }
    }

    abstract public void program();

    protected void sleep(int ms) {
        long end = robot.getTime() + ms;
        while (robot.getTime() < end) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
            }
        }
    }

    protected void cycle(int ms, Runnable func) {
        long end = robot.getTime() + ms;
        func.run();
        while (robot.getTime() < end) {
            robot.waitForStep();
            func.run();
        }
    }
}

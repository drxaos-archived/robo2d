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

    protected void cycle(Runnable func, int maxMs) {
        long end = robot.getTime() + maxMs;
        try {
            func.run();
            while (robot.getTime() < end) {
                robot.waitForStep();
                func.run();
            }
        } catch (Interrupt e) {
        }
    }

    public static double differenceAngle(double theta1, double theta2) {
        double dif = theta2 - theta1;
        while (dif < -Math.PI) dif += 2 * Math.PI;
        while (dif > Math.PI) dif -= 2 * Math.PI;
        if (dif < 0) dif *= -1;
        return dif;
    }

    public static class Interrupt extends RuntimeException {
        public Interrupt() {
        }
    }
}

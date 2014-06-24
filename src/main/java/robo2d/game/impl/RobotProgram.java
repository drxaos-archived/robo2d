package robo2d.game.impl;

import robo2d.game.api.Chassis;
import robo2d.game.api.Radar;
import robo2d.game.api.Robot;
import straightedge.geom.KPoint;

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

    protected void stop() {
        Chassis chassis = robot.getEquipment(Chassis.class);
        if (chassis != null) {
            chassis.setLeftAcceleration(0d);
            chassis.setRightAcceleration(0d);
        }
    }

    protected boolean rotate(double toAngle, boolean precise, int maxMs) {
        long end = robot.getTime() + maxMs;
        Chassis chassis = robot.getEquipment(Chassis.class);
        Radar radar = robot.getEquipment(Radar.class);
        if (chassis == null || radar == null) {
            return false;
        }
        stop();
        while (robot.getTime() < end) {
            double azimuth = differenceAngle(radar.getAngle(), toAngle);
            if (Math.abs(azimuth) < (precise ? 0.0001 : 0.001)) {
                stop();
                return true;
            }
            if (precise) {
                azimuth = Math.max(Math.abs(azimuth) - 0.03, 0.001) * Math.signum(azimuth);
            }
            int force = 500;
            chassis.setLeftAcceleration(-1 * force * azimuth);
            chassis.setRightAcceleration(1 * force * azimuth);
            robot.waitForStep();
        }
        stop();
        return false;
    }

    protected boolean forward(double distance, boolean precise, int maxMs) {
        long end = robot.getTime() + maxMs;
        Chassis chassis = robot.getEquipment(Chassis.class);
        Radar radar = robot.getEquipment(Radar.class);
        if (chassis == null || radar == null) {
            return false;
        }
        stop();
        KPoint start = radar.getPosition();
        while (robot.getTime() < end) {
            KPoint current = radar.getPosition();
            double remains = distance - distance(start, current);
            if (Math.abs(remains) < (precise ? 0.001 : 0.01)) {
                stop();
                return true;
            }
            if (precise) {
                remains = Math.max(Math.abs(remains) - 1, 0.01) * Math.signum(remains);
            }
            int force = 100;
            chassis.setLeftAcceleration(1 * force * remains);
            chassis.setRightAcceleration(1 * force * remains);
            robot.waitForStep();
        }
        stop();
        return false;
    }

    protected boolean move(KPoint to, boolean precise, int maxMs) {
        long end = robot.getTime() + maxMs;
        Chassis chassis = robot.getEquipment(Chassis.class);
        Radar radar = robot.getEquipment(Radar.class);
        if (chassis == null || radar == null) {
            return false;
        }
        stop();
        double longAng = (precise ? 0.01 : 0.1);
        double shortDist = (precise ? 1.5 : 1.0);
        double shortAng = (precise ? 0.005 : 0.05);
        double enoughDist = (precise ? 0.1 : 0.8);
        while (robot.getTime() < end) {
            KPoint current = radar.getPosition();
            double angleToTarget = angle(current, to);
            double azimuth = differenceAngle(radar.getAngle(), angleToTarget);
            double distance = distance(to, current);
            if (Math.abs(distance) >= shortDist && Math.abs(azimuth) > longAng) {
                rotate(angleToTarget, precise, (int) (end - robot.getTime()));
            } else if (Math.abs(distance) < shortDist && Math.abs(distance) > enoughDist &&
                    (Math.abs(azimuth) > shortAng && Math.abs(azimuth) < Math.PI - shortAng)) {
                if (Math.abs(azimuth) < Math.PI / 2) {
                    rotate(angleToTarget, precise, (int) (end - robot.getTime()));
                } else {
                    rotate(angleToTarget + Math.PI, precise, (int) (end - robot.getTime()));
                }
            } else if (Math.abs(distance) < enoughDist) {
                stop();
                return true;
            } else {
                int force = 80;
                if (precise) {
                    distance = Math.max(Math.abs(distance) - 0.2, 0.01) * Math.signum(distance);
                }
                chassis.setLeftAcceleration(Math.min(1 * force * distance, 100));
                chassis.setRightAcceleration(Math.min(1 * force * distance, 100));
                robot.waitForStep();
            }
        }
        stop();
        return false;
    }


    public static double differenceAngle(double theta1, double theta2) {
        double dif = theta2 - theta1;
        while (dif < -Math.PI) dif += 2 * Math.PI;
        while (dif > Math.PI) dif -= 2 * Math.PI;
        return dif;
    }

    public static double angle(KPoint from, KPoint to) {
        return to.findAngle(from);
    }

    public static double distance(KPoint from, KPoint to) {
        return from.distance(to);
    }

    public static class Interrupt extends RuntimeException {
        public Interrupt() {
        }
    }
}

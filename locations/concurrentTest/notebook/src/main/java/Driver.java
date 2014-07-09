import com.robotech.military.api.Chassis;
import com.robotech.military.api.Radar;
import com.robotech.military.api.Robot;

import java.awt.geom.Point2D;

public class Driver {
    protected Robot robot;

    public Driver(Robot robot) {
        this.robot = robot;
    }

    protected void waitForChanges() {
        robot.waitForStep();
    }

    protected void sleep(int ms) {
        long end = robot.getTime() + ms;
        while (robot.getTime() < end) {
            waitForChanges();
        }
    }

    public void stop() {
        Chassis chassis = robot.getEquipment(Chassis.class);
        if (chassis != null) {
            chassis.setLeftAcceleration(0d);
            chassis.setRightAcceleration(0d);
        }
    }

    public boolean rotate(double toAngle, boolean precise, int maxMs) {
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
            waitForChanges();
        }
        stop();
        return false;
    }

    public boolean forward(double distance, boolean precise, int maxMs) {
        long end = robot.getTime() + maxMs;
        Chassis chassis = robot.getEquipment(Chassis.class);
        Radar radar = robot.getEquipment(Radar.class);
        if (chassis == null || radar == null) {
            return false;
        }
        stop();
        Point2D start = radar.getPosition();
        while (robot.getTime() < end) {
            Point2D current = radar.getPosition();
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
            waitForChanges();
        }
        stop();
        return false;
    }

    public boolean move(Point2D to, boolean precise, int maxMs) {
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
            Point2D current = radar.getPosition();
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
                waitForChanges();
            }
        }
        stop();
        return false;
    }


    public boolean moveSmooth(Point2D to, int maxMs) {
        long end = robot.getTime() + maxMs;
        Chassis chassis = robot.getEquipment(Chassis.class);
        Radar radar = robot.getEquipment(Radar.class);
        if (chassis == null || radar == null) {
            return false;
        }
        while (robot.getTime() < end) {
            int force = 80;
            int rotateForce = 120;
            double width = 2;
            Point2D current = radar.getPosition();
            double myAngle = radar.getAngle();
            double angleToTarget = angle(current, to);
            double azimuth = differenceAngle(myAngle, angleToTarget);
            double direction = (Math.abs(azimuth) > Math.PI / 2) ? -1 : 1;
            double distance = distance(to, current);
            if (distance < 1 && Math.abs(azimuth) > 0.2 && Math.abs(azimuth) < Math.PI - 0.2) {
                chassis.setLeftAcceleration(-direction * rotateForce * azimuth);
                chassis.setRightAcceleration(direction * rotateForce * azimuth);
            } else if (distance < 0.2) {
                return true;
            } else {
                double distanceLeft = distance(to, new Point2D.Double(current.getX() + Math.cos(myAngle + Math.PI / 2) * width, current.getY() + Math.sin(myAngle + Math.PI / 2) * width));
                double distanceRight = distance(to, new Point2D.Double(current.getX() + Math.cos(myAngle - Math.PI / 2) * width, current.getY() + Math.sin(myAngle - Math.PI / 2) * width));
                double distDiff = distanceLeft - distanceRight;
                chassis.setLeftAcceleration(direction * (Math.min(1 * force * distance, 100) - rotateForce * distDiff));
                chassis.setRightAcceleration(direction * (Math.min(1 * force * distance, 100) + rotateForce * distDiff));
            }
            waitForChanges();
        }
        return false;
    }

    public static double differenceAngle(double theta1, double theta2) {
        double dif = theta2 - theta1;
        while (dif < -Math.PI) dif += 2 * Math.PI;
        while (dif > Math.PI) dif -= 2 * Math.PI;
        return dif;
    }

    public static double angle(Point2D from, Point2D to) {
        double dx = from.getX() - to.getX();
        double dy = from.getY() - to.getY();
        return Math.atan2(dy, dx);
    }

    public static double distance(Point2D from, Point2D to) {
        return from.distance(to);
    }

    public static class Interrupt extends RuntimeException {
        public Interrupt() {
        }
    }
}


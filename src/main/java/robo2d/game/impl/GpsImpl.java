package robo2d.game.impl;

import com.robotech.military.api.Point;
import robo2d.game.Game;

import java.awt.geom.Point2D;

public class GpsImpl implements EquipmentImpl {

    Game game;
    RobotImpl robot;
    Double scanDistance = null;

    public GpsImpl(Game game, Double scanDistance) {
        this.game = game;
        this.scanDistance = scanDistance;
    }

    @Override
    public void setup(RobotImpl robot) {
        this.robot = robot;
    }

    @Override
    public LocatorScanData locate(double angle) {
        if (scanDistance == null) {
            return null;
        }
        if (!robot.consumeEnergy(0.001)) {
            return null;
        }
        synchronized (game.stepSync()) {
            return game.resolveDirection(angle, scanDistance, robot);
        }
    }

    @Override
    public Double getAngle() {
        if (!game.hasGps()) {
            return null;
        }
        return robot.box.getAngle();
    }

    @Override
    public Point getPosition() {
        if (!game.hasGps()) {
            return null;
        }
        Point2D position = robot.box.getPosition();
        return new Point((float) position.getX(), (float) position.getY());
    }

    @Override
    public RobotImpl getRobot() {
        return robot;
    }

    @Override
    public void setSatResponse(SatelliteScanData response) {
        satelliteScanData = response;
    }
}

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

    public Double getAngle() {
        if (!game.hasGps()) {
            return null;
        }
        return robot.box.getAngle();
    }

    public Point getPosition() {
        if (!game.hasGps()) {
            return null;
        }
        Point2D position = robot.box.getPosition();
        return new Point((float) position.getX(), (float) position.getY());
    }

    public RobotImpl getRobot() {
        return robot;
    }
}

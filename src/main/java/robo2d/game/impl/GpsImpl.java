package robo2d.game.impl;

import com.robotech.military.api.Point;
import robo2d.game.Game;

import java.awt.geom.Point2D;

public class GpsImpl implements EquipmentImpl {

    Game game;
    RobotImpl robot;

    public GpsImpl(Game game) {
        this.game = game;
    }

    @Override
    public void setup(RobotImpl robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
        robot.getComputer().setStateString("gps/ready", "true");
    }

    @Override
    public void update() {
        Point position = getPosition();
        Double angle = getAngle();
        robot.getComputer().setStateString("gps/posx", "" + position.getX());
        robot.getComputer().setStateString("gps/posy", "" + position.getY());
        robot.getComputer().setStateString("gps/angle", "" + angle);
        robot.getComputer().setStateString("gps/time", "" + System.currentTimeMillis());
    }

    public Double getAngle() {
        return robot.box.getAngle();
    }

    public Point getPosition() {
        Point2D position = robot.box.getPosition();
        return new Point((float) position.getX(), (float) position.getY());
    }

    public RobotImpl getRobot() {
        return robot;
    }
}

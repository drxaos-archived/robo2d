package robo2d.game.impl;

import com.robotech.military.api.Point;
import com.robotech.game.Game;

public class GpsImpl implements EquipmentImpl {

    Game game;
    Host robot;

    public GpsImpl(Game game) {
        this.game = game;
    }

    @Override
    public void setup(Host robot) {
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
        return robot.getAngle();
    }

    public Point getPosition() {
        Point position = robot.getPosition();
        return position;
    }
}

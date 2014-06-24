package robo2d.game.impl;

import robo2d.game.Game;
import robo2d.game.api.Radar;
import straightedge.geom.KPoint;

public class RadarImpl implements Radar, EquipmentImpl {

    Game game;
    RobotImpl robot;

    public RadarImpl(Game game) {
        this.game = game;
    }

    @Override
    public void setup(RobotImpl robot) {
        this.robot = robot;
    }

    @Override
    public robo2d.game.api.map.Map fullScan() {
        return null;
    }

    @Override
    public Double getAngle() {
        return robot.box.getAngle();
    }

    @Override
    public KPoint getPosition() {
        return robot.box.getPosition();
    }
}

package robo2d.game.impl;

import robo2d.game.Game;

public class RadarImpl implements EquipmentImpl {

    Game game;
    RobotImpl robot;
    Double scanDistance = null;

    public RadarImpl(Game game, Double scanDistance) {
        this.game = game;
        this.scanDistance = scanDistance;
    }

    @Override
    public void setup(RobotImpl robot) {
        this.robot = robot;
    }

//    @Override
//    public LocatorScanData locate(double angle) {
//        if (scanDistance == null) {
//            return null;
//        }
//        if (!robot.consumeEnergy(0.001)) {
//            return null;
//        }
//        synchronized (game.stepSync()) {
//            return game.resolveDirection(angle, scanDistance, robot);
//        }
//    }

    public RobotImpl getRobot() {
        return robot;
    }
}

package robo2d.game.impl;

import com.robotech.game.Game;

public class RadarImpl implements EquipmentImpl {

    Game game;
    Host robot;
    Double scanDistance = null;

    public RadarImpl(Game game, Double scanDistance) {
        this.game = game;
        this.scanDistance = scanDistance;
    }

    @Override
    public void setup(Host robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
        robot.getComputer().setStateString("radar/ready", "true");
    }

    @Override
    public void update() {

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
}

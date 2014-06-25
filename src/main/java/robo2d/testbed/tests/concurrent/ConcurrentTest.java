package robo2d.testbed.tests.concurrent;

import robo2d.game.Game;
import robo2d.game.impl.*;
import robo2d.testbed.RobotTest;
import robo2d.testbed.tests.move.EngineTestProgram;
import straightedge.geom.KPoint;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ConcurrentTest extends RobotTest {

    @Override
    public Game createGame() {
        Game game = new Game(getWorld(), getDebugDraw());

        PlayerImpl player1 = new PlayerImpl("player1");

        for (int i = 0; i < 10; i++) {
            RobotImpl robot = new RobotImpl(player1, new KPoint(100*Math.random(), 100*Math.random()), Math.PI * 4 * Math.random());
            ChassisImpl chassis = new ChassisImpl(300d);
            RadarImpl radar = new RadarImpl(game);
            ComputerImpl computer = new ComputerImpl(ConcurrentTestProgram.class);
            robot.addEquipment(chassis);
            robot.addEquipment(radar);
            robot.addEquipment(computer);
            robot.charge(4000);
            game.addRobot(robot);
        }

        return game;
    }

    public static void main(String[] args) {
        new ConcurrentTest().start();
    }
}

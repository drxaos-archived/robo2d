package robo2d.testbed.tests.base;

import com.jme3.math.FastMath;
import robo2d.game.Game;
import robo2d.game.impl.*;
import robo2d.testbed.RobotTest;
import straightedge.geom.KPoint;

public class BaseTest extends RobotTest {

    @Override
    public Game createGame() {
        Game game = new Game(getWorld(), getDebugDraw());

        BaseImpl base = new BaseImpl(new KPoint(5, 5), FastMath.PI / 4);
        game.addBase(base);

        PlayerImpl player1 = new PlayerImpl("player1", new KPoint(15, 15), FastMath.PI / 4 * 5);
        player1.setNotebookDir("src/main/java/robo2d/testbed/tests/base/notebook");
        game.addPlayer(player1);

        RobotImpl robot = new RobotImpl("MR_BS_1", game, player1, new KPoint(15, 5), 0);
        ChassisImpl chassis = new ChassisImpl(300d);
        RadarImpl radar = new RadarImpl(game, 100d);
        ComputerImpl computer = new ComputerImpl(true);
        robot.addEquipment(chassis);
        robot.addEquipment(radar);
        robot.addEquipment(computer);
        robot.charge(4000);
        game.addRobot(robot);
        game.addGps();
        game.addSatellite(20, 2000);

        return game;
    }

    public static void main(String[] args) {
        new BaseTest().start();
    }
}

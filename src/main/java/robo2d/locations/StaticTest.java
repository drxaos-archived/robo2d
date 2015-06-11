package robo2d.locations;

import com.robotech.game.Game;
import robo2d.game.impl.PlayerImpl;
import robo2d.game.impl.RobotImpl;
import robo2d.testbed.RobotTest;
import straightedge.geom.KPoint;

public class StaticTest extends RobotTest {

    @Override
    public Game createGame() {
        Game game = new Game(getWorld(), getDebugDraw());

        PlayerImpl player1 = new PlayerImpl("player1", new KPoint(0, -2), 0);
        game.addPlayer(player1);

        RobotImpl robot = new RobotImpl("AGR-ENG-1", game, player1, new KPoint(0, 0), 0);
        game.addRobot(robot);

        return game;
    }

    public static void main(String[] args) {
        new StaticTest().start();
    }
}

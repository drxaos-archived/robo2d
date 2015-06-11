package robo2d.locations;

import com.robotech.game.Game;
import robo2d.game.impl.*;
import robo2d.game.impl.statics.WallImpl;
import robo2d.testbed.RobotTest;
import straightedge.geom.KPoint;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MoveTest extends RobotTest {

    @Override
    public Game createGame() {
        Game game = new Game(getWorld(), getDebugDraw());

        ArrayList<Point2D> points = new ArrayList<Point2D>();
        points.add(new Point2D.Double(7d, -7d));
        points.add(new Point2D.Double(15d, -15d));
        points.add(new Point2D.Double(-10d, -15d));
        points.add(new Point2D.Double(-10d, -10d));
        points.add(new Point2D.Double(0, 10));
        points.add(new Point2D.Double(5, 5));
        points.add(new Point2D.Double(17, 13));
        points.add(new Point2D.Double(10, 0));

        game.addWall(new WallImpl(points));

        PlayerImpl player1 = new PlayerImpl("player1", new KPoint(-15, 15), 0);
        game.addPlayer(player1);

        RobotImpl robot = new RobotImpl("MR_ENG_1", game, player1, new KPoint(15, 15), Math.PI * 4 * Math.random());
        ChassisImpl chassis = new ChassisImpl(300d);
        RadarImpl radar = new RadarImpl(game, 100d);
        ComputerImpl computer = new ComputerImpl(ComputerImpl.State.ON);
        robot.addEquipment(chassis);
        robot.addEquipment(radar);
        robot.addEquipment(computer);
        robot.charge(400);
        game.addRobot(robot);

        return game;
    }

    public static void main(String[] args) {
        new MoveTest().start();
    }
}

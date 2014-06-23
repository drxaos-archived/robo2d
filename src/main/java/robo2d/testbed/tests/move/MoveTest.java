package robo2d.testbed.tests.move;

import robo2d.game.Game;
import robo2d.game.impl.ChassisImpl;
import robo2d.game.impl.PlayerImpl;
import robo2d.game.impl.RobotImpl;
import robo2d.game.impl.WallImpl;
import robo2d.testbed.RobotTest;
import straightedge.geom.KPoint;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MoveTest extends RobotTest {
    @Override
    public void createWorld() {
        Game game = new Game();
        game.worldBox = getWorld();

        ArrayList<Point2D> points = new ArrayList<Point2D>();
        points.add(new Point2D.Double(0, 0));
        points.add(new Point2D.Double(0, 10));
        points.add(new Point2D.Double(5, 5));
        points.add(new Point2D.Double(10, 10));
        points.add(new Point2D.Double(10, 0));

        game.physicals.add(new WallImpl(points, Math.PI * 0.01));


        PlayerImpl player1 = new PlayerImpl("player1");
        ChassisImpl chassis = new ChassisImpl(1d);

        game.physicals.add(new RobotImpl(player1, chassis, null, 500d, new KPoint(15,15), 1));

        game.start();
    }

    public static void main(String[] args) {
        new MoveTest().start();
    }
}

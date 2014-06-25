package robo2d.game;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import robo2d.game.box2d.Physical;
import robo2d.game.box2d.RobotBox;
import robo2d.game.impl.ComputerImpl;
import robo2d.game.impl.PlayerImpl;
import robo2d.game.impl.RobotImpl;
import robo2d.game.impl.WallImpl;
import straightedge.geom.KPoint;

import java.util.ArrayList;
import java.util.List;

public class Game {
    protected List<PlayerImpl> players = new ArrayList<PlayerImpl>();
    protected List<Physical> physicals = new ArrayList<Physical>();
    protected List<RobotImpl> robots = new ArrayList<RobotImpl>();
    protected World worldBox;
    protected DebugDraw debugDraw;
    public static final Color3f GRAY = new Color3f(0.3f, 0.3f, 0.3f);

    public Game(World worldBox, DebugDraw debugDraw) {
        this.worldBox = worldBox;
        this.debugDraw = debugDraw;
    }

    public void addRobot(RobotImpl robot) {
        physicals.add(robot);
        robots.add(robot);
    }

    public void addWall(WallImpl wall) {
        physicals.add(wall);
    }

    public void start() {
        for (Physical physical : physicals) {
            Body body = worldBox.createBody(physical.getBox().bodyDef);
            for (FixtureDef fixtureDef : physical.getBox().fixtureDefs) {
                body.createFixture(fixtureDef);
            }
            physical.getBox().body = body;
        }

        for (RobotImpl robot : robots) {
            ComputerImpl computer = robot.getComputer();
            if (computer != null) {
                computer.startProgram();
            }
        }
    }

    public void managePrograms() {
        for (RobotImpl robot : robots) {
            ComputerImpl computer = robot.getComputer();
            if (computer != null) {
                if (robot.getEnergy() < 0.01) {
                    computer.stopProgram();
                } else {
                    computer.startProgram();
                }
            }
        }
    }

    public void applyEffects() {
        for (RobotImpl robot : robots) {
            robot.applyEffects();
        }
    }

    public void sync() {
        for (RobotImpl robot : robots) {
            robot.sync();
        }
    }

    public void debug() {
        Vec2 res = new Vec2();
        for (RobotImpl robot : robots) {
            String msg = robot.getDebug();
            if (msg != null && !msg.isEmpty()) {
                debugDraw.getWorldToScreenToOut(robot.getBox().getPositionVec2().add(new Vec2(1.5f * (float) RobotBox.SIZE, 1.5f * (float) RobotBox.SIZE)), res);
                debugDraw.drawString(res, msg, Color3f.RED);
            }
            debugDraw.getWorldToScreenToOut(robot.getBox().getPositionVec2().add(new Vec2(1.5f * (float) RobotBox.SIZE, -1.5f * (float) RobotBox.SIZE)), res);
            debugDraw.drawString(res, String.format("E:%.2f", robot.getEnergy()) + ", " + (robot.getComputer().isRunning() ? "ON" : "OFF"), Color3f.BLUE);
            KPoint point = robot.getDebugPoint();
            if (point != null) {
                debugDraw.drawPoint(new Vec2((float) point.getX(), (float) point.getY()), 3, Color3f.WHITE);
                debugDraw.drawSegment(new Vec2((float) point.getX(), (float) point.getY()), robot.getBox().getPositionVec2(), GRAY);
            }
        }
    }
}

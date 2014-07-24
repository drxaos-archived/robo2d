package robo2d.game;

import com.robotech.military.api.Radar;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import robo2d.game.box2d.Physical;
import robo2d.game.box2d.RobotBox;
import robo2d.game.impl.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Game {
    protected List<Physical> physicals = new ArrayList<Physical>();
    protected List<RobotImpl> robots = new ArrayList<RobotImpl>();
    protected List<BaseImpl> bases = new ArrayList<BaseImpl>();
    protected PlayerImpl player;

    Object stepSync = new Object();

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

    public void addPlayer(PlayerImpl player) {
        physicals.add(player);
        this.player = player;
    }

    public void addBase(BaseImpl base) {
        physicals.add(base);
        this.bases.add(base);
    }

    public PlayerImpl getPlayer() {
        return player;
    }

    public List<RobotImpl> getRobots() {
        return robots;
    }

    public List<Physical> getPhysicals() {
        return physicals;
    }

    public Object stepSync() {
        return stepSync;
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

        // Start programs

        Terminal.bindInterface();
        for (RobotImpl robot : robots) {
            robot.init();
            robot.update();
            ComputerImpl computer = robot.getComputer();
            if (computer != null && computer.bootOnStartup()) {
                computer.startProgram();
            }
        }
    }

    public void stop() {
        for (RobotImpl robot : robots) {
            ComputerImpl computer = robot.getComputer();
            if (computer != null) {
                computer.stopProgram();
            }
        }
    }

    public Long getTime() {
        return System.currentTimeMillis();
    }

    public void beforeStep() {
        for (RobotImpl robot : robots) {
            robot.update();
        }
        for (BaseImpl base : bases) {
            base.update();
        }
        managePrograms();
        applyEffects();
        sync();
    }

    public void afterStep() {
        debug();
    }

    public void managePrograms() {
        for (RobotImpl robot : robots) {
            ComputerImpl computer = robot.getComputer();
            if (computer != null) {
                if (robot.getEnergy() < 0.01) {
                    computer.stopProgram();
                }
            }
        }
    }

    public void applyEffects() {
        for (RobotImpl robot : robots) {
            robot.applyEffects();
        }
    }

    private String recognizeType(Physical physical, RobotImpl forRobot) {
        if (physical instanceof WallImpl) {
            return Radar.WALL;
        } else if (physical instanceof RobotImpl) {
            if (physical == forRobot) {
                return Radar.MATE;
            } else if (((RobotImpl) physical).getOwner() == forRobot.getOwner()) {
                return Radar.MATE;
            } else {
                return Radar.ENEMY;
            }
        } else {
            return Radar.UNKNOWN;
        }
    }

    public String resolvePoint(double x, double y, RobotImpl forRobot) {
        for (Physical physical : physicals) {
            if (physical.getBox().hasPoint(new Vec2((float) x, (float) y))) {
                return recognizeType(physical, forRobot);
            }
        }
        return Radar.EMPTY;
    }

    public List<BaseImpl> getBases() {
        return bases;
    }

    private static class RayCastClosestCallback implements RayCastCallback {
        Body fromBody;
        Vec2 m_point;
        Body body;

        private RayCastClosestCallback(Body fromBody) {
            this.fromBody = fromBody;
        }

        public void init() {
            body = null;
            m_point = null;
        }

        public float reportFixture(Fixture fixture, Vec2 point,
                                   Vec2 normal, float fraction) {
            body = fixture.getBody();
            m_point = point;
            return fraction;
        }

        public Body getBody() {
            return body;
        }

        public Vec2 getPoint() {
            return m_point;
        }
    }

//    public Radar.LocatorScanData resolveDirection(double angle, double scanDistance, RobotImpl forRobot) {
//        RayCastClosestCallback callback = new RayCastClosestCallback(forRobot.getBox().body);
//        worldBox.raycast(callback,
//                forRobot.getBox().getPositionVec2(),
//                forRobot.getBox().getPositionVec2().add(
//                        new Vec2((float) (Math.cos(angle) * scanDistance),
//                                (float) (Math.sin(angle) * scanDistance))
//                ));
//        Body body = callback.getBody();
//        Vec2 point = callback.getPoint();
//        Physical physical = null;
//        for (Physical p : physicals) {
//            if (p.getBox().body == body) {
//                physical = p;
//                break;
//            }
//        }
//        if (physical == null || point == null) {
//            return new Radar.LocatorScanData(Radar.Type.EMPTY, scanDistance, angle);
//        }
//        Point2D forRobotPosition = forRobot.getBox().getPosition();
//        return new Radar.LocatorScanData(
//                recognizeType(physical, forRobot),
//                KPoint.distance(
//                        new KPoint(point.x, point.y),
//                        new KPoint(forRobotPosition.getX(), forRobotPosition.getY())
//                ),
//                angle
//        );
//    }

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
            debugDraw.drawString(res, String.format("E:%.2f", robot.getEnergy()) + ", " + (robot.getComputer() != null && robot.getComputer().isRunning() ? "ON" : "OFF"), Color3f.BLUE);
            Point2D point = robot.getDebugPoint();
            if (point != null) {
                debugDraw.drawPoint(new Vec2((float) point.getX(), (float) point.getY()), 3, Color3f.WHITE);
                debugDraw.drawSegment(new Vec2((float) point.getX(), (float) point.getY()), robot.getBox().getPositionVec2(), GRAY);
            }
        }
    }
}

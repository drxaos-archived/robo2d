package robo2d.game;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import robo2d.game.api.Radar;
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

    private Radar.Type recognizeType(Physical physical, RobotImpl forRobot) {
        if (physical instanceof WallImpl) {
            return Radar.Type.WALL;
        } else if (physical instanceof RobotImpl) {
            if (physical == forRobot) {
                return Radar.Type.ME;
            } else if (((RobotImpl) physical).getOwner() == forRobot.getOwner()) {
                return Radar.Type.MATE_BOT;
            } else {
                return Radar.Type.ENEMY_BOT;
            }
        } else {
            return Radar.Type.UNKNOWN;
        }
    }

    public Radar.Type resolvePoint(double x, double y, RobotImpl forRobot) {
        for (Physical physical : physicals) {
            if (physical.getBox().hasPoint(new Vec2((float) x, (float) y))) {
                return recognizeType(physical, forRobot);
            }
        }
        return Radar.Type.EMPTY;
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

    public Radar.ScanData resolveDirection(double angle, double scanDistance, RobotImpl forRobot) {
        RayCastClosestCallback callback = new RayCastClosestCallback(forRobot.getBox().body);
        worldBox.raycast(callback,
                forRobot.getBox().getPositionVec2(),
                forRobot.getBox().getPositionVec2().add(
                        new Vec2((float) (Math.cos(angle) * scanDistance),
                                (float) (Math.sin(angle) * scanDistance))
                ));
        Body body = callback.getBody();
        Vec2 point = callback.getPoint();
        Physical physical = null;
        for (Physical p : physicals) {
            if (p.getBox().body == body) {
                physical = p;
                break;
            }
        }
        if (physical == null || point == null) {
            return new Radar.ScanData(Radar.Type.EMPTY, 0, angle);
        }
        return new Radar.ScanData(
                recognizeType(physical, forRobot),
                KPoint.distance(
                        new KPoint(point.x, point.y),
                        forRobot.getBox().getPosition()
                ),
                angle
        );
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

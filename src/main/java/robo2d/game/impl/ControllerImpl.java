package robo2d.game.impl;

import com.jme3.math.FastMath;
import com.robotech.military.api.Point;
import robo2d.game.box2d.Box;
import robo2d.game.box2d.Physical;
import robo2d.game.box2d.StaticBox;
import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ControllerImpl implements Physical, Enterable, Host {

    public static final float SIZE = 6;

    String name;
    StaticBox box;
    KPoint pos;
    float angle;
    CpuImpl cpu;
    PlayerImpl owner;

    PlayerImpl enteredPlayer;

    public KPoint getPos() {
        return pos;
    }

    public Double getAngle() {
        return (double) angle;
    }

    public ControllerImpl(PlayerImpl owner, KPoint pos, float angle, String name) {
        this.name = name;
        this.pos = pos;
        this.angle = angle;
        this.owner = owner;

        ArrayList<KPoint> kPoints = new ArrayList<KPoint>();

        kPoints.add(new KPoint(1f, 1f));
        kPoints.add(new KPoint(1f, -1f));
        kPoints.add(new KPoint(-1f, -1f));
        kPoints.add(new KPoint(-1f, 1f));
        kPoints.add(new KPoint(-1f / 4f, 1f));
        kPoints.add(new KPoint(-1f / 4f, 7f / 9f));
        kPoints.add(new KPoint(-8f / 9f, 7f / 9f));
        kPoints.add(new KPoint(-8f / 9f, -8f / 9f));
        kPoints.add(new KPoint(8f / 9f, -8f / 9f));
        kPoints.add(new KPoint(8f / 9f, 7f / 9f));
        kPoints.add(new KPoint(1f / 4f, 7f / 9f));
        kPoints.add(new KPoint(1f / 4f, 1f));
        KPolygon polygon = new KPolygon(kPoints);
        polygon.scale(SIZE * 0.7);
        polygon.rotate(-FastMath.PI / 2);

        box = new StaticBox(polygon, pos, angle);
    }

    public void addLaptop(CpuImpl laptop) {
        this.cpu = laptop;
        laptop.setup(this);
    }

    @Override
    public Box getBox() {
        return box;
    }

    @Override
    public boolean canEnter(PlayerImpl player) {
        return owner == player;
    }

    @Override
    public void enter(PlayerImpl player) {
        if (enteredPlayer == null) {
            enteredPlayer = player;
            Terminal.open(cpu);
        }
    }

    @Override
    public Point2D exit() {
        if (enteredPlayer != null) {
            Terminal.close(cpu);
            enteredPlayer = null;
            return getBox().getPosition();
        } else {
            return null;
        }
    }

    @Override
    public CpuImpl getComputer() {
        return cpu;
    }

    @Override
    public boolean consumeEnergy(double v) {
        return true;
    }

    @Override
    public String getUid() {
        return name;
    }

    @Override
    public Point getPosition() {
        Point2D p = box.getPosition();
        return new Point((float) p.getX(), (float) p.getY());
    }

    public void init() {
        cpu.init();
    }

    public void update() {
        cpu.update();
    }
}

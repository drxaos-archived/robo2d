package robo2d.game.impl;

import com.jme3.math.FastMath;
import com.robotech.military.api.Point;
import robo2d.game.Game;
import robo2d.game.box2d.Box;
import robo2d.game.box2d.Physical;
import robo2d.game.box2d.RobotBox;
import straightedge.geom.KPoint;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

public class RobotImpl implements Physical, Enterable, Host {

    Set<EquipmentImpl> equipment = new HashSet<EquipmentImpl>();
    Set<HasEffects> hasEffects = new HashSet<HasEffects>();
    ComputerImpl computer;

    Game game;
    PlayerImpl owner;
    RobotBox box;
    String uid;
    double energy = 0d;

    final Object sync = new Object();
    String debugMsg;
    Point2D debugPoint;

    PlayerImpl enteredPlayer;
    private Point2D position;

    public RobotImpl(String uid, Game game, PlayerImpl owner, KPoint position, double angle) {
        this.uid = uid;
        this.owner = owner;
        this.game = game;
        box = new RobotBox(uid, position, angle - FastMath.PI / 2);
    }

    public <T extends EquipmentImpl> T getEquipment(Class<T> type) {
        for (EquipmentImpl eq : equipment) {
            if (type.isAssignableFrom(eq.getClass())) {
                return (T) eq;
            }
        }
        return null;
    }

    public PlayerImpl getOwner() {
        return owner;
    }

    public ChassisImpl getChassis() {
        return getEquipment(ChassisImpl.class);
    }

    public ComputerImpl getComputer() {
        return computer;
    }

    public RadarImpl getRadar() {
        return getEquipment(RadarImpl.class);
    }

    public void addEquipment(EquipmentImpl equipment) {
        this.equipment.add(equipment);
        if (equipment instanceof HasEffects) {
            hasEffects.add((HasEffects) equipment);
        }
        if (equipment instanceof ComputerImpl) {
            computer = (ComputerImpl) equipment;
        }
        equipment.setup(this);
    }

    public Double getEnergy() {
        return energy;
    }

    public String getUid() {
        return uid;
    }

    public boolean consumeEnergy(double amount) {
        if (energy > amount) {
            energy -= amount;
            if (energy < 0.01) {
                energy = 0;
            }
            return true;
        } else {
            energy = 0;
            return false;
        }
    }

    public void charge(double energy) {
        this.energy += energy;
    }

    public Long getTime() {
        return game.getTime();
    }

    public void waitForStep() {
        try {
            synchronized (sync) {
                sync.wait();
            }
        } catch (InterruptedException e) {
        }
    }

    public void debug(Object dbg) {
        if (dbg == null) {
            debugPoint = null;
            debugMsg = null;
        } else if (dbg instanceof Point2D) {
            debugPoint = (Point2D) dbg;
        } else {
            debugMsg = dbg.toString();
            System.out.println(debugMsg);
        }
    }

    public String getDebug() {
        return debugMsg;
    }

    public Point2D getDebugPoint() {
        return debugPoint;
    }

    @Override
    public Box getBox() {
        return box;
    }

    public void applyEffects() {
        for (HasEffects eq : hasEffects) {
            box.applyForces(uid, eq.getEffects());
        }
    }


    public void sync() {
        synchronized (sync) {
            sync.notifyAll();
        }
    }

    @Override
    public boolean canEnter(PlayerImpl player) {
        return owner == player;
    }

    @Override
    public void enter(PlayerImpl player) {
        if (enteredPlayer == null) {
            enteredPlayer = player;
            Terminal.open(getComputer());
        }
    }

    @Override
    public Point2D exit() {
        if (enteredPlayer != null) {
            Terminal.close(getComputer());
            enteredPlayer = null;
            return getBox().getPosition();
        } else {
            return null;
        }
    }

    public void init() {
        for (EquipmentImpl e : equipment) {
            e.init();
        }
    }

    public void update() {
        for (EquipmentImpl e : equipment) {
            e.update();
        }
    }

    public Point getPosition() {
        Point2D p = box.getPosition();
        return new Point((float) p.getX(), (float) p.getY());
    }

    @Override
    public Double getAngle() {
        return box.getAngle();
    }
}

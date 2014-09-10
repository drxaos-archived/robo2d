package robo2d.game.impl;

import com.robotech.military.api.Point;
import robo2d.game.box2d.Box;
import robo2d.game.box2d.Physical;
import robo2d.game.box2d.StaticBox;
import robo2d.testbed.devices.DeviceManager;
import robo2d.testbed.devices.DisplayFrame;
import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ControllerImpl implements Physical, Enterable, Host, Dynamic, HasDevices {

    public static final float SIZE = 6;

    public static final String DM_CPU = "Connect to CPU";
    public static final String DM_CPU_RESTART = "Restart CPU";
    public static final String DM_DISPLAY = "View display";

    String name;
    StaticBox box;
    KPoint pos;
    float angle;
    CpuImpl cpu;
    PlayerImpl owner;
    boolean hasDisplay = true;

    PlayerImpl enteredPlayer;

    public KPoint getPos() {
        return pos;
    }

    public Double getAngle() {
        return (double) angle;
    }

    @Override
    public boolean hasAccessToComputer() {
        return owner == enteredPlayer;
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
        KPolygon polygon = new KPolygon(kPoints);
        polygon.scale(SIZE * 0.05);

        box = new StaticBox(polygon, pos, angle);
    }

    public void setHasDisplay(boolean hasDisplay) {
        this.hasDisplay = hasDisplay;
    }

    public void addCpu(CpuImpl cpu) {
        this.cpu = cpu;
        cpu.setup(this);
    }

    @Override
    public Box getBox() {
        return box;
    }

    @Override
    public boolean canEnter(PlayerImpl player) {
        return true;
    }

    @Override
    public void enter(PlayerImpl player) {
        if (enteredPlayer == null) {
            enteredPlayer = player;
            Terminal.open(cpu);
            DeviceManager.setDevicesHost(this);
        }
    }

    @Override
    public Point2D exit() {
        if (enteredPlayer != null) {
            Terminal.close(cpu);
            DeviceManager.setDevicesHost(null);
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

    @Override
    public String[] getDevices() {
        ArrayList<String> dev = new ArrayList<String>();
        if (cpu.canDebug()) {
            dev.add(DM_CPU);
            dev.add(DM_CPU_RESTART);
        }
        if (hasDisplay) {
            dev.add(DM_DISPLAY);
        }
        return dev.toArray(new String[dev.size()]);
    }

    @Override
    public void activate(String device) {
        if (device.equals(DM_CPU)) {
            Terminal.connect();
        } else if (device.equals(DM_CPU_RESTART)) {
            Terminal.disconnect();
            cpu.stopProgram();
            cpu.startProgram();
        } else if (device.equals(DM_DISPLAY)) {
            DisplayFrame.showDisplay();
        }
    }

    @Override
    public void deactivate(String device) {
    }

    @Override
    public void close() {
        DisplayFrame.hideDisplay();
    }
}

package com.robotech.game;

import org.jbox2d.dynamics.World;
import com.robotech.game.Physical;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {

    // transients
    public transient Object stepSync;
    protected transient World worldBox;

    // state
    protected long ticks = 0;
    protected List<Physical> physicals = new ArrayList<Physical>();

    // initialization
    public void add(Physical object) {
        physicals.add(object);
    }

    public void start(World world) {
        stepSync = new Object();
        this.worldBox = world;

        for (Physical physical : physicals) {
            physical.init();
        }
    }

    // simulation
    public void beforeStep() {
        for (Physical physical : physicals) {
            physical.beforeStep(ticks);
        }
    }

    public void afterStep() {
        ticks++;
        for (Physical physical : physicals) {
            physical.afterStep(ticks);
        }
    }

    // serialization
    public void save(OutputStream toStream) throws IOException {
        synchronized (this) {
            ObjectOutputStream oos = new ObjectOutputStream(toStream);
            oos.writeObject(this);
        }
    }

    public static Game load(InputStream fromStream) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(fromStream);
        return (Game) ois.readObject();
    }
}

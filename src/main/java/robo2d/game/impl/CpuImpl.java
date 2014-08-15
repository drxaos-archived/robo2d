package robo2d.game.impl;

public class CpuImpl extends AbstractComputer {
    public static enum State {
        ON, OFF
    }

    ControllerImpl controller;
    String name;
    State initState;

    public CpuImpl(String name, State state) {
        this.name = name;
        initState = state;
    }

    public void setup(ControllerImpl controller) {
        this.controller = controller;
        Terminal.registerComputer(this);
    }

    public boolean bootOnStartup() {
        return initState == State.ON;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void update() {
        super.update();
    }

    public String getUid() {
        return name + "-" + cid;
    }

    @Override
    public String getName() {
        return name;
    }
}

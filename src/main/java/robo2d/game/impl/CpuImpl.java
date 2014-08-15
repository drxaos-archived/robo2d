package robo2d.game.impl;

import java.util.ArrayList;
import java.util.List;

public class CpuImpl extends AbstractComputer {

    ControllerImpl controller;
    String name;

    public CpuImpl(String name) {
        this.name = name;
    }

    public void setup(ControllerImpl controller) {
        this.controller = controller;
        Terminal.registerComputer(this);
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

package robo2d.game.impl;

import java.util.ArrayList;
import java.util.List;

public class LaptopImpl extends AbstractComputer {

    BaseImpl base;
    String name;
    List<String> cameras = new ArrayList<String>();
    String activeCamera;

    public LaptopImpl(String name) {
        this.name = name;
    }

    public void setup(BaseImpl base) {
        this.base = base;
        Terminal.registerComputer(this);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void update() {
        super.update();
        String cam = state.get("computer/camera");
        if (cameras.contains(cam)) {
            activeCamera = cam;
        } else {
            activeCamera = null;
        }
    }

    public void addCamera(String name) {
        cameras.add(name);
    }

    public String getActiveCamera() {
        return activeCamera;
    }

    public String getUid() {
        return name + "-" + cid;
    }

    @Override
    public String getName() {
        return name;
    }
}

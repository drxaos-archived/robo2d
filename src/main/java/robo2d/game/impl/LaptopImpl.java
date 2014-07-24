package robo2d.game.impl;

public class LaptopImpl extends AbstractComputer {

    BaseImpl base;
    String name;

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
    }

    public String getUid() {
        return name + "-" + cid;
    }

    @Override
    public String getName() {
        return name;
    }
}

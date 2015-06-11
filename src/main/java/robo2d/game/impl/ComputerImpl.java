package robo2d.game.impl;

public class ComputerImpl extends AbstractComputer implements EquipmentImpl {

    public static enum State {
        ON, OFF
    }

    Host robot;
    State initState;

    public ComputerImpl(State state) {
        initState = state;
    }

    @Override
    public void setup(Host robot) {
        this.robot = robot;
    }

    public boolean bootOnStartup() {
        return initState == State.ON;
    }

    @Override
    public boolean canDebug() {
        return robot.hasAccessToComputer();
    }

    public String getUid() {
        return robot.getUid() + "-" + cid;
    }

    public String getName() {
        return robot.getUid();
    }
}

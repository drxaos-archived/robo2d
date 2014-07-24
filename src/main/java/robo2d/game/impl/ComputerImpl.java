package robo2d.game.impl;

public class ComputerImpl extends AbstractComputer implements EquipmentImpl {

    public static enum State {
        ON, OFF
    }

    RobotImpl robot;
    State initState;

    public ComputerImpl(State state) {
        initState = state;
    }

    @Override
    public void setup(RobotImpl robot) {
        this.robot = robot;
        Terminal.registerComputer(this);
    }

    public boolean bootOnStartup() {
        return initState == State.ON;
    }

    public String getUid() {
        return robot.getUid() + "-" + cid;
    }

    public String getName() {
        return robot.getUid();
    }
}

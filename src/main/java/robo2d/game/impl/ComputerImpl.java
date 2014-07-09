package robo2d.game.impl;

import com.robotech.military.api.Computer;

import java.io.IOException;

public class ComputerImpl implements Computer, EquipmentImpl {

    boolean initWorking;

    RobotImpl robot;

    public ComputerImpl(boolean working) {
        initWorking = working;
    }

    public boolean isInitWorking() {
        return initWorking;
    }

    @Override
    public void setup(RobotImpl robot) {
        this.robot = robot;
    }

    public synchronized void startProgram() {
        try {
            Process process = Runtime.getRuntime().exec("java -cp programs/" + robot.getUid() + " Boot");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void stopProgram() {
    }

    public boolean isRunning() {
        return false;
    }
}

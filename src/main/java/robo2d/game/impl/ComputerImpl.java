package robo2d.game.impl;

import robo2d.game.api.Computer;

public class ComputerImpl implements Computer, EquipmentImpl {

    RobotImpl robot;
    Thread program;

    @Override
    public void setup(RobotImpl robot) {
        this.robot = robot;
    }

    public void startProgram() {
        try {
            if (program == null) {
                RobotProgram robotProgram = robot.owner.program.getConstructor().newInstance();
                robotProgram.robot = robot;
                program = new Thread(robotProgram);
                program.setDaemon(true);
                program.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopProgram() {
        try {
            if (program != null) {
                program.interrupt();
                program.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

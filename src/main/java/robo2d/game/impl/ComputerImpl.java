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
                RobotProgram robotProgram = robot.program.getConstructor().newInstance();
                robotProgram.robot = robot;
                program = new Thread(robotProgram);
                program.setDaemon(true);
                program.setPriority(Thread.MIN_PRIORITY);
                program.start();
            } else {
                double consumption = 0.01;
                if (program.getState() == Thread.State.RUNNABLE) {
                    consumption = 0.05;
                }
                if (!robot.consumeEnergy(consumption)) {
                    stopProgram();
                }
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
                program = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return program != null;
    }
}

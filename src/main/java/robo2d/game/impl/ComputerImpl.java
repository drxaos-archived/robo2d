package robo2d.game.impl;

import com.robotech.military.api.Computer;
import com.robotech.military.api.Program;

import java.util.HashMap;
import java.util.Map;

public class ComputerImpl implements Computer, EquipmentImpl {

    RobotImpl robot;
    Thread program;
    Map<String, String> memory = new HashMap<String, String>();
    boolean initWorking;

    public ComputerImpl(boolean working) {
        initWorking = working;
    }

    @Override
    public void setup(RobotImpl robot) {
        this.robot = robot;
    }

    protected Class compile() {
        try {
            return Class.forName(memory.get("Program.java"));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }


    public void startProgram() {
        try {
            if (program == null) {
                Class code = compile();
                if (code == null || !Program.class.isAssignableFrom(code)) {
                    return;
                }
                Program robotProgram = (Program) code.getConstructor().newInstance();
                robotProgram.init(robot); // TODO move to thread
                program = new Thread(robotProgram);
                program.setDaemon(true);
                program.setPriority(Thread.MIN_PRIORITY);
                program.start();
            } else {
                double consumption = 0.0001;
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
        return program != null && program.isAlive();
    }

    public boolean isInitWorking() {
        return initWorking;
    }

    @Override
    public void saveFile(String fileName, String content) {
        memory.put(fileName, content);
    }

    @Override
    public String loadFile(String fileName) {
        return memory.get(fileName);
    }
}

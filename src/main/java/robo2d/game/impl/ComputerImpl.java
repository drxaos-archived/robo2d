package robo2d.game.impl;

import com.robotech.military.api.Computer;
import com.robotech.military.api.Program;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class ComputerImpl implements EquipmentImpl {

    RobotImpl robot;
    Thread program;
    Map<String, String> memory = new HashMap<String, String>();
    boolean initWorking;
    ComputerInterfaceImpl computerInterface;

    public ComputerImpl(boolean working) {
        initWorking = working;
    }

    @Override
    public void setup(RobotImpl robot) {
        this.robot = robot;
        computerInterface = new ComputerInterfaceImpl(robot);
    }

    public ComputerInterfaceImpl getComputerInterface() {
        return computerInterface;
    }

    protected Class compile() {
        File file = ComputerHelper.compile(this);
        if (file == null) {
            return null;
        }
        try {
            // Try this if reloading fails
            //  http://tutorials.jenkov.com/java-reflection/dynamic-class-loading-reloading.html
            URL classUrl = file.toURI().toURL();
            URL[] urls = new URL[]{classUrl};
            ClassLoader ucl = new URLClassLoader(urls, getClass().getClassLoader());
            return ucl.loadClass("Boot");
        } catch (ClassNotFoundException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, String> getMemory() {
        return memory;
    }

    public void startProgram() {
        try {
            if (program == null) {
                final Class code = compile();
                if (code == null || !Program.class.isAssignableFrom(code)) {
                    return;
                }
                program = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Program robotProgram = (Program) code.getConstructor().newInstance();
                            robotProgram.run(robot);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
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
            program = null;
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

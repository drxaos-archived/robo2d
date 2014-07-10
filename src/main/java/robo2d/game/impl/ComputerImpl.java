package robo2d.game.impl;

import com.robotech.military.api.Computer;
import org.apache.maven.cli.MavenCli;

import java.io.File;

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
            new Thread() {
                @Override
                public void run() {
                    File notebook = new File("notebook");
                    MavenCli cli = new MavenCli();
                    String uid = robot.getUid();
                    String rid = robot.getUid();
                    cli.doMain(new String[]{"exec:java", "-Dexec.args=\"" + uid + " " + rid + "\""}, notebook.getAbsolutePath(), System.out, System.out);
                    System.out.println("PROGRAM END");
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void stopProgram() {
    }

    public boolean isRunning() {
        return false;
    }
}

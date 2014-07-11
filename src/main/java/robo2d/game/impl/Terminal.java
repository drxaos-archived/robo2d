package robo2d.game.impl;

import bluej.Boot;
import bluej.Main;

import java.io.File;

public class Terminal {

    static ComputerImpl computer;

    public static void open(final ComputerImpl computer) {
        Terminal.computer = computer;
        if (computer != null) {
            final File dir = new File("computer");
            ComputerHelper.saveToDisk(computer, dir);
            Main.registerBluejListener(new Main.BluejListener() {
                @Override
                public void onExit() {

                }

                @Override
                public void deploy() {
                    ComputerHelper.loadFromDisk(computer, dir, false);
                    computer.stopProgram();
                    computer.startProgram();
                }
            });
            Boot.start("computer");
        }
    }

    public static void close() {
        Boot.exit();
        ComputerHelper.loadFromDisk(computer, new File("computer"), true);
    }

}

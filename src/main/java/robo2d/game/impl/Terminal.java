package robo2d.game.impl;

import bluej.Boot;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class Terminal {

    static ComputerImpl computer;

    public static void open(ComputerImpl computer) {
        Terminal.computer = computer;
        if (computer != null) {
            ComputerHelper.saveToDisk(computer, new File("computer"));
            Boot.start("computer");
        }
    }

    public static void close() {
        Boot.exit();
        ComputerHelper.loadFromDisk(computer, new File("computer"));
    }

}

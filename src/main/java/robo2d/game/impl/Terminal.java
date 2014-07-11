package robo2d.game.impl;

import bluej.Boot;

public class Terminal {

    public static void open(ComputerImpl computer) {
        if (computer != null) {
            Boot.start("computer");
        }
    }

    public static void close() {
        Boot.exit();
    }

}

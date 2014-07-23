package robo2d.game.impl;

import bluej.Boot;
import bluej.Main;
import bluej.debugger.Debugger;
import bluej.pkgmgr.PkgMgrFrame;

import java.io.File;

public class Terminal {

    static ComputerImpl computer;
    static BaseImpl base;

    public synchronized static void open(final ComputerImpl computer) {
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
                    unbindDebug();
                    ComputerHelper.loadFromDisk(computer, dir, false);
                    computer.stopProgram();
                    computer.startProgram();
                }

                @Override
                public void halt() {
                    computer.stopProgram();
                }

                @Override
                public void debuggerReady(Debugger dbg) {
                }
            });

            bindDebug();

            try {
                PkgMgrFrame.doOpen(new File("computer"), PkgMgrFrame.getAllFrames()[0]);
            } catch (Throwable e) {
                e.printStackTrace();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                try {
                    // try again
                    PkgMgrFrame.doOpen(new File("computer"), PkgMgrFrame.getAllFrames()[0]);
                } catch (Throwable e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public synchronized static void open(final BaseImpl base) {
        Terminal.base = base;
        final File dir = new File("computer");
        //ComputerHelper.saveToDisk(base, dir);
        Main.registerBluejListener(null);
        PkgMgrFrame.doOpen(new File("computer"), PkgMgrFrame.getAllFrames()[0]);
    }

    public synchronized static void close(ComputerImpl computer) {
        Main.registerBluejListener(null);
        unbindDebug();
        Boot.exit();
        ComputerHelper.loadFromDisk(computer, new File("computer"), true);
    }

    public synchronized static void close(BaseImpl base) {
        Main.registerBluejListener(null);
        unbindDebug();
        Boot.exit();
        //ComputerHelper.loadFromDisk(base, new File("computer"), true);
    }

    private static void bindDebug() {
    }

    private static void unbindDebug() {
    }

}

package robo2d.game.impl;

import bluej.Boot;
import bluej.Main;
import bluej.debugger.Debugger;
import bluej.debugger.DebuggerResult;
import bluej.debugmgr.objectbench.ObjectWrapper;
import bluej.pkgmgr.PkgMgrFrame;
import bluej.pkgmgr.Project;
import bluej.testmgr.record.ConstructionInvokerRecord;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.Server;

import java.io.File;

public class Terminal {

    static ComputerImpl computer;
    static BaseImpl base;

    static CallHandler callHandler;
    static Server server;

    public static void exportInterface() {
        if (server == null) {
            ComputerInterfaceImpl computerInterface;
            try {
                computerInterface = computer.getComputerInterface();

                callHandler = new CallHandler();
                callHandler.registerGlobal(ComputerInterface.class, computerInterface);
                server = new Server();
                int thePortIWantToBind = 4455;
                server.bind(thePortIWantToBind, callHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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
                    unbindRmi();
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
                    exportInterface();
                    DebuggerResult debuggerResult = dbg.instantiateClass("robo2d.game.impl.ComputerInterfaceProxy");
                    if (debuggerResult.getResultObject() != null) {
                        Project openProj = Main.getOpenProj();
                        for (PkgMgrFrame pkgMgrFrame : PkgMgrFrame.getAllFrames()) {
                            if (pkgMgrFrame.getPackage().getId().equals(openProj.getPackage("").getId())) {

                                ObjectWrapper wrapper = ObjectWrapper.getWrapper(pkgMgrFrame, pkgMgrFrame.getObjectBench(), debuggerResult.getResultObject(),
                                        debuggerResult.getResultObject().getGenType(), "computer");
                                pkgMgrFrame.getObjectBench().addObject(wrapper);

                                dbg.addObject(openProj.getPackage("").getId(), "computer", debuggerResult.getResultObject());

                                dbg.addObject(pkgMgrFrame.getPackage().getId(), wrapper.getName(), debuggerResult.getResultObject());

                                pkgMgrFrame.getObjectBench().addInteraction(new ConstructionInvokerRecord(
                                        "robo2d.game.impl.ComputerInterfaceProxy",
                                        "computer",
                                        "new robo2d.game.impl.ComputerInterfaceProxy()",
                                        null
                                ));
                            }
                        }
                    } else {
                        System.out.println(debuggerResult.getException());
                    }
                }
            });

            try {
                PkgMgrFrame.doOpen(new File("computer"), PkgMgrFrame.getAllFrames()[0]);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized static void open(final BaseImpl base) {
        Terminal.base = base;
        final File dir = new File("computer");
        ComputerHelper.saveToDisk(base, dir);
        Main.registerBluejListener(null);
        PkgMgrFrame.doOpen(new File("computer"), PkgMgrFrame.getAllFrames()[0]);
    }

    public synchronized static void close(ComputerImpl computer) {
        Main.registerBluejListener(null);
        unbindRmi();
        Boot.exit();
        ComputerHelper.loadFromDisk(computer, new File("computer"), true);
    }

    public synchronized static void close(BaseImpl base) {
        Main.registerBluejListener(null);
        unbindRmi();
        Boot.exit();
        ComputerHelper.loadFromDisk(base, new File("computer"), true);
    }

    private static void unbindRmi() {
        if (server != null) {
            try {
                server.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            server = null;
            try {
                Project openProj = Main.getOpenProj();
                if (openProj != null) {
                    openProj.restartVM();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}

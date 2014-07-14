package robo2d.game.impl;

import bluej.Boot;
import bluej.Main;
import bluej.debugger.Debugger;
import bluej.debugger.DebuggerResult;
import bluej.pkgmgr.PkgMgrFrame;
import bluej.pkgmgr.Project;
import bluej.testmgr.record.ConstructionInvokerRecord;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

public class Terminal {

    static ComputerImpl computer;
    public static Registry registry;

    static {
        try {
            registry = LocateRegistry.createRegistry(7099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static Set<Remote> exported = new HashSet<Remote>();

    public synchronized static void open(final ComputerImpl computer) {
        Terminal.computer = computer;
        if (computer != null) {
            try {
                ComputerInterfaceImpl computerInterface = computer.getComputerInterface();
                Remote stub = UnicastRemoteObject.exportObject(computerInterface, 0);
                exported.add(computerInterface);
                registry.rebind("ComputerInterface", stub);
            } catch (Exception e) {
                e.printStackTrace();
            }

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
                    DebuggerResult debuggerResult = dbg.instantiateClass("robo2d.game.impl.proxy.ComputerInterfaceProxy");
                    if (debuggerResult.getResultObject() != null) {
                        Project openProj = Main.getOpenProj();
                        dbg.addObject(openProj.getPackage("").getId(), "computer", debuggerResult.getResultObject());
                        for (PkgMgrFrame pkgMgrFrame : PkgMgrFrame.getAllFrames()) {
                            if (pkgMgrFrame.getPackage().getId().equals(openProj.getPackage(""))) {
                                pkgMgrFrame.getObjectBench().addInteraction(new ConstructionInvokerRecord(
                                        "robo2d.game.impl.proxy.ComputerInterfaceProxy",
                                        "computer",
                                        "new robo2d.game.impl.proxy.ComputerInterfaceProxy()",
                                        null
                                ));
                            }
                        }
                    } else {
                        System.out.println(debuggerResult.getException());
                    }
                }
            });

            PkgMgrFrame.doOpen(new File("computer"), PkgMgrFrame.getAllFrames()[0]);
        }
    }

    public synchronized static void close() {
        unbindRmi();
        Boot.exit();
        ComputerHelper.loadFromDisk(computer, new File("computer"), true);
    }

    private static void unbindRmi() {
        try {
            for (Remote remote : exported) {
                UnicastRemoteObject.unexportObject(remote, true);
            }
            exported.clear();
            for (String stub : registry.list()) {
                try {
                    registry.unbind(stub);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

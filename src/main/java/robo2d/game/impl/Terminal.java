package robo2d.game.impl;

import bluej.Boot;
import bluej.Main;
import bluej.debugger.Debugger;
import bluej.debugger.DebuggerEvent;
import bluej.debugger.DebuggerListener;
import bluej.debugger.DebuggerResult;
import bluej.pkgmgr.Project;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

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

    public synchronized static void open(final ComputerImpl computer) {
        new Thread() {
            @Override
            public void run() {
                Terminal.computer = computer;
                if (computer != null) {
                    try {
                        Remote stub = UnicastRemoteObject.exportObject(computer.getComputerInterface(), 0);
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
                    });
                    Boot.start("computer");

                    long timeout = System.currentTimeMillis() + 30000;
                    while (System.currentTimeMillis() < timeout) {
                        final Project openProj = Main.getOpenProj();
                        if (openProj != null) {
                            Debugger debugger = openProj.getDebugger();
                            if (debugger != null && debugger.getStatus() == Debugger.IDLE) {
                                final Debugger dbg = debugger;
                                int state = debugger.addDebuggerListener(new DebuggerListener() {
                                    @Override
                                    public boolean examineDebuggerEvent(DebuggerEvent e) {
                                        if (e.getID() == DebuggerEvent.DEBUGGER_STATECHANGED) {
                                            if (e.getOldState() == Debugger.NOTREADY && e.getNewState() == Debugger.IDLE) {
                                                DebuggerResult debuggerResult = dbg.instantiateClass("robo2d.game.impl.proxy.ComputerInterfaceProxy");
                                                if (debuggerResult.getResultObject() != null) {
                                                    dbg.addObject(openProj.getPackage("").getId(), "computer", debuggerResult.getResultObject());
                                                } else {
                                                    System.out.println(debuggerResult.getException());
                                                }
                                                return false;
                                            }
                                        }
                                        return true;
                                    }

                                    @Override
                                    public void processDebuggerEvent(DebuggerEvent e, boolean skipUpdate) {
                                    }
                                });
                                if (state == Debugger.IDLE) {
                                    DebuggerResult debuggerResult = dbg.instantiateClass("robo2d.game.impl.proxy.ComputerInterfaceProxy");
                                    if (debuggerResult.getResultObject() != null) {
                                        dbg.addObject(openProj.getPackage("").getId(), "computer", debuggerResult.getResultObject());
                                    } else {
                                        System.out.println(debuggerResult.getException());
                                    }
                                    break;
                                }
                            }
                        }
                        synchronized (Terminal.class) {
                            try {
                                Terminal.class.wait(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }.start();
    }

    public synchronized static void close() {
        unbindRmi();
        Boot.exit();
        ComputerHelper.loadFromDisk(computer, new File("computer"), true);
    }

    private static void unbindRmi() {
        try {
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

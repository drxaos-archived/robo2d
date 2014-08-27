package robo2d.game.impl;

import bluej.Boot;
import bluej.Main;
import bluej.debugger.Debugger;
import bluej.pkgmgr.PkgMgrFrame;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Terminal {

    static boolean connected = false;
    static AbstractComputer computer;
    static CampImpl base;

    public static boolean canConnect() {
        return computer != null;
    }

    public synchronized static void open(final AbstractComputer computer) {
        Terminal.computer = computer;
    }

    public synchronized static void connect() {
        if (connected) {
            disconnect();
        }
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

                @Override
                public void connect() {
                    Terminal.connect();
                }

                @Override
                public void disconnect() {
                    Terminal.disconnect();
                }
            });

            bindDebug();

            try {
                PkgMgrFrame.doOpen(new File("computer"), PkgMgrFrame.getAllFrames()[0]);
                connected = true;
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

    public static Main.BluejListener defaultListener = new Main.BluejListener() {
        @Override
        public void onExit() {
        }

        @Override
        public void deploy() {
        }

        @Override
        public void halt() {
        }

        @Override
        public void debuggerReady(Debugger debugger) {
        }

        @Override
        public void connect() {
            Terminal.connect();
        }

        @Override
        public void disconnect() {
            Terminal.disconnect();
        }
    };

    static {
        Main.registerBluejListener(defaultListener);
    }

    public synchronized static void close(AbstractComputer computer) {
        disconnect();
        Terminal.computer = null;
    }

    public synchronized static void disconnect() {
        if (connected) {
            Main.registerBluejListener(defaultListener);
            unbindDebug();
            Boot.exit();
            ComputerHelper.loadFromDisk(computer, new File("computer"), true);
            connected = false;
        }
    }

    private static void bindDebug() {
    }

    private static void unbindDebug() {
    }

    static Map<String, AbstractComputer> computers = new HashMap<String, AbstractComputer>();

    public synchronized static void registerComputer(AbstractComputer computer) {
        computers.put(computer.getUid(), computer);
    }

    public synchronized static void unRegisterComputer(AbstractComputer computer) {
        computers.remove(computer.getUid());
    }

    public synchronized static void bindInterface() {
        try {
            final ServerSocket servers = new ServerSocket(4455);
            new Thread() {
                @Override
                public void run() {
                    System.out.println("Starting computers interface");
                    BufferedReader in = null;
                    PrintWriter out = null;

                    Socket socket = null;

                    try {
                        while (true) {
                            socket = servers.accept();
                            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            out = new PrintWriter(socket.getOutputStream());
                            String id = in.readLine();
                            System.out.println("New computer connection: " + id);
                            AbstractComputer computer = computers.get(id);
                            if (id.equals("debug")) {
                                computer = Terminal.computer;
                            }
                            if (computer == null) {
                                out.println("not found");
                                socket.close();
                            } else {
                                final AbstractComputer finalComputer = computer;
                                final Socket finalSocket = socket;
                                final BufferedReader finalIn = in;
                                final PrintWriter finalOut = out;
                                final String finalId = id;
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            System.out.println("Handling connection: " + finalId);
                                            finalComputer.handleConnection(finalId, finalSocket, finalIn, finalOut);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            finalIn.close();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                        try {
                                            finalOut.close();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                        try {
                                            finalSocket.close();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }.start();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            if (socket != null) {
                                socket.close();
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

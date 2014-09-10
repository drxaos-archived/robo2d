package robo2d.testbed.devices;

import robo2d.game.impl.HasDevices;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DeviceManager extends JFrame {
    static DeviceManager inst;
    static HasDevices host;
    JList list;

    static MouseListener ml = new MouseAdapter() {
        public void mouseClicked(MouseEvent evt) {
            JList list = (JList) evt.getSource();
            if (evt.getClickCount() == 2) {
                int index = list.locationToIndex(evt.getPoint());
                if (host != null) {
                    host.activate("" + list.getSelectedValue());
                }
            }
        }
    };

    public DeviceManager() throws HeadlessException {
        super("Device Manager");

        setDefaultLookAndFeelDecorated(true);

        BoxLayout boxLayout = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        list = new JList();
        list.addMouseListener(ml);
        add(new JScrollPane(list));

        setPreferredSize(new Dimension(250, 250));
        pack();

        setVisible(true);
    }

    public static void start() {
        inst = new DeviceManager();
    }

    public static void setDevicesHost(HasDevices devicesHost) {
        if (devicesHost != null) {
            setDevicesHost(null);
            inst.list.setListData(devicesHost.getDevices());
            host = devicesHost;
        } else if (host != null) {
            host.close();
            host = null;
            inst.list.setListData(new String[0]);
        }
    }
}

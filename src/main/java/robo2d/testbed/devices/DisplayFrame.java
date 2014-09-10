/*
 This file is part of the BlueJ program. 
 Copyright (C) 2013  Michael Kolling and John Rosenberg 
 
 This program is free software; you can redistribute it and/or 
 modify it under the terms of the GNU General Public License 
 as published by the Free Software Foundation; either version 2 
 of the License, or (at your option) any later version. 
 
 This program is distributed in the hope that it will be useful, 
 but WITHOUT ANY WARRANTY; without even the implied warranty of 
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 GNU General Public License for more details. 
 
 You should have received a copy of the GNU General Public License 
 along with this program; if not, write to the Free Software 
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. 
 
 This file is subject to the Classpath exception as provided in the  
 LICENSE.txt file that accompanied this code.
 */
package robo2d.testbed.devices;

import robo2d.game.impl.Terminal;

import javax.swing.*;
import java.awt.*;


public class DisplayFrame extends JFrame {
    static DisplayFrame inst;
    DisplayPanel displayPanel;

    public DisplayFrame() throws HeadlessException {
        super("Device display");

        setDefaultLookAndFeelDecorated(true);

        BoxLayout boxLayout = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        displayPanel = new DisplayPanel();
        add(new JScrollPane(displayPanel));

        setPreferredSize(new Dimension(640, 480));
        pack();

        setVisible(false);
    }

    public static void start() {
        inst = new DisplayFrame();
        Terminal.setDisplay(inst.displayPanel);
    }

    public static void showDisplay() {
        inst.setVisible(true);
    }

    public static void hideDisplay() {
        inst.setVisible(false);
    }
}

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
package bluej.pkgmgr;

import bluej.Config;
import bluej.utility.CenterLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class is a container to show a message for the user
 * when no project is opened
 *
 * @author amjad
 */
public class NoProjectMessagePanel extends JPanel {

    private JLabel noProjectMessageLabel;
    private static final Color TRANSPARANT = new Color(0f, 0f, 0f, 0.0f);
    public BufferedImage img = new BufferedImage(80 * 9, 25 * 16, BufferedImage.TYPE_INT_RGB);
    public boolean display = false;

    public NoProjectMessagePanel() {
        super(new CenterLayout());
        setBackground(TRANSPARANT);
        noProjectMessageLabel = new JLabel(Config.getString("pkgmgr.noProjectOpened.message"));
        noProjectMessageLabel.setEnabled(false);
        add(noProjectMessageLabel);
    }

    public void paintComponent(Graphics g) {
        if (display) {
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
        } else {
            super.paintComponent(g);
        }
    }
}

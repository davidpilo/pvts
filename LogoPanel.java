/*
 * LogoPanel.java
 *
 * Created on January 24, 2007, 12:15 PM
 *
 * author: David Pilo
 *
  Pilo's Visualization Tools for Scheme (PVTS). A Basic Visual Scheme Interpreter. 
    Copyright (C) 2007  David A. Pilo Mansion

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

Contact information:
David Pilo
davidpilo@gmail.com
 */

/**
 *
 * @author david
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class LogoPanel extends JPanel {

    LogoPanel () { 
    
  } 

  public void paintComponent(Graphics g)   { 
   // Paint background
   super.paintComponent(g); 


   // Get the drawing area 
   g.setColor(Color.black);
    g.drawRect(11,11,61,41);
    g.drawRect(21,21,61,41);
    g.drawRect(31,31,61,41);
    g.fillRect(12,12,61,41);
    g.setColor(Color.GRAY);
    g.fillRect(22,22,60,40);
    g.setColor(Color.LIGHT_GRAY);
    g.fillRect(32,32,60,40);
    g.setFont(new java.awt.Font("Courier", Font.BOLD, 15));
    g.setColor(Color.BLACK);
    g.drawString("PVTS",40,55);
    

  } // paintComponent
  
  
  
  
} // class LogoPanel


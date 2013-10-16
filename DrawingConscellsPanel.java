/* 
 *
 * DrawingConscellsPanel.java
 *
 * Author: David Pilo
 *
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




import javax.swing.*;
import java.awt.*;
import java.util.*;

public class DrawingConscellsPanel extends JPanel {

    public static int veryold_a = 0;
    public static int old_a = 0;
    public static Color color; 
    public static Color bcolor;
    static public Color defaultc;
    static public int zoom = 16;
    
    
  DrawingConscellsPanel () { 
    // Set background color for the applet's panel. 
    this.color = new Color(153,204,255);
    defaultc = this.getBackground();
    this.bcolor = Color.white;
    setBackground (bcolor);
  } // ctor

  public void paintComponent(Graphics g)   { 
   // Paint background
   super.paintComponent(g); 


   // Get the drawing area 
    this.setBackground(this.bcolor);
    draw_list_of_lists(PVTS.orphan_list_of_lists,Color.gray,g,true);
    draw_list_of_lists(PVTS.list_of_lists,Color.black,g,false);
    
    this.setPreferredSize(new Dimension(PVTS.b,PVTS.a));
    

  } // paintComponent
  
  
  
  
   public void draw_list_of_lists(LinkedList l, Color c, Graphics g,boolean t){
        // Prints the whole list of Conscells

        if (l == null) {
            //TextIO.put("yo i got a null list");
            return;
        }
        // TextIO.putln("0");
        ListIterator lI = l.listIterator();
        g.setColor(c);
        Color temp = g.getColor();
        //old_a = 0;
        while (lI.hasNext()){
            ListCC e = (ListCC) lI.next();
           // e.draw(g);
            if (PVTSFrame.stepper_mode && t) {
                g.setColor(Color.YELLOW);
               /* Polygon tmpPoly=new Polygon();
                tmpPoly.addPoint(0,old_a);
                tmpPoly.addPoint(10,old_a);
                tmpPoly.addPoint(10,old_a-10);
                tmpPoly.addPoint(15,old_a);
                tmpPoly.addPoint(10,old_a+20);
                tmpPoly.addPoint(0,old_a+20);
                g.fillPolygon(tmpPoly);*/
                if (old_a == PVTS.a)
                    g.fillRect(0,veryold_a-25,5000,PVTS.a - veryold_a);
                else
                    g.fillRect(0,old_a-25,5000,PVTS.a - old_a);
                g.setColor(temp);
            
            if (old_a != PVTS.a)
                veryold_a = old_a;
            old_a = PVTS.a;
            }
            e.draw(g);
        }
    }
} // class DrawingConscellsPanel

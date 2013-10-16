/*
 * DrawingTreesPanel.java
 *
 * Created on August 3, 2006, 11:50 AM
 *
 * Author: David Pilo
 *
 *  Pilo's Visualization Tools for Scheme (PVTS). A Basic Visual Scheme Interpreter. 
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
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.*;
import java.util.*;
/**
 *
 * @author david
 */
public class DrawingTreesPanel extends JPanel {
    

    
    //static PopupFactory factory = PopupFactory.getSharedInstance();
   // static LinkedList list_of_popups = new LinkedList();
   // public static LinkedList<JFrame> tagList = new LinkedList();
  // public static Popup popup;
   // public static int old_x=0;
    public static int old_y=0;
    public static int veryold_y=0;
    public static ProcedureAndArgs currentlySelected = null;
    public static Color color;
     public static Color bcolor; 
    static public Color defaultc;
    /** Creates a new instance of DrawingTreesPanel */
    public DrawingTreesPanel() {
        this.color = new Color(255,204,153);
         defaultc = this.getBackground();
        this.bcolor = Color.WHITE;
        setBackground (bcolor);
        //JInternalFrame funcTag = new JInternalFrame();
        
       /* this.addMouseMotionListener(new javax.swing.event.MouseInputListener() {
            public void mouseClicked(MouseEvent mouseEvent) {
            }
            public void mouseDragged(MouseEvent mouseEvent) {
            }
            public void mouseEntered(MouseEvent mouseEvent) {
            }
            public void mouseExited(MouseEvent mouseEvent) {
            }
            public void mouseMoved(MouseEvent mouseEvent) {
                  Point loc = mouseEvent.getPoint();
                 ProcedureAndArgs  current;
                 for (int i = 0; i < PVTS.list_of_trees.size(); i++) {
                     for (int j = 0; j < ((FunctionManager) PVTS.list_of_trees.get(i)).theTree.size() ; j++) {
                         for (int k = 0; k < ((LinkedList)((FunctionManager)PVTS.list_of_trees.get(i)).theTree.get(j)).size(); k++) {
                             if (((LinkedList)((FunctionManager)PVTS.list_of_trees.get(i)).theTree.get(j)).get(k).getClass().getName().equalsIgnoreCase("ProcedureAndArgs")) {
                                 current = ((ProcedureAndArgs)((LinkedList)((FunctionManager)PVTS.list_of_trees.get(i)).theTree.get(j)).get(k));
                                 if (popup != null) popup.hide();
                                 if (loc.x <= (current.xCoor + current.width) && loc.x >= current.xCoor
                                         && loc.y <= (current.yCoor + current.height) && loc.y >= current.yCoor) {
                                     
                                     String s = current.p+current.sexprs.toString();
                                     if (current.result.isListCC() || current.result.isAtom())
                                        s = s + " => " + current.result.toString();
                                 //    PVTSFrame.functag.setText(s);
                                 //    currentlySelected = current;
                                 
                                     
                                 //    if (!list_of_popups.contains(current)) {
                                 //        list_of_popups.add(current);
                                         
                                    popup = factory.getPopup(PVTSFrame.ftrees,
                                                  new JLabel(s),java.awt.MouseInfo.getPointerInfo().getLocation().x+20,
                                                                java.awt.MouseInfo.getPointerInfo().getLocation().y-20);
                                     
                                    // Popup popup = factory.getPopup(PVTSFrame.ftrees,
                                    ////               new JLabel(s),loc.x,
                                    //         loc.y+22);
                                     

                                    
                                     popup.show();
                                    // popup.wait()
                                     PVTSFrame.ftrees.repaint();
                                 }
                             }
                         }
                     }
                 }
             
            }
            public void mousePressed(MouseEvent mouseEvent) {
            }
            public void mouseReleased(MouseEvent mouseEvent) {
            }
        });*/
        
         this.addMouseListener(new java.awt.event.MouseListener() {
             public void mouseClicked(MouseEvent mouseEvent) {
                 Point loc = mouseEvent.getPoint();
                 ProcedureAndArgs  current;
                 for (int i = 0; i < PVTS.list_of_trees.size(); i++) {
                     for (int j = 0; j < ((FunctionManager) PVTS.list_of_trees.get(i)).theTree.size() ; j++) {
                         for (int k = 0; k < ((LinkedList)((FunctionManager)PVTS.list_of_trees.get(i)).theTree.get(j)).size(); k++) {
                             if (((LinkedList)((FunctionManager)PVTS.list_of_trees.get(i)).theTree.get(j)).get(k).getClass().getName().equalsIgnoreCase("ProcedureAndArgs")) {
                                 current = ((ProcedureAndArgs)((LinkedList)((FunctionManager)PVTS.list_of_trees.get(i)).theTree.get(j)).get(k));
                                 if (loc.x <= (current.xCoor + current.width) && loc.x >= current.xCoor
                                         && loc.y <= (current.yCoor + current.height) && loc.y >= current.yCoor) {
                                     
                                     String s = current.p+current.sexprs.toString();
                                     if (current.result.isListCC() || current.result.isAtom())
                                        s = s + " => " + current.result.toString();
                                     PVTSFrame.functag.setText(s);
                                     currentlySelected = current;
                                     PVTSFrame.ftrees.repaint();
                                     
                                 //    if (!list_of_popups.contains(current)) {
                                 //        list_of_popups.add(current);
                                         
                                 // //   Popup popup = factory.getPopup(PVTSFrame.ftrees,
                                 //                 new JLabel(s),loc.x+PVTSFrame.ftrees.getLocation().x,
                                 //            loc.y+22+PVTSFrame.ftrees.getLocation().y);
                                     
                                    // Popup popup = factory.getPopup(PVTSFrame.ftrees,
                                    ////               new JLabel(s),loc.x,
                                    //         loc.y+22);
                                     
                                 //    popup.show();
                                     //PVTSFrame.scrollPaneT.get
                                 //    list_of_popups.add(popup);
                                //     }
                                                    
                                     
                                     
                                   //  tagList.add(new JFrame());
                                     //PVTSFrame.ftrees.add(tagList.getLast());
                                   //  tagList.getLast().setAlwaysOnTop(true);
                                   //  tagList.getLast().add(new JLabel(current.p+current.sexprs.toString()));
                                   //  tagList.getLast().setSize(new Dimension((current.p+current.sexprs.toString()).length()*5,10));
                                   //  tagList.getLast().setLocation(loc.x+PVTSFrame.ftrees.getLocation().x,loc.y+PVTSFrame.ftrees.getLocation().y+22);
                                   //  tagList.getLast().setVisible(true);
                                 }
                             }
                             
                         }
                     }
                 }
             }
             public void mouseEntered(MouseEvent mouseEvent) {
             }
             public void mouseExited(MouseEvent mouseEvent) {
             }
             public void mousePressed(MouseEvent mouseEvent) {
             }
             public void mouseReleased(MouseEvent mouseEvent) {
             }
               
         } 
        );
    }
    
    
   /* public static void clearTagList () { 
        for (int i = 0; i < tagList.size(); i++) {
            tagList.get(0).dispose();
            tagList.remove(tagList.get(0));
        }
        //tagList.removeAll();
    }*/
    public void paintComponent(Graphics g)   {
        // Paint background
        super.paintComponent(g);
        this.setBackground(this.bcolor);
        draw_list_of_trees(PVTS.list_of_trees,g);
       // draw_popups();
        this.setPreferredSize(new Dimension(FunctionManager.max_x, FunctionManager.current_y+150));
    } // paintComponent
    
    
   /* public void draw_popups() {
        int min_x = PVTSFrame.ftrees.getLocation().x;
        int min_y = PVTSFrame.ftrees.getLocation().y;
        int max_x = PVTSFrame.ftrees.getSize().width;
        int max_y = PVTSFrame.ftrees.getSize().height;
        int p = PVTSFrame.scrollPaneT.getVerticalScrollBar().getValue();
        
      //  for (int i = 1; i < list_of_popups.size(); i = i+2) {
      //      if (((Popup)list_of_popups.get(i)).
      //  }
        
    }*/
    //java.awt.RenderingHints //antialiasing?
    
    public void draw_list_of_trees(LinkedList l,Graphics g) {

        
        if( l == null) {
            return;
        }
        Color temp = g.getColor();
        ListIterator lI = l.listIterator();
        FunctionManager.current_y = 10;
        FunctionManager.max_x = 0;
        while (lI.hasNext()){
          /*  if (PVTSFrame.stepper_mode) {
                g.setColor(Color.YELLOW);
                Polygon tmpPoly=new Polygon();
                tmpPoly.addPoint(0,old_a);
                tmpPoly.addPoint(10,old_a);
                tmpPoly.addPoint(10,old_a-10);
                tmpPoly.addPoint(15,old_a);
                tmpPoly.addPoint(10,old_a+20);
                tmpPoly.addPoint(0,old_a+20);
                g.fillPolygon(tmpPoly);
                if (old_y == FunctionManager.current_y)
                    g.fillRect(0,veryold_y-25,5000,FunctionManager.current_y - veryold_y);
                else
                    g.fillRect(0,old_y-25,5000,FunctionManager.current_y - old_y);
                g.setColor(temp);
            
            if (old_y != FunctionManager.current_y)
                veryold_y = old_y;
            old_y = FunctionManager.current_y;
            } */
            FunctionManager m = (FunctionManager) lI.next();
            m.draw(g);
            FunctionManager.current_y = FunctionManager.current_y + (int)( 50 * FunctionManager.zoom);

        }
        if (currentlySelected != null) {
            g.setColor(Color.RED);
            g.drawRoundRect(currentlySelected.xCoor,currentlySelected.yCoor,currentlySelected.width,currentlySelected.height,20,20);
            g.drawRoundRect(currentlySelected.xCoor-1,currentlySelected.yCoor-1,currentlySelected.width+2,currentlySelected.height+2,20,20);
        }
    }

   
    
    
}

/*
 * DrawingVarsPanel.java
 *
 * Created on August 22, 2006, 4:31 PM
 *
 * Author: David Pilo
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
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.*;
import java.util.*;
/**
 *

/**
 *
 * @author david
 */
public class DrawingVarsPanel extends JPanel {
    
    
    public static boolean varTableShow = true;
    public static boolean primTableShow = false;
    public static boolean procTableShow = true;
    public static boolean listTableShow = true;
    public JTable primTable = new JTable(300,1);
    public JTable procTable = new JTable(300,2);
    public JTable listTable = new JTable(300,2);
    public JTable varsTable = new JTable(300,2);
    public JLabel primLabel = new JLabel("Primitives:");
    public JLabel procLabel = new JLabel("Procedures:");
    public JLabel listLabel = new JLabel("Lists Variables:");
    public JLabel varsLabel = new JLabel("Other Variables:");
    public JPanel primPanel = new JPanel();
    public JPanel procPanel = new JPanel();
    public JPanel listPanel = new JPanel();
    public JPanel varsPanel = new JPanel();
  
    /** Creates a new instance of DrawingTreesPanel */
    public DrawingVarsPanel() {
        primPanel.add(primLabel);
        primPanel.add(primTable);
        primPanel.setLayout(new BoxLayout(primPanel, BoxLayout.PAGE_AXIS));
        procPanel.add(procLabel);
        procPanel.add(procTable);
        procPanel.setLayout(new BoxLayout(procPanel, BoxLayout.PAGE_AXIS));
        listPanel.add(listLabel);
        listPanel.add(listTable);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));
        varsPanel.add(varsLabel);
        varsPanel.add(varsTable);
        varsPanel.setLayout(new BoxLayout(varsPanel, BoxLayout.PAGE_AXIS));
        
        
       this.add(listPanel);
       this.add(varsPanel); 
       this.add(procPanel);
       //this.add(primPanel);
       
       addTableListeners();
       
       

       
    }
    public void paintComponent(Graphics g)   {
        // Paint background
        super.paintComponent(g);
        this.removeAll();
        if (listTableShow) {
            this.add(listPanel);
            drawListsTable();
        }
        
        if (varTableShow) {
            this.add(varsPanel);
            drawVariablesTable();
        }
        if (procTableShow) {
            this.add(procPanel);
            drawProceduresTable();
        }
       if (primTableShow) {
            this.add(primPanel);
            drawPrimitivesTable();
        }

        this.revalidate();
    } // paintComponent
    
    
   
    public void addTableListeners() {
        procTable.addMouseListener(new java.awt.event.MouseListener() {
           public void mouseClicked(MouseEvent mouseEvent) {
           }
           public void mouseEntered(MouseEvent mouseEvent) {
           }
           public void mouseExited(MouseEvent mouseEvent) {
           }
           public void mousePressed(MouseEvent mouseEvent) {
           }
           public void mouseReleased(MouseEvent mouseEvent) {
               JFrame f = new JFrame("Information:");
               JTextArea t = new JTextArea();
               t.setLineWrap(true);
               f.add(t);
               t.setText((procTable.getValueAt(procTable.getSelectedRow(),procTable.getSelectedColumn())).toString());
               f.setVisible(true);
               f.setLocation(java.awt.MouseInfo.getPointerInfo().getLocation());
               f.setSize(new Dimension(400,150));
           }
       });
       primTable.addMouseListener(new java.awt.event.MouseListener() {
           public void mouseClicked(MouseEvent mouseEvent) {
           }
           public void mouseEntered(MouseEvent mouseEvent) {
           }
           public void mouseExited(MouseEvent mouseEvent) {
           }
           public void mousePressed(MouseEvent mouseEvent) {
           }
           public void mouseReleased(MouseEvent mouseEvent) {
               JFrame f = new JFrame("Information:");
               JTextArea t = new JTextArea();
               t.setLineWrap(true);
               f.add(t);
               t.setText((primTable.getValueAt(primTable.getSelectedRow(),primTable.getSelectedColumn())).toString());
               f.setVisible(true);
               f.setLocation(java.awt.MouseInfo.getPointerInfo().getLocation());
               f.setSize(new Dimension(400,150));
           }
       });
       varsTable.addMouseListener(new java.awt.event.MouseListener() {
           public void mouseClicked(MouseEvent mouseEvent) {
           }
           public void mouseEntered(MouseEvent mouseEvent) {
           }
           public void mouseExited(MouseEvent mouseEvent) {
           }
           public void mousePressed(MouseEvent mouseEvent) {
           }
           public void mouseReleased(MouseEvent mouseEvent) {
               JFrame f = new JFrame("Information:");
               JTextArea t = new JTextArea();
               t.setLineWrap(true);
               f.add(t);
               t.setText((varsTable.getValueAt(varsTable.getSelectedRow(),varsTable.getSelectedColumn())).toString());
               f.setVisible(true);
               f.setLocation(java.awt.MouseInfo.getPointerInfo().getLocation());
               f.setSize(new Dimension(400,150));
           }
       });
       listTable.addMouseListener(new java.awt.event.MouseListener() {
           public void mouseClicked(MouseEvent mouseEvent) {
           }
           public void mouseEntered(MouseEvent mouseEvent) {
           }
           public void mouseExited(MouseEvent mouseEvent) {
           }
           public void mousePressed(MouseEvent mouseEvent) {
           }
           public void mouseReleased(MouseEvent mouseEvent) {
               //if (listTable.(listTable.getSelectedRow(),listTable.getSelectedColumn())) {
               JFrame f = new JFrame("Information:");
               JTextArea t = new JTextArea();
               t.setLineWrap(true);
               f.add(t);
               t.setText((listTable.getValueAt(listTable.getSelectedRow(),listTable.getSelectedColumn())).toString());
               f.setVisible(true);
               f.setLocation(java.awt.MouseInfo.getPointerInfo().getLocation());
               f.setSize(new Dimension(400,150));
              // }
           }
       });
    }
   
    
    public void drawVariablesTable () {
        varsTable.setBackground(new Color(210,210,210));
       // varsTable.selectAll();
       // varsTable.removeAll();
       // varsTable.clearSelection();
        clearTable(varsTable);
        varsTable.setValueAt("Name:",0,0);
        varsTable.setValueAt("Value:",0,1);
        LinkedList l = PVTS.envStack.getAllVariables();
        for (int i=0; i < ((LinkedList)l.get(0)).size(); i= i+2) {
            //varsTable.setSize(new Dimension(30,2));
            varsTable.setValueAt(((LinkedList)l.get(0)).get(i),i/2+1,0);
            varsTable.setValueAt(((LinkedList)l.get(0)).get(i+1),i/2+1,1);
        }

    }
    
    
     public void drawListsTable () {
        listTable.setBackground(new Color(210,210,210));
       // listTable.removeAll();
        clearTable(listTable);
        listTable.setValueAt("Name:",0,0);
        listTable.setValueAt("Value:",0,1);
        LinkedList l = PVTS.envStack.getAllLists();
        for (int i=0; i < ((LinkedList)l.get(0)).size(); i= i+2) {
            //varsTable.setSize(new Dimension(30,2));
            listTable.setValueAt(((LinkedList)l.get(0)).get(i),i/2+1,0);
            listTable.setValueAt(((LinkedList)l.get(0)).get(i+1),i/2+1,1);
        }

    }
    
    
     public void drawPrimitivesTable () {
        primTable.setBackground(new Color(210,210,210));
        clearTable(primTable);
        primTable.setValueAt("Name:",0,0);
        LinkedList l = PVTS.envStack.getAllPrimitives();
        for (int i=0; i < ((LinkedList)l.get(0)).size(); i= i+1) {
            //varsTable.setSize(new Dimension(30,2));
            primTable.setValueAt(((LinkedList)l.get(0)).get(i),i+1,0);
        }

    }
     
      public void drawProceduresTable () {
        procTable.setBackground(new Color(210,210,210));
       // procTable.selectAll();
        //procTable.clearSelection();
        clearTable(procTable);
        procTable.setValueAt("Name:",0,0);
        procTable.setValueAt("Value:",0,1);
        LinkedList l = PVTS.envStack.getAllProcedures();
        for (int i=0; i < ((LinkedList)l.get(0)).size(); i= i+2) {
            //varsTable.setSize(new Dimension(30,2));
            procTable.setValueAt(((LinkedList)l.get(0)).get(i),i/2+1,0);
            procTable.setValueAt(((LinkedList)l.get(0)).get(i+1),i/2+1,1);
        }

    }
      
      
    
    public void clearTable(JTable t) {
        for (int i = 0 ; i < t.getColumnCount(); i ++) {
            for (int j = 0; j < t.getRowCount(); j ++) {
                t.setValueAt("",j,i);
            }
        }
    }
    
    
}



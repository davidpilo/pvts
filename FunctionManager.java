/*
 * FunctionManager.java
 *
 * Created on August 30, 2006, 7:36 PM
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
import java.util.*;
import java.awt.*;
import javax.swing.*;
/**
 *
 * @author David Pilo
 */
public class FunctionManager {
    
   //public static int shiftConstant = 100;
    /** Creates a new instance of FunctionManager */
    public FunctionManager() {
       theTree = new ArrayList(); 
       for (int i = 0; i <50 ; i++) {
            this.theTree.add(i,new LinkedList());
       }
       
    }
    
    
     public void add(ProcedureAndArgs p, int i,boolean isdummy) {
            if (!isdummy)
                this.theTree.get(i).add(p);
            else
                this.theTree.get(i).add("dummy");
          
    }
     
     
     
    public ProcedureAndArgs getParent(int i, int j) {
        int numOfdummies = 0;
        for (int k = 0; k < j; k++) {
            if (!this.theTree.get(i).get(k).getClass().getName().equalsIgnoreCase("ProcedureAndArgs"))
                numOfdummies ++;
         
        }
        int temp = 0;
        int dum = numOfdummies;
        for (int l = 0; l <= dum;l++) {
            if (!this.theTree.get(i-1).get(l).getClass().getName().equalsIgnoreCase("ProcedureAndArgs")) {
                temp ++;
                dum++;
            }
                
        }
       // PVTSFrame.put("\nparent of: ("+i+", "+j+") is ");
       // PVTSFrame.put("(" + (i-1) +", "+numOfdummies+"+"+temp+")");
        return ((ProcedureAndArgs)this.theTree.get(i-1).get(numOfdummies+temp));
    } 
    
    
    public static void drawArrow(Graphics2D g2d, int xCenter, int yCenter, int x, int y, float stroke) {
      double aDir=Math.atan2(xCenter-x,yCenter-y);
      g2d.drawLine(x,y,xCenter,yCenter);
      g2d.setStroke(new BasicStroke(1f));
      Polygon tmpPoly=new Polygon();
      int i1=12+(int)(stroke*2);
      int i2=6+(int)stroke;		
      tmpPoly.addPoint(x,y);
      tmpPoly.addPoint(x+xCor(i1,aDir+.5),y+yCor(i1,aDir+.5));
      tmpPoly.addPoint(x+xCor(i2,aDir),y+yCor(i2,aDir));
      tmpPoly.addPoint(x+xCor(i1,aDir-.5),y+yCor(i1,aDir-.5));
      tmpPoly.addPoint(x,y);
      g2d.fillPolygon(tmpPoly);	
   //   g2d.setColor(Color.black);
   //   g2d.drawPolygon(tmpPoly);
   }
   private static int yCor(int len, double dir) {return (int)(len * Math.cos(dir));}
   private static int xCor(int len, double dir) {return (int)(len * Math.sin(dir));}
    
    public void drawBranch (Graphics g,ProcedureAndArgs child, ProcedureAndArgs parent) {
        g.drawLine(child.xCoor+(child.width)/2,child.yCoor,parent.xCoor+(parent.width)/2,parent.yCoor+parent.height);
        int xend = child.xCoor+(child.width)/2;
        int yend = child.yCoor;
        int xCenter = parent.xCoor+(parent.width)/2;
        int yCenter = parent.yCoor+parent.height;
        double aDir=Math.atan2(xCenter-xend,yCenter-yend);
      g.drawLine(xend,yend,xCenter,yCenter);

      Polygon tmpPoly=new Polygon();
      int i1=12;//+(int)(stroke*2);
      int i2=10;//+(int)stroke;			
      tmpPoly.addPoint(xend,yend);		
      tmpPoly.addPoint(xend+xCor(i1,aDir+.5),yend+yCor(i1,aDir+.5));
      tmpPoly.addPoint(xend+xCor(i2,aDir),yend+yCor(i2,aDir));
      tmpPoly.addPoint(xend+xCor(i1,aDir-.5),yend+yCor(i1,aDir-.5));
      tmpPoly.addPoint(xend,yend);			
    //  g.drawPolygon(tmpPoly);
      g.fillPolygon(tmpPoly);	
    }
     

    public void draw (Graphics g) {
        ProcedureAndArgs current;
        int temp_y = current_y;
        x = -1;
        y = -1;
        int max_x_panel = PVTSFrame.ftrees.getSize().width - ((int)(100*zoom));
        //PVTSFrame.put(max_x_panel);
        int num_of_calls; 
        int coeff; 
        for(int i = 0; i < this.theTree.size(); i++) {
            if (this.theTree.get(i).size() != 0) {
                y++;
                x = -1;
                for (int j = 0; j <this.theTree.get(i).size(); j++) {
                    x++;
                    if (this.theTree.get(i).get(j).getClass().getName().equalsIgnoreCase("ProcedureAndArgs")) {
                        current = ((ProcedureAndArgs)this.theTree.get(i).get(j));
                        if (viewMode ==3)
                            current.xCoor = (int) (80*x*zoom);
                        if (viewMode == 2)
                            current.xCoor = (int) (80*j*zoom);
                        if (viewMode == 0) {
                            num_of_calls = this.theTree.get(i).size();
                            coeff = max_x_panel / (num_of_calls);
                            if (j == 0 && i == 0)
                                current.xCoor = max_x_panel/2;
                            else
                                current.xCoor = coeff * (j+1);
                            
                        }
                        if (viewMode == 1) {
                            
                            num_of_calls = this.theTree.get(i).size();
                            coeff = max_x_panel / (num_of_calls);
                            if (j == 0 && i ==0)
                                current.xCoor = max_x_panel/2;
                            else
                                current.xCoor = coeff * (x+1);
                            
                        }
                        current.yCoor = (int) ((90*y)*zoom)+ temp_y;
                        current.width = (int) (70*zoom);
                        current.height = (int) (25*zoom);
                        if (current.yCoor > current_y)
                            current_y = current.yCoor + 50;
                        if (current.xCoor > max_x)
                            max_x = current.xCoor + 50;
                        g.setColor(Color.gray);
                        g.fillRoundRect(current.xCoor+5,current.yCoor+4,current.width,current.height,20,20);
                        g.setColor(DrawingTreesPanel.color);
                        if (i == 0)
                            g.setColor(DrawingTreesPanel.color.darker());
                        g.fillRoundRect(current.xCoor,current.yCoor,current.width,current.height,20,20);
                        //g.fillOval(current.xCoor,current.yCoor,current.width,current.height);
                        g.setColor(Color.black);
                        //g.drawOval(current.xCoor,current.yCoor,current.width,current.height);
                        g.drawRoundRect(current.xCoor,current.yCoor,current.width,current.height,20,20);
                        String s;
                        if (funcNameDisplay)
                            s = current.p.substring(0,1) + current.sexprs.toString();
                        else
                            s = current.p + current.sexprs.toString();
                        int l = s.length();
                        if (l*7 > (current.width))
                            s = s.substring(0,l-(l*7-current.width)/7);
                        if ((current.result.isListCC() || current.result.isAtom()) && result_bubble) {
                            g.drawString(current.result.toString(), current.xCoor + current.width + ((int)(30*zoom)) , current.yCoor+((current.height)/2)+6);
                            Color temp = g.getColor();
                            g.setColor(Color.red);
                            Polygon p = new Polygon();
                            p.addPoint(current.xCoor+current.width+((int)(8*zoom)),current.yCoor+((int)current.height/3));
                            p.addPoint(current.xCoor+current.width+((int)(14*zoom)),current.yCoor+((int)current.height/3));
                            p.addPoint(current.xCoor+current.width+((int)(14*zoom)),current.yCoor+((int)current.height/5));
                            p.addPoint(current.xCoor+current.width+((int)(25*zoom)),current.yCoor+((int)current.height/2));
                            p.addPoint(current.xCoor+current.width+((int)(14*zoom)),current.yCoor+((int)current.height*4/5));
                            p.addPoint(current.xCoor+current.width+((int)(14*zoom)),current.yCoor+((int)current.height/3*2));
                            p.addPoint(current.xCoor+current.width+((int)(8*zoom)),current.yCoor+((int)current.height/3*2));
                            g.fillPolygon(p);
                            g.setColor(Color.black);
                            g.drawPolygon(p);
                            g.setColor(temp);
                            //g.drawLine(current.xCoor + current.width + 3, current)
                        }
                        g.drawString(s,current.xCoor+5,current.yCoor+((current.height)/2)+6);
                        if (i != 0) { // draw child to parent branch if not root
                            ProcedureAndArgs parent = getParent(i,j);
                            drawBranch(g,current,parent);
                        }
                        
                    }
                    else {
                        x--;
                    }
                }
            }
        }
    }

    public ArrayList<LinkedList> theTree;
   // public ArrayL
    public int x;
    public int y;
    public static int max_x;
    public static int current_y;
    public static double zoom = 1.0;
    public static int viewMode = 0;
    public static boolean funcNameDisplay = true;
    public static boolean result_bubble = true;

}

/* 
 *
 * Arrow.java
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


import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Arrow {
    Conscell conscell;
    SExpression sexpr;
    static int antiColVarVARIANCE = 20;
    int my_antiColVar;
    int unitDraw = DrawingConscellsPanel.zoom;
    int startX, startY, endX, endY;
    static int antiColVar = 0;
    static Random ran = new Random();
    
    Arrow(Conscell c, SExpression s) {
        conscell = c;
        sexpr = s;
        updateantiColVar();
        my_antiColVar = antiColVar;
        ArrowDB.addAndChangeColVar(this);
    }
    
    public int getColVar() {
        return my_antiColVar;
    }
    
    public void setColVar(int i) {
        my_antiColVar = i;
    }
    
    public static void updateantiColVar() {
        antiColVar = ran.nextInt(antiColVarVARIANCE) - antiColVarVARIANCE/2;
        //TextIO.putln("ran =" + my_antiColVar);
    }
    
    
    public void setXYs(int sX, int sY, int eX, int eY) {
        startX = sX;
        startY = sY;
        endX = eX;
        endY = eY;
    }
    
    public int getsX() {
        return startX;
    }
    
    public int getsY() {
        return startY;
    }
    
    public int geteX() {
        return endX;
    }
    
    public int geteXY() {
        return endY;
    }
    
    
    public void drawCdr(Graphics g) {
        //updatemy_antiColVar();
        if (conscell.getY() == sexpr.getY()) {
            setXYs(conscell.getX() + (int)(1.5 * unitDraw),conscell.getY() - (int) (0.5 * unitDraw),sexpr.getX(),sexpr.getY()-(int)(0.5 * unitDraw));
            g.drawLine(startX,startY,endX,endY);
            g.drawLine(endX-3,endY-3,endX,endY);
            if (startX == endX) // then vertical line
                g.drawLine(endX+3,endY-3,endX,endY);
            else
                g.drawLine(endX-3,endY+3,endX,endY);
        } else // that is the case when the conscells are not in the same "horizontal" line.
        {
            g.drawLine(conscell.getX() + (int)(1.5 * unitDraw),conscell.getY() - (int) (0.5 * unitDraw),conscell.getX() + (int)(2.5 * unitDraw),conscell.getY() - (int) (0.5 * unitDraw));
            int tempX = sexpr.getX() - conscell.getX();
            int tempY = sexpr.getY() - conscell.getY();
            if (tempY > 0)
                tempY = tempY - unitDraw;
            else
                tempY = tempY + unitDraw;
            g.drawLine(conscell.getX() + (int)(2.5 * unitDraw),conscell.getY() - (int) (0.5 * unitDraw), conscell.getX() + (int)(2.5 * unitDraw), conscell.getY() - (int) (0.5 * unitDraw) + tempY+my_antiColVar);
            g.drawLine(conscell.getX() + (int)(2.5 * unitDraw), conscell.getY() - (int) (0.5 * unitDraw) + tempY+my_antiColVar, conscell.getX() + (int)(2.5 * unitDraw) + tempX - (3 * unitDraw), conscell.getY() - (int) (0.5 * unitDraw) + tempY+my_antiColVar);
            int temp = 0;
            if (tempY > 0)
                temp = unitDraw;
            else
                temp = (0 - unitDraw);
            g.drawLine(conscell.getX() + (int)(2.5 * unitDraw) + tempX - (3 * unitDraw), conscell.getY() - (int) (0.5 * unitDraw) + tempY+my_antiColVar, conscell.getX() + (int)(2.5 * unitDraw) + tempX - (3 * unitDraw), conscell.getY() - (int) (0.5 * unitDraw) + tempY + temp);
            setXYs(conscell.getX() + (int)(2.5 * unitDraw) + tempX - (3 * unitDraw), conscell.getY() - (int) (0.5 * unitDraw) + tempY + temp, conscell.getX() + (int)(2.5 * unitDraw) + tempX - (3 * unitDraw) + (unitDraw / 2), conscell.getY() - (int) (0.5 * unitDraw) + tempY + temp);
            g.drawLine(startX,startY,endX,endY);
           // g.drawLine(endX-3,endY-3,endX,endY);
           // g.drawLine(endX-3,endY+3,endX,endY);
           

               
            
        }
        
         Polygon tmpPoly=new Polygon();		
                tmpPoly.addPoint(endX,endY);				
                tmpPoly.addPoint(endX-3,endY-3);			
                tmpPoly.addPoint(endX-3,endY+3);
                tmpPoly.addPoint(endX,endY);			
                g.drawPolygon(tmpPoly);
                g.fillPolygon(tmpPoly);	
    }
    public void drawCar(Graphics g) {
        int sx,sy,ex,ey;
        //updatemy_antiColVar();
        //int tempX,tempY;
        if (sexpr.getClass().getName().equals("ListCC") && (!(((ListCC) sexpr).isEmpty()))) {
            //TextIO.putln("its a list");
            sx = conscell.getX() + (int)(0.5 * unitDraw);
            sy = conscell.getY() - (int) (0.5 * unitDraw);
            ex = ((ListCC) sexpr).getFirst().getX() + (int)(0.5 * unitDraw);
            ey = ((ListCC) sexpr).getFirst().getY() - (int) (0.5 * unitDraw);
        } else {
            sx = conscell.getX() + (int) (0.5 * unitDraw);
            sy = conscell.getY() - (int) (0.5 * unitDraw);
            ex = sexpr.getX() + (int) (0.5 * unitDraw);
            ey = sexpr.getY() - (int) (0.5 * unitDraw);
        }
        
        // int temp = 0;
        if (ey == sy)
            my_antiColVar = 0;
        
        if ((ey - sy) > 0) {
            g.drawLine(sx,sy,sx,ey - (unitDraw)+my_antiColVar);
            g.drawLine(sx,ey - (unitDraw)+my_antiColVar, ex, ey - (unitDraw)+my_antiColVar);
            setXYs(ex, ey - (unitDraw)+my_antiColVar,ex,ey - (unitDraw/2));
            g.drawLine(startX,startY,endX,endY);
          //  g.drawLine(endX-3,endY-3,endX,endY);
            //if (startX == endX) // then vertical line
          //  g.drawLine(endX+3,endY-3,endX,endY);
            Polygon tmpPoly=new Polygon();		
                tmpPoly.addPoint(endX,endY);				
                tmpPoly.addPoint(endX+3,endY-3);			
                tmpPoly.addPoint(endX-3,endY-3);
                tmpPoly.addPoint(endX,endY);			
                g.drawPolygon(tmpPoly);
                g.fillPolygon(tmpPoly);	
            // else
            //   g.drawLine(endX-3,endY+3,endX,endY);
        } else {
            g.drawLine(sx,sy,sx,sy - (unitDraw)+my_antiColVar);
            if (ex > sx)
                g.drawLine(sx,sy-(unitDraw)+my_antiColVar,ex-(unitDraw)+my_antiColVar,sy-(unitDraw)+my_antiColVar);
            else {
                g.drawLine(sx,sy-(unitDraw)+my_antiColVar,ex,sy-(unitDraw)+my_antiColVar);
                g.drawLine(ex,sy-(unitDraw)+my_antiColVar, ex-(unitDraw)+my_antiColVar,sy-(unitDraw)+my_antiColVar);
            }
            g.drawLine(ex-(unitDraw)+my_antiColVar,sy-(unitDraw)+my_antiColVar, ex - unitDraw+my_antiColVar, ey - unitDraw);
            g.drawLine(ex - unitDraw+my_antiColVar, ey - unitDraw, ex, ey -unitDraw);
            setXYs(ex, ey -unitDraw,ex,ey - (unitDraw/2));
            g.drawLine(startX,startY,endX,endY);
           // g.drawLine(endX-3,endY-3,endX,endY);
            //if (startX == endX) // then vertical line
           // g.drawLine(endX+3,endY-3,endX,endY);
            Polygon tmpPoly=new Polygon();		
                tmpPoly.addPoint(endX,endY);				
                tmpPoly.addPoint(endX+3,endY-3);			
                tmpPoly.addPoint(endX-3,endY-3);
                tmpPoly.addPoint(endX,endY);			
                g.drawPolygon(tmpPoly);
                g.fillPolygon(tmpPoly);	
        }
        
        
    }
    
}
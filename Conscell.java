/* 
 *
 * Conscell.java
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

public class Conscell extends SExpression {
    int unitDraw = DrawingConscellsPanel.zoom;
    SExpression car;
    Conscell cdr;
    SExpression dotted;
    
    
    public Conscell() {
        car = null;
        cdr = null;
        dotted = null;
        x = 0;
        y = 0;
    }
    
    public Conscell (SExpression e1, SExpression e2) { 
        car = e1;
        dotted = e2;
        cdr = null;
        x = 0;
        y = 0;
    }
    
    public Conscell(SExpression cr) {
        car = cr;
        cdr = null;
        dotted = null;
        x = 0;
        y = 0;
    }
    
    public Conscell copy() {
        Conscell temp = new Conscell();
        temp.setCar(this.getCar().copy());
        if (this.getDotted() != null)
            temp.setDotted(this.getDotted().copy());
        if (!(this.getCdr() == null))
            temp.setCdr(this.getCdr().copy());
        //TextIO.put("done conscell copy");
        return temp;
    }
    
    
    
  /*  public Conscell (SExpression cr, Conscell cd)
   {
   car = cr;
   cdr = cd;
   x = 0;
   y = 0;
   }*/
    
    
    public String toString() {
        return car.toString();
    }
    
    // May not need this one
    boolean hasSuccessor() {
        return (cdr != null);
    }
    
    boolean isDottedPair() {
        return (dotted != null);
    }
    
    void setCar(SExpression c) {
        car = c;
    }
    
    void setCdr(Conscell c) {
        cdr = c;
    }
    
    void setDotted (SExpression c) {
        dotted = c;
    }
    
    SExpression getDotted() {
        return dotted;
    }
    
    SExpression getCar() {
        return car;
    }
    
    Conscell getCdr() {
        return cdr;
    }
    
    
    void print() {
        if (this.isDottedPair()) {
            car.print();
            dotted.print();
        }
        else
            car.print();
    }
    
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    
    
    
    public void draw(Graphics g) {
        Color temp = g.getColor();
        g.setColor(DrawingConscellsPanel.color);
        g.fillRect(x,y-unitDraw,(2*unitDraw),(unitDraw));
        g.setColor(temp);
        g.drawLine( x, y, x+(2*unitDraw), y);
        g.drawLine( x+(2*unitDraw), y, x+(2*unitDraw), y-unitDraw);
        g.drawLine( x+(2*unitDraw), y-unitDraw, x, y-unitDraw);
        g.drawLine( x, y-unitDraw, x, y);
        g.drawLine( x+unitDraw, y, x+unitDraw, y-unitDraw);
        
        if (this.getCar() != null) {
            Arrow toCar = new Arrow(this,this.getCar());
            toCar.drawCar(g);
            this.getCar().draw(g);
        }
        if (this.getCdr() == null && !this.isDottedPair())
        {
            drawNull(g,this);
        }
        if (this.isDottedPair()) {
            g.drawLine(x+unitDraw+(unitDraw/2),y-(unitDraw/2),x+unitDraw+(unitDraw/2),y+unitDraw);
           // g.drawLine(x+unitDraw+(unitDraw/2),y+unitDraw,x+unitDraw+(unitDraw/2)-3,y+unitDraw-3);
           // g.drawLine(x+unitDraw+(unitDraw/2),y+unitDraw,x+unitDraw+(unitDraw/2)+3,y+unitDraw-3);
            Polygon tmpPoly=new Polygon();		
                tmpPoly.addPoint(x+unitDraw+(unitDraw/2),y+unitDraw);				
                tmpPoly.addPoint(x+unitDraw+(unitDraw/2)-3,y+unitDraw-3);			
                tmpPoly.addPoint(x+unitDraw+(unitDraw/2)+3,y+unitDraw-3);
                tmpPoly.addPoint(x+unitDraw+(unitDraw/2),y+unitDraw);			
                g.drawPolygon(tmpPoly);
                g.fillPolygon(tmpPoly);	
            dotted.setXY(x+unitDraw,y+unitDraw+unitDraw);
            dotted.draw(g);
        }
        else { 
            if (this.getCdr() != null) {
            Arrow toCdr = new Arrow(this, this.getCdr());
            toCdr.drawCdr(g);
            this.getCdr().draw(g);
            }
        }
        
    }
    
    public void drawNull(Graphics g, Conscell c) {
        
        g.drawLine(c.getX() + unitDraw,c.getY(),c.getX() + (2*unitDraw), c.getY() - unitDraw);
    }
    
    
} //END conscell
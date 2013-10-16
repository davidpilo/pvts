/*
 * ListCC.java
 *
 * author: David Pilo
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

class ListCC extends SExpression {
    Conscell first,last;
    int size;
    int unitDraw = DrawingConscellsPanel.zoom;
    
    ListCC() {
        first = null;
        last = null;
        size = 0;
    }
    
    public ListCC copy() {
        if (isEmpty()) {
            //TextIO.put("got the emptylist");
            return new ListCC();} else {
            ListCC temp = new ListCC();
            temp.first = this.first.copy();
            // temp.first.setCar(car().copy());
            // temp.first.setCdr(cdr().first.copy());
            //   TextIO.put("done list copy");
            // have to traverse the list and set the last!
            int temp_size = 1;
            ListCC c = temp;
            while (!(c.cdr().isEmpty())) {
                c = c.cdr();
                temp_size ++;
            }
            temp.last = c.first;
            temp.size = temp_size;
            return temp;
            
            }
        
        
    }
    
    public Conscell getFirst() {
        return first;
    }
    
    public Conscell getLast() {
        return last;
    }
    
    public int getSize() {
        return size;
    }
    
    ListCC(Conscell c) {
        first = c;
        last = c;
        size = 1;
    }
    
    
    
    
    
    public void print() {
        PVTSFrame.put("(");
        Conscell current = first;
        for(int i = 0;i<size;i++) {
            if (i > 0) PVTSFrame.put(" ");
            current.getCar().print();
            current = current.getCdr();
        }
        if (last != null) {
            if (last.isDottedPair()) {
                PVTSFrame.put(" . ");
                last.getDotted().print();
            }
        }
        PVTSFrame.put(")");
    }
    
    public String toString() {
        String str = "(";
        Conscell current = first;
        for(int i = 0;i<size;i++) {
            if (i > 0) str = str + " ";
            str = str + current.getCar().toString();
            current = current.getCdr();
        }
        if (last != null) {
            if (last.isDottedPair()) {
                str = str + " . ";
                str = str + last.getDotted().toString();
            }
        }
        str = str +")";
        return str;
    }
    
    
    public String toStringList() {
        String str = "'( ";
        Conscell current = first;
        for(int i = 0;i<size;i++) {
            if (i > 0) str = str + " ";
            str = str + current.getCar().toString();
            current = current.getCdr();
        }
        if (last != null) {
            if (last.isDottedPair()) {
                str = str + " . ";
                str = str + last.getDotted().toString();
            }
        }
        str = str +")";
        return str;
    }
    
    public SExpression car() {
        return first.getCar();
    }
    
    
    
    public ListCC cdr() {
        ListCC lc = new ListCC(first.getCdr());
        lc.last = last;
        lc.size = size - 1;
        return lc;
    }
    

    
    public void removeAllButFirst() {
        last = first;
        first.dotted = null;
        size = 1;
    }
    
    public void add(Conscell c) {
        if (first == null) {
            first = c;
            last = c;
            size = 1;
        } else {
            last.setCdr(c);
            last = c;
            size = size + 1;
        }
    }
    
    public void addL(ListCC l) {
        if (first == null) {
            first = l.getFirst();
            last = l.getLast();
            size = l.getSize();
        } else {
            last.setDotted(null);
            last.setCdr(l.getFirst());
            last = l.getLast();
            size = size + l.getSize();
        }
    }
    
    
    public static ListCC makeReverseList(ArrayList<SExpression> sexprs) {
        ListCC myList = new ListCC();
        //   Conscell last = null;
        ArrayList<SExpression> l = new ArrayList();
        Conscell current = ((ListCC) sexprs.get(0)).getFirst();
        for(int i = 0 ; i < ((ListCC) sexprs.get(0)).size; i++) {
            l.add(current.getCar());
            current = current.getCdr();
        }
        for (int i = 0; i < l.size(); i++) {
            // TextIO.put(sxpr.toString()+" *** ");
            Conscell c = new Conscell(l.get(l.size()-i-1));
            myList.add(c);
        }
        return myList;
    }
    
    public static ListCC makeList(ArrayList<SExpression> sexprs) {
        // if (sexprs.size() == 0)
        //TextIO.put("enter makelist");
        ListCC myList = new ListCC();
        //   Conscell last = null;
        for (SExpression sxpr : sexprs) {
            // TextIO.put(sxpr.toString()+" *** ");
            Conscell c = new Conscell(sxpr);
            myList.add(c);
        }
        return myList;
    }
    
     public static ListCC makeListDotted(ArrayList<SExpression> sexprs) {
        // if (sexprs.size() == 0)
        int s = sexprs.size();
        ListCC myList = new ListCC();
        //   Conscell last = null;
        for (int i= 0; i <s-2;i++) {
            Conscell c = new Conscell(sexprs.get(i));
            myList.add(c);
        }
        Conscell last = new Conscell(sexprs.get(s-2),sexprs.get(s-1));
        myList.add(last);
        return myList;
    }
    
    
    public boolean isSameListAs(ListCC l) {
        if (this.first == l.first)
            return true;
        else
            return false;
    }
     
     
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    
    
    public boolean isEmpty() {
        //return (size == 0); // may want to use first == null
        return (first == null);
    }
    
    
    
    public void draw(Graphics g) {
        if (this.isBounded()) {
            int nameLength = this.getNameObj().getName().length();
            g.drawString(this.getNameObj().getName(),name_obj.x-(8*unitDraw),name_obj.y);
            Conscell dummy = new Conscell();
            dummy.setXY(name_obj.x-(9*unitDraw)+(7*nameLength),name_obj.y);
            Arrow a = new Arrow(dummy,this);
            a.drawCdr(g);
        }
        if (isEmpty())
            drawEmptyList(g);
        else {
            Conscell current = first;
            current.draw(g);
        }
    }
    
    
    
    public void drawEmptyList(Graphics g) {
        Color temp = g.getColor();
        g.setColor(DrawingConscellsPanel.color);
        g.fillRect(x,y-unitDraw,unitDraw,unitDraw);
        g.setColor(temp);
        g.drawLine( x, y, x+(unitDraw), y);
        g.drawLine( x+(unitDraw), y, x+(unitDraw), y-unitDraw);
        g.drawLine( x+(unitDraw), y-unitDraw, x, y-unitDraw);
        g.drawLine( x, y-unitDraw, x, y);
        g.drawLine( x, y, x+unitDraw, y-unitDraw);
    }
    
}


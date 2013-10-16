/*
 * SEBool.java
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

class SEBool extends Atom {
    String bool;
    
    SEBool(String b) {
        bool = b;
        x = 0;
        y = 0;
    }
    
    void print() {
        PVTSFrame.put(bool);
    }
    
    public SEBool copy() {
        return new SEBool(this.bool);
    }
    
    public void setTrue() {
        bool = "#t";
    }
    
    public void setFalse() {
        bool = "#f";
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public String getPrintable() {
        return bool;
    }
    
    public Boolean boolValue() {
        if (bool.equals("#f"))
            return false;
        else
            return true;
    }
    
    
    public void draw(Graphics g) {
        g.drawString(""+ bool,x,y);
    }
    
}
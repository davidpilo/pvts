/*
 * ErrorMess.java
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

class ErrorMess extends SExpression {
    
    static String allmessages;
    String message;
    static int pos;
    
    public ErrorMess(String st) {
        pos = TextIO.getPos();
        PVTS.error = true;
        message = st;
        allmessages = allmessages + "\nat Line "+PVTS.currentLine+": " + st;
    }
    
    public ErrorMess copy() {
        return new ErrorMess(this.message);
    }
    
    
    
    public void print() {
        if (PVTS.debug)
            PVTSFrame.put(message);
    }
    
    
    
    public void draw(Graphics g) {
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
}
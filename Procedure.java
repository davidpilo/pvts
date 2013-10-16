/*
 * Procedure.java
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

class Procedure extends Function {

    ArrayList<SESymbol> parameters;
    ArrayList<SExpression> body;
    Stack<Hashtable> environments;
    boolean isVariableNumberOfParameters = false;
    
    String parametersToString() {
        String s = "";
        for (int i = 0; i < parameters.size(); i++) {
            s = s+parameters.get(i)+" ";
        }
        return s;
    }
    
    Procedure copyReturn () {
        Procedure p = new Procedure(this.op);
        p.parameters = this.parameters;
        p.body = this.body;
        return p;
    }
    
    Procedure(String s) {
        parameters  = new ArrayList();
        environments = new Stack();
        op = s;
        body = new ArrayList();
        x = 0;
        y = 0;
        isVariableNumberOfParameters = false;
    }
    
    
   /* void setBody (SExpression b) {
        body = b;
    }*/
    
    
    
  
   
    void addToBody (SExpression b) {
        body.add(b);
    }
    
    public String toString() {
        String temp = "";    
    
        for (int i = 0; i<body.size(); i++) {
            temp = temp + body.get(i).toString() + " ";
        
        }
        return temp;
    }
    
    void addParameter(SESymbol s) {
        if (parameters.contains(s)) {
            PVTSFrame.put ("define: duplicate argument identifier in: "+s);
            PVTS.error = true;
        }
        else {
            parameters.add(s);
        }
  
    }
    
    void print() {
        PVTSFrame.put("#<procedure:"+op+">");
    }
    
    public SEBool copy() {
        return new SEBool(this.op);
    }
    
    public void setOp(String st) {
        op = st;
    }
    
    public String getOp() {
        return op;
    }
    
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    
    
    
    
    public void draw(Graphics g) {
        g.drawString(""+ op,x,y);
    }
    
}
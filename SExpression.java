/*
 * SExpression.java
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


abstract class SExpression {
    int x,y;
    
    SEName name_obj = new SEName();
    public boolean managed = false;
    // abstract String getName();
    // abstract void setName(String n);
    abstract int getX();
    abstract int getY();
    abstract void draw(Graphics g);
    abstract void print();
    abstract SExpression copy();
    public String toString() { return "SExpression!";}
    
    
    public Boolean isListCC() {
        return this.getClass().getName().equalsIgnoreCase("ListCC");
    }
    
    public Boolean isReal() {
        return (this.getClass().getName().equals("SEInt") || this.getClass().getName().equals("SEFloat"));
    }
    

    
    public Boolean isProperList() {
        if (this.getClass().getName().equalsIgnoreCase("ListCC")) {
            if (((ListCC) this).isEmpty())
                return true;
            if (!((ListCC) this).last.isDottedPair())
                return true;
            else
                return false;
        }
        else
            return false;
                
    }
    
    public Boolean isAtom() {
        return (this.getClass().getName().equals("SEInt") || this.getClass().getName().equals("SEBool") || this.getClass().getName().equals("SESymbol")
        || this.getClass().getName().equals("Primitive") || this.getClass().getName().equals("Procedure") 
         || this.getClass().getName().equals("SEString") || this.getClass().getName().equals("SEFloat") || this.getClass().getName().equals("SEChar"));
    }
    
    public Boolean isSEString() {
        return this.getClass().getName().equals("SEString");
    }
    
    public Boolean isSEChar() {
        return this.getClass().getName().equals("SEChar");
    }
    
    public Boolean isFunction() {
        return (this.getClass().getName().equals("Primitive") || this.getClass().getName().equals("Procedure") 
        || this.getClass().getName().equals("Function"));
    }
    
    public Boolean isSENumber() {
        return (this.getClass().getName().equals("SEInt") || this.getClass().getName().equals("SEFloat"));
    }
    
    public static Boolean isPrimitive(String st) {
        return (st.equals("+") || st.equals("-") || st.equals("/") || st.equals("*") ||
                st.equals(">") || st.equals("<") || st.equals("=") || st.equals("<=") ||
                st.equals(">="));
    }
    
     public Boolean isPrimitiveG() {
        return  this.getClass().getName().equals("Primitive");
    }
     
      public Boolean isProcedure() {
        return  this.getClass().getName().equals("Procedure");
    }
    
    public Boolean isSEFloat(){
        return this.getClass().getName().equals("SEFloat");
    }
    
    public Boolean isSEInt(){
        return this.getClass().getName().equals("SEInt");
    }
    
    public Boolean isSESymbol() {
        return this.getClass().getName().equalsIgnoreCase("SESymbol");
    }
    
    public SEName getNameObj() {
        return name_obj;
    }
    
    
    
    public void setXY(int a, int b) {
        if (!managed) {
            x = a;
            y = b;
        }
    }
    
    public void setX(int n) {
        if (!managed) {
            x = n;
        }
    }
    
    public void setY(int n) {
        if (!managed) {
            y = n;
        }
    }
    
    boolean isBounded() {
        return name_obj.hasAName();
    }
    
    
    
}


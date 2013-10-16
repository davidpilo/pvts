/*
 * SEName.java
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

import java.util.ArrayList;

public class SEName {
    int environmentLevel;
    ArrayList<String> nameAL;
    // String exp_name;
    int x, y;
    boolean managed;
    
    public SEName() {
        environmentLevel = -1; //this var should never be left unmodified.
        nameAL = new ArrayList();
        managed = false;
    }
    public void setXY(int a, int b) {
        if (!managed) {
            x = a;
            y = b;
        }
    }
    
    public void setEnvironmentLevel (int i) {
        environmentLevel = i;
    }
    
    public void setX(int n) {
        if (!managed) {
            x = n;
        }
    }
    
    public boolean hasAName() {
        return (!nameAL.isEmpty());
    }
    
    public void setY(int n) {
        if (!managed) {
            y = n;
        }
    }
    
    
    public void addName(String n) {
        if (!nameAL.contains(n)) {
            nameAL.add(n);
        }
    }
    
    public void removeName(String n) {
        if (nameAL.contains(n)) {
            nameAL.remove(n);
        }
        
    }
    
    public String getName() {
        String temp = "";
        if (hasAName()) {
            if (environmentLevel != 0)
                temp = temp + "E"+environmentLevel+": ";
            else 
                temp = temp + "GE: ";
        }
        int l;
        for (String st: nameAL) {
            temp = temp + st +",";
        }
        l = temp.length();
        return temp.substring(0,l-1);
    }
    
    
}
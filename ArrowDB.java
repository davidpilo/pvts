/* 
 *
 * ArrowDB.java
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


class ArrowDB {
    static ArrayList<Arrow> arrowArray = new ArrayList();
    
    ArrowDB() {
        
    }
    
    
    public static void addAndChangeColVar(Arrow a) {
        boolean contained = false;
        for (Arrow ar: arrowArray) {
            if (sameArrow(a, ar)) {
                a.setColVar(ar.getColVar());
                contained = true;
            }
        }
        if (!contained)
            arrowArray.add(a);
    }
    
    
    
    public static boolean sameArrow(Arrow a, Arrow b) {
        //return (a.conscell == b.conscell  && a.sexpr == b.sexpr);
        return (a.conscell.x == b.conscell.x && a.conscell.y == b.conscell.y && a.sexpr.x == b.sexpr.x && a.sexpr.y == b.sexpr.y);
    }
    
    
    
}
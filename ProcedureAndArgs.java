/*
 * ProcedureAndArgs.java
 *
 * Created on September 7, 2006, 8:27 PM
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
/**
 *
 * @author david
 */
public class ProcedureAndArgs {
    
    /** Creates a new instance of ProcedureAndArgs */
    public ProcedureAndArgs() {
    }
     public String p;
        public ArrayList<SExpression> sexprs;
        //public int index;
        public int xCoor;
        public int yCoor;
        public int height;
        public int width;
        public SExpression result;
        public String display;
       
        //SExpression result;
        ProcedureAndArgs(String pro , ArrayList<SExpression> args) {
            this.sexprs = args;
            this.p = pro;
}
}

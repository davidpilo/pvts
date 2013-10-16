/*
 * ListMatrix.java
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


import java.util.*;
/**
 * A class for storing and operating on two dimensional matrices that will
 * hold LinkedList that will hold ListCC's
 */
public class ListMatrix {
    /**
   Construct a Matrix object
   @arg rows the number of rows in the matrix
   @arg cols the number of rows in the matrix
     */
    public ListMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        elements = new LinkedList[rows][cols];
    }
    
    /**
   Get the number of rows in this matrix
   @return the number of rows in the matrix
     */
    public int getRows() {
        return rows;
    }
    
    /**
   Get the number of columns in this matrix
   @return the number of columns in the matrix
     */
    public int getCols() {
        return cols;
    }
    
    /**
   Get the value of a particular element from this matrix
   @arg r the row of the element to get
   @arg c the column of the element to get
   @return the value of the specified element from the matrix
     */
    public LinkedList get(int r, int c) {
        return elements[r][c];
    }
    
    /**
   Set the value of a particular element from this matrix
   @arg r the row of the element to get
   @arg c the column of the element to get
   @arg v the value to store in the specified location
     */
    public void set(int r, int c, LinkedList v) {
        elements[r][c] = v;
    }
    
    
    public static ListMatrix emptyMatrix(int rows, int cols) {
        ListMatrix result = new ListMatrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result.set(i,j,new LinkedList());
        
        return result;
    }
    
    
    
    public void printMatrix() {
        TextIO.putln();
        for (int i=0; i < rows; i++) {
            for(int j=0; j < cols; j++) {
                if (j==0) TextIO.put("[");
                //TextIO.put((int) get(i,j));
                TextIO.putln(get(i,j).toString());
                if (j==cols-1) TextIO.putln("]");
                else
                    TextIO.put(" ");
            }
        }
        TextIO.putln(" ");
    }
    
    
    private LinkedList[][] elements;
    private int rows, cols;
}
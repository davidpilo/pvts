/*
 * ListManager.java
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

/* ListManager Class by David Pilo Mansion.
 *
 *
 * The algorithm will store in a matrix the lists. If an SEInt or a cons-cell belonging to list x is in the
 * (i,j) spot then the reference to that list will be stored in the (i,j) element of the matrix.
 * If there is a collision between an SEInt and a cons-cell, the list to which the cons-cell belongs to will be shifted down one spot,
 * if the collision happens between two SEInts or two cons-cells then the oldest list (the furthest left) will be shifted down.
 * The Algorithm will repeat the process until no collisions are found.
 *
 */

class ListManager {
    int xMax;
    int yMax;
    int xOffset;
    int yOffset;
    int xStep = 32;
    int yStep = 48;
    int MAX = 30;
    ListCC tList;
    SExpression current;
    SExpression cu;
    ListMatrix listMatrix;
    LinkedList orderOfLists;
    
    ListManager(ListCC l) {
        // int xOffset = xOffsetIn;
        //  int yOffset = yOffsetIn;
        xMax = 0;
        yMax = 0;
        tList = l;
        current = tList;
        cu = tList;
        listMatrix = ListMatrix.emptyMatrix(MAX,MAX);
        orderOfLists = new LinkedList();
    }
    
    public ListCC getListCC() {
        return tList;
    }
    
    public void setOffsets(int a, int b) {
        xOffset = a;
        yOffset = b;
    }
    
    public void manage() {// Creates the initial matrix and manages the collisions until there is no more.
        
        
        createInitMatrix(current,0,0,null);
        // TextIO.putln(orderOfLists.toString());
        // listMatrix.printMatrix();
       while (!(detectCollision() == null)) {
            shiftListCC(detectCollision());
            //TextIO.putln(orderOfLists.toString());
            //listMatrix.printMatrix();
        }
        setManagedTrue(cu);
        
    }
    
    
    public void setManagedTrue(SExpression cu) {
        if (cu != null) {
            cu.managed = true;
            if (cu.getClass().getName().equals("ListCC")) {
                ((ListCC) cu).name_obj.managed = true;
                if (!(((ListCC) cu).isEmpty()))
                    setManagedTrue(((ListCC) cu).getFirst());
                //   setManagedTrue(((ListCC)cu).car());
                //   setManagedTrue(((ListCC)cu).cdr());
            }
            if (cu.getClass().getName().equals("Conscell")) {
                setManagedTrue(((Conscell)cu).getCar());
                setManagedTrue(((Conscell)cu).getCdr());
            }
        }
    }
    
    public ListMatrix getListMatrix() {
        return listMatrix;
    }
    
    
    public boolean samePos(SExpression a, SExpression b) {// return true if two Conscells and/or SEInts have the same position
        return ((a.getX() == b.getX()) && (a.getY() == b.getY()));
    }
    
    
    public void shiftListCC(SExpression l) {// Shifts (in the matrix and the canvas) a whole list "l" down one spot.
        // that is, it moves all the elements of "l" one spot down.
        if (l != null) {
            // if (l.getClass().getName().equals ("SEInt"))
            if (l.isAtom()) {
                shiftDownSingleExp(l); // shifts the SEInt
            } else if (l.getClass().getName().equals("ListCC")) {
                shiftListCCinMatrix(((ListCC) l));
                shiftDownSingleExp(l);
                shiftListCC(((ListCC) l).first);
                
            } else if (l.getClass().getName().equals("Conscell")) {
                shiftDownSingleExp(l);
                shiftListCC(((Conscell) l).getCar());
                shiftListCC(((Conscell) l).getCdr());
            }
        }
    }
    
    public void shiftListCCinMatrix(ListCC l) {
        // Moves all the elements "list l" in position aij to the position a(i+1)j.
        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                if (listMatrix.get(MAX-i-1,MAX-j-1).size() > 0) {
                    if (listMatrix.get(MAX-i-1,MAX-j-1).contains(l)) // have to do it from bottom up
                    {
                        listMatrix.get(MAX-i-1,MAX-j-1).remove(l);
                        if (!(listMatrix.get(MAX-i-1+1,MAX-j-1).contains(l)))
                            listMatrix.get(MAX-i-1+1,MAX-j-1).addFirst(l); // or addFirst()?? or add()
                    }
                }
            }
        }
    }
    
    
    public void shiftDownSingleExp(SExpression s) {
        // Shifts an object (conscell or SEInt) down by xStep units down in the canvas.
        s.setY(s.getY()+xStep);
        if (s.getY() > yMax)
            yMax = s.getY(); // update the yMax;
    }
    
    
    public ListCC detectCollision() {
        // First detects a collision in the matrix (that is where there is more than one elemenent,
        // in other words where more than one pointer to a list is found). Then returns the list to be shifted down.
        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                if (listMatrix.get(i,j).size() > 1)
                    return (ListCC) giveListCC(listMatrix.get(i,j),i,j);
            }
        }
        return null;
    }
    
    public ListCC giveListCC(LinkedList l,int x, int y) {
        // Returns the list that needs to be shifted down.
        if ((l.size() > 2) || (detectType(l,x,y) == 0)) {
            for(int i = 0; i < orderOfLists.size(); i++) {
                for (int j = 0; j < l.size(); j++) {
                    if (((ListCC)orderOfLists.get(i)) == ((ListCC)l.get(j)))
                        return (ListCC) l.get(j);
                }
            }
        } else// collision between SEInt and conscell
        {
            if (detectType(l,x,y) == 1) {
                return (ListCC) l.get(1);
            }
            if (detectType(l,x,y) == 2) {
                return (ListCC) l.get(0);
            }
        }
        
        PVTSFrame.put(" this should never happen!");
        return null;
        
    }
    
    public int detectType(LinkedList l, int i, int j) {// detects the type of collision found, that is collisions between conscells and/or SEInts.
        ListCC temp0 = (ListCC) l.get(0);
        ListCC temp1 = (ListCC) l.get(1);
        if (thereIs(temp0,i-1,j)) // then temp0 is an SEInt
        {
            if (thereIs(temp1,i-1,j)) // temp1 is SEInt
            {
                return 0; // both are SEInts
            } else
                return 1; // temp0 is SEInt and temp1 is conscell
        } else // temp0 is a conscell
        {
            if (thereIs(temp1,i-1,j)) // temp1 is SEInt
            {
                return 2; // temp0 is a conscell and temp1 is an SEInt
            } else
                return 0; // both are conscells.
        }
    }
    
    public boolean thereIs(ListCC list, int i, int j) {// returns true if "list" is in the linkedlist of lists at position ij in the matrix.
        ListIterator lI = listMatrix.get(i,j).listIterator();
        while (lI.hasNext()) {
            ListCC l = (ListCC) lI.next();
            if (l == list)
                return true;
        }
        // TextIO.putln("could not find the list in "+i+", "+j+". ");
        return false;
    }
    
    public int giveMaxY() {
        return yMax;
    }
    
    public void createInitMatrix(SExpression c, int x, int y, ListCC currentList) {// Creates the initial matrix and sets the apropriate coordinates to the lists (and its elements)
        // according to the initial matrix. This results in the visualization of a list that isn't
        // concerned about collisions.
        if (((ListCC) c).isEmpty()) {
            c.name_obj.setXY(yStep*y+yOffset,xStep*x+xOffset); // set the pos of the name of the list
            c.setXY(yStep*y+yOffset,xStep*x+xOffset); // set the pos of the list.
            if (c.getY() > yMax)
                yMax = c.getY();
        } else {
            while (c != null) {
                if (c.getClass().getName().equals("ListCC")) {
                    currentList = (ListCC) c;
                    //  if (!((ListCC)c).isEmpty())
                    //  {
                    if (((Conscell) ((ListCC)c).first).managed == true) {
                        c.setXY(((Conscell) ((ListCC)c).first).getX(),((Conscell) ((ListCC)c).first).getY());
                        c.name_obj.setXY(yStep*y+yOffset,xStep*x+xOffset); // set the pos of the name of the list
                    } else {
                        c.name_obj.setXY(yStep*y+yOffset,xStep*x+xOffset); // set the pos of the name of the list
                        c.setXY(yStep*y+yOffset,xStep*x+xOffset); // set the pos of the list.
                    }
                    if (c.getY() > yMax)
                        yMax = c.getY();
                    //  TextIO.putln("the object: " + c + "has position: " + c.getX() + ", " + c.getY());
                    c = ((ListCC)c).first;
                }
                //   }
                listMatrix.get(x,y).add(currentList); // add the current list in the correct spot in the matrix
                if (!(orderOfLists.contains(currentList)))
                    orderOfLists.add(currentList);
                c.setXY(yStep*y+yOffset,xStep*x+xOffset); // have to invert and y because in matrix and graph coordinates its inverted
                if (c.getY() > yMax)
                    yMax = c.getY();
                //  TextIO.putln("the object: " + c + "has position: " + c.getX() + ", " + c.getY());
                //  if (isAtom(((Conscell) c).getCar()))
                // if (((Conscell) c).getCar().getClass().getName().equals ("SEInt") || ((Conscell) c).getCar().getClass().getName().equals ("SEBool"))
                if ((((Conscell) c).getCar()).isAtom()) {
                    listMatrix.get(x+1,y).add(currentList);
                    if (!(orderOfLists.contains(currentList)))
                        orderOfLists.add(currentList);
                    ((Conscell)c).getCar().setXY(yStep*(y)+yOffset,xStep*(x+1)+xOffset);// have to invert and y because in matrix and graph coordinates its inverted
                    //   TextIO.putln("the object: " + ((Conscell)c).getCar() + "has position: " + ((Conscell)c).getCar().getX() + ", " + ((Conscell)c).getCar().getY());
                } else {
                    if (((Conscell) c).getCar().getClass().getName().equals("ListCC"))
                        
                    {
                        if (((ListCC)((Conscell) c).getCar()).isEmpty()) {
                            listMatrix.get(x+1,y).add(currentList);
                            if (!(orderOfLists.contains(currentList)))
                                orderOfLists.add(currentList);
                            ((Conscell)c).getCar().setXY(yStep*(y)+yOffset,xStep*(x+1)+xOffset);
                        } else
                            createInitMatrix(((Conscell) c).getCar(),x+1,y,currentList);
                    } else
                        createInitMatrix(((Conscell) c).getCar(),x+1,y,currentList);
                    //   TextIO.putln("the object: " + ((Conscell)c).getCar() + "has position: " + ((Conscell)c).getCar().getX() + ", " + ((Conscell)c).getCar().getY());
                }
                c = ((Conscell) c).getCdr();
                y++;
                
            }
        }
    }
}



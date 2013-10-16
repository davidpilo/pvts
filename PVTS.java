
/*
 * PVTS.java
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

class PVTS // Pilo's Visualization tool for Scheme
{

   // public static Stack<Hashtable> procedure_environments = new Stack();
   // public static int backup_pos = 0;
   // public static int current_pos = 0;
    public static SExpression current_SExpression = null;
    public static boolean consMode = false;
    public static boolean quote_mode = false;
    public static boolean define_body_func = false;
    public static boolean evaluatingProcedure = false;
    public static int current_level = 0;
    public static Stack<Integer> parenthesisLocStack =  new Stack();

    public static boolean debug = false;
    public static int currentLine = 1;
    
    public static LinkedList list_of_lists;// = new LinkedList();
    public static LinkedList orphan_list_of_lists;// = new LinkedList();
    public static LinkedList list_of_trees;
    
    public static int a;// = 35;
    public static int b;// = 55;
    public static Environment envStack = new Environment();
    public static Hashtable globalEnv = new Hashtable();
    public static Stack<Object> bufferStack = new Stack();
    public static String globalBuffer;
    public static boolean error;
    
    
    // parameters for coordinates of Recursive Function drawings
    public static int yRec;
    
    
    PVTS() {
        // parameters for coordinates of Recursive Function drawings
     //   DrawingTreesPanel.clearTagList();
        PVTSFrame.recuTextArea.setText("");
        PVTSFrame.functag.setText("");
        DrawingTreesPanel.currentlySelected = null;
        yRec = 50;
        FunctionManager.current_y = 0;
        String globalBuffer = "";
        int initPos = 0;
        currentLine = 1;
        ErrorMess.allmessages = "";
        bufferStack.clear();
        bufferStack.push(globalBuffer);
        bufferStack.push(initPos);
        envStack.enviro.clear();
        globalEnv.clear();
        envStack.enviro.push(globalEnv);
        quote_mode = false;
        a = 32;// affects y
        b = 150;// affects x
        list_of_lists = new LinkedList();// this list will hold all the Cons Cell lists.
        orphan_list_of_lists = new LinkedList(); // this list will hold all the orphan lists.
        list_of_trees = new LinkedList(); // this list will hold all the trees related to the recursive functions drawn.
        error = false;
        ReadEvalPrint.loadPrimitives(envStack);
        
        
    } // end PVTS()
    
    public static void resetPVTS(String g) {
        PVTSFrame.recuTextArea.setText("");
        ArrowDB.arrowArray = new ArrayList();
       // DrawingTreesPanel.clearTagList();
        PVTSFrame.functag.setText("");
        DrawingTreesPanel.currentlySelected = null;
        yRec = 50;
        FunctionManager.current_y = 0;
        String globalBuffer = g;
        int initPos = 0;
        bufferStack.clear();
        bufferStack.push(globalBuffer);
        bufferStack.push(initPos);
        envStack.enviro.clear();
        globalEnv.clear();
        envStack.enviro.push(globalEnv);
        quote_mode = false;
        currentLine = 1;
        ErrorMess.allmessages = "";
        a = 32; // affects y
        b = 150;// affects x
        list_of_lists = new LinkedList();// this list will hold all the Cons Cell lists.
        orphan_list_of_lists = new LinkedList(); // this list will hold all the orphan lists.
        list_of_trees = new LinkedList(); // this list will hold all the trees related to the recursive functions drawn.
        error = false;
        ReadEvalPrint.loadPrimitives(envStack);
    }
    
    public static boolean moreInput() {
        ReadEvalPrint.skipBlanks();
        return (TextIO.peek() != '~' );//(TextIO.pos < (TextIO.buffer.length()));
    }
    
    public static boolean anotherSExpr() {
        ReadEvalPrint.skipBlanks();
        char c = TextIO.peek();
        //TextIO.putln("in anotherExpr"); // DEBUG
        //TextIO.putln(c); // DEBUG
        boolean b = (c == '(') || Character.isLetter(c) || Character.isDigit(c) || (c == '#') || (c == '\'')
        || (c == '+') || (c == '-') || (c == '/') || (c == '*') || (c == '<')  || (c == '>')
        || (c == '=') || (c == '!') || (c == '"');
        // if (!b) TextIO.getln(); // Flush the line
        return (b && (c != '\n') && (c != '\r'));
    }
    
    
    
    public static SExpression repProcedure() {
        ArrayList<SExpression> posible_body = new ArrayList();
        //posible_body.add(new Primitive("begin",86));
        SExpression exp = new Mess("the procedure has no return statement! revise syntax");
        // will do the same as rep() except that will return in the end the las SExpression.
        while (moreInput() && !error) {
            ReadEvalPrint.skipBlanks();
            if (TextIO.peek() == ';') // it denotes that the current line is a comment
                TextIO.getln(); // flush the line
            if (!anotherSExpr() && !(TextIO.peek() == ';')) {
                PVTSFrame.put("read: bad syntax, unexpected "+TextIO.getAnyChar()+"\n");
                PVTS.error = true;
                break;
            }
            // if (!anotherSExpr())
            //   break;
           
            while (anotherSExpr() && !error) {
                Interpreter interp = new Interpreter();
                try {
                    // read eval print loop
                    exp = interp.parse_eval(envStack);
                    //if (!exp.getClass().getName().equalsIgnoreCase("Mess"))
                    posible_body.add(exp);
         
                } catch (Exception e) {
                    PVTSFrame.put(e.getMessage());
                    PVTSFrame.put(ErrorMess.allmessages);
                }
                
            } // END while (anotherSExpr())

        }
        if (exp.isProcedure()) {
            //((Procedure) exp).body = posible_body;
            Procedure p  = new Procedure(((Procedure)exp).op);
            p.parameters = ((Procedure)exp).parameters;
            p.body = posible_body;
            //p.environment = new Hashtable();
            //Enumeration<SExpression> ele = PVTS.envStack.enviro.peek().elements();
            //Enumeration<String> keys = PVTS.envStack.enviro.peek().keys();
          /*  while (ele.hasMoreElements()) {
            SExpression next = ele.nextElement();
            String s = keys.nextElement();
            p.environment.put(s,next);
            }*/
            //p.environment = PVTS.envStack.enviro.peek();
            
            //p.environment = PVTS.procedure_environments.peek();
           // p.body.add(posible_body.get(0));
          //  p.body.add(posible_body.get(1));
          //  p.body.add(((Procedure) exp).body.get(0));

            //for (int i=0 ; i < posible_body.size() ; i ++) {
              //  if (!((Procedure)exp).name_obj.equals("SExpression!"))
              //      ((Procedure)exp).addToBody(posible_body.get(i));
              //  else
              //      return new ErrorMess("returned an SExpression! @ pvts.java (repProcedure())");
            //}
            return p;
        }
            //return eval_procedure(posible_body,new Primitive("begin",86), PVTS.envStack);
        else
            return exp;
    }
    
    public static SExpression repLet() {
        SExpression exp = new Mess("the procedure has no return statement! revise syntax");
        // will do the same as rep() except that will return in the end the las SExpression.
        while (TextIO.peek() != ')' && !error) {
            ReadEvalPrint.skipBlanks();
            if (TextIO.peek() == '~') // end of file error
                new ErrorMess("read: expected ')'");
            if (TextIO.peek() == ';') // it denotes that the current line is a comment
                TextIO.getln(); // flush the line
          
            while (anotherSExpr() && !error) {
                Interpreter interp = new Interpreter();
                try {
                    // read eval print loop
                    exp = interp.parse_eval(envStack);
          
                } catch (Exception e) {
                    PVTSFrame.put(e.getMessage());
                    PVTSFrame.put(ErrorMess.allmessages);
                }
                
            } // END while (anotherSExpr())
        }
        return exp;
    } 
    
    public static SExpression repCond() {
        SExpression exp = new Mess("the procedure has no return statement! revise syntax");
        // will do the same as rep() except that will return in the end the las SExpression.
        while (TextIO.peek() != ')' && !error) {
            ReadEvalPrint.skipBlanks();
            if (TextIO.peek() == ';') // it denotes that the current line is a comment
                TextIO.getln(); // flush the line
            if (!anotherSExpr() && !(TextIO.peek() == ';')) {
                PVTSFrame.put("read: bad syntax, unexpected "+TextIO.getAnyChar()+"\n");
                PVTS.error = true;
                break;
            }
            // if (!anotherSExpr())
            //   break;
            while (anotherSExpr() && !error) {
                Interpreter interp = new Interpreter();
                try {
                    // read eval print loop
                    exp = interp.parse_eval(envStack);
           
                } catch (Exception e) {
                    PVTSFrame.put(e.getMessage());
                    PVTSFrame.put(ErrorMess.allmessages);
                }
                
            } // END while (anotherSExpr())
        }
        return exp;
    }
    
    public static void rep() {
        //ReadEvalPrint.skipBlanks();
        if(!PVTSFrame.stepper_mode) {
            while (moreInput() && !error) {
                ReadEvalPrint.skipBlanks();
                if (TextIO.peek() == ';') // it denotes that the current line is a comment 
                {TextIO.getln(); PVTSFrame.startHighlight = TextIO.getPos();} // flush the line
                if (!anotherSExpr() && !(TextIO.peek() == ';') && moreInput()) {
                    PVTSFrame.put("read: bad syntax, unexpected "+TextIO.getAnyChar()+"\n");
                    PVTS.error = true;
                    break;
                }
                // if (!anotherSExpr())
                //   break;
                while (anotherSExpr() && !error) {
                    runInterpreter();
                } // END while (anotherSExpr())
            } // END while (moreInput())
            
            
        }//end if stepper_mode
        else {
            ReadEvalPrint.skipBlanks();
            while (TextIO.peek() == ';') {

                ReadEvalPrint.skipBlanks();
                TextIO.getln();
                PVTSFrame.startHighlight = TextIO.getPos();
                ReadEvalPrint.skipBlanks();
            }// flush the line
            // ReadEvalPrint.skipBlanks();
            if(moreInput() && anotherSExpr() && !error) {
                // if (!anotherSExpr())
                //     break;
                // ReadEvalPrint.skipBlanks();
                runInterpreter();
                
            } else if (!anotherSExpr() && !(TextIO.peek() == ';') && (moreInput())) {
                PVTSFrame.put("read: bad syntax, unexpected "+TextIO.getAnyChar()+"\n");
                PVTS.error = true;
            } else {
                if (error)
                    PVTSFrame.put(ErrorMess.allmessages);
                else {
                    PVTSFrame.stopStepper = true;
                    //PVTSFrame.stopStepper();
                    //PVTSFrame.put("system: done! Please Stop Stepper.\n");
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        }
        
    } // end rep ()
    
    
    public static void runInterpreter() {
        Interpreter interp = new Interpreter();
        try {
            // read eval print loop
            SExpression exp = interp.parse_eval(envStack);
            if (!exp.getClass().getName().equals("Mess")) {
                if (exp != null) exp.print();
                PVTSFrame.put("\n");
                if (!PVTS.orphan_list_of_lists.contains(exp) && !PVTS.list_of_lists.contains(exp) && exp.getClass().getName().equals("ListCC")) {
                    PVTS.orphan_list_of_lists.add(exp);
                    ListManager lm = new ListManager((ListCC) exp);
                    lm.setOffsets(PVTS.a,PVTS.b);
                    lm.manage();
                    PVTS.a = 64 + lm.giveMaxY();
                }
            }
        } catch (Exception e) {
            PVTSFrame.put(e.getMessage());
            PVTSFrame.put(ErrorMess.allmessages);
        }
        
        
    }
}
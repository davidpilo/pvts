/* 
 *
 * Interpreter class
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



public class Interpreter {
    
    
    public SExpression parse_eval(Environment env) {
        //PVTS.backup_pos = TextIO.pos;
        SExpression e;
        String st ="";
        if (!PVTS.error) {
            // boolean isList = false;
            SExpression exp = null;
            ReadEvalPrint.skipBlanks();
            char nc = TextIO.peek();
            if (nc == '(') {
                TextIO.getAnyChar(); // clear the '('
                if (PVTS.quote_mode) {
                    e = (SExpression) env.get("list");
                } else {
                    st = TextIO.getWord(); // May have to modify getWord. ')' is a delimiter.
                    //TextIO.putln(st); //DEBUG
                    //process special forms:
                    if (st.equalsIgnoreCase("define")) {
                        exp = define(env);
                        return PVTS.current_SExpression = exp;
                    }
                    
                    if (st.equalsIgnoreCase("if")) {
                        exp = do_if(env);
                        return PVTS.current_SExpression = exp;
                    }
                    if (st.equalsIgnoreCase("cond")) {
                        exp = do_cond(env);
                        return PVTS.current_SExpression = exp;
                    }
                    if (st.equalsIgnoreCase("let")) {
                        exp = do_let(env);
                        env.enviro.pop();
                        return PVTS.current_SExpression = exp;
                    }
                    if (st.equalsIgnoreCase("set!")) {
                        exp = do_set(env);
                        return PVTS.current_SExpression = exp;
                    }
                    if (st.equals("lambda")) {
                        exp = do_lambda(env);
                        return PVTS.current_SExpression = exp;
                    }
                    // if (st.equalsIgnoreCase("read")) {
                    //     exp = do_read(env);
                    //     return exp;
                    // }
                    
                    if (st.equals("") && TextIO.peek()==')') {
                        exp = do_list(new ArrayList(),env);
                        TextIO.getAnyChar();
                        return PVTS.current_SExpression = exp;
                    }
                    // at this point we know it is not a special form
                    e = (SExpression) env.get(st); // Look-up the function by name.
                    
                }
                
                
                //process function applications
                if (!(e.getClass().getName().equals("Mess"))) { // found a function to call
                    // detect if it is a primitive or a procedure:
                    if(e.getClass().getName().equalsIgnoreCase("Procedure")) { // we got a procedure
                        // get the arguments
                        ArrayList args = new ArrayList();
                        while (TextIO.peek() != ')') // check end of list
                        {
                            ReadEvalPrint.skipBlanks();
                            if (TextIO.peek() == '\n') { PVTSFrame.put("Unexpected end of line."); break;}
                            if (TextIO.peek() == ')') { break;}
                            SExpression en = parse_eval(env);
                            args.add(en);
                            /* if (!PVTS.orphan_list_of_lists.contains(en) && !PVTS.list_of_lists.contains(en) && en.getClass().getName().equals("ListCC")) {
                                 PVTS.orphan_list_of_lists.add(en);
                                 ListManager lm = new ListManager((ListCC) en);
                                 lm.setOffsets(PVTS.a,PVTS.b);
                                 lm.manage();
                                 PVTS.a = 64 + lm.giveMaxY();
                            }*/
                        }
                        ReadEvalPrint.skipBlanks(); //                                       watch this FIX ME??
                        if (TextIO.peek() == ')') TextIO.getAnyChar(); // clear ")"
                        // eval procedure:
                        PVTS.evaluatingProcedure = true;
                        //    if (((Procedure) e).isRecursive) {
                        
                        //    }
                        exp = eval_procedure(args,(Procedure) e,env);
                        PVTS.evaluatingProcedure = false;
                        // PVTS.current_recursive_level = 0;
                        
                    } else {
                        
                        // First evaluate all arguments and put them in a list.
                        ArrayList args = new ArrayList();
                        while (TextIO.peek() != ')' && !PVTS.error) // check end of list
                        {
                            ReadEvalPrint.skipBlanks();
                            if (TextIO.peek() == '~') { return new ErrorMess("read: bad syntax, expected ')'");}
                            if (TextIO.peek() == '\n') { PVTSFrame.put("Unexpected end of line."); break;}
                            if (TextIO.peek() == ')') { break;}
                            if (TextIO.peek() == '\'' && PVTS.define_body_func) { // capture the quote in define_body_func
                                args.add(new SESymbol("'"));
                                TextIO.getAnyChar(); // catch the quote
                            }
                            if (TextIO.peekWord().equalsIgnoreCase(".") && PVTS.quote_mode) {
                                TextIO.getAnyChar();
                                //  PVTS.cons1stArgument = (SExpression) args.get(args.size()-1);
                                PVTS.consMode = true;
                            }
                            if (TextIO.peekWord().equalsIgnoreCase(".") && !PVTS.quote_mode) {
                                new ErrorMess("system: bad syntax (illegal use of '.')");
                                break;
                            }
                            
                            SExpression en = parse_eval(env);
                            if (PVTS.consMode == true) {
                                //  PVTS.cons2ndArgument = (SExpression) args.get(args.size()-1);
                                ReadEvalPrint.skipBlanks();
                                if (TextIO.peek() != ')')
                                    new ErrorMess("system: bad syntax (illegal use of '.')");
                            }
                            args.add(en);
                            if (e.isPrimitiveG()){
                                if (((Primitive)e).prim_code == 5  || ((Primitive)e).prim_code == 6 ) {
                                    if (en.isListCC()) {
                                        if (!((ListCC) en).isEmpty()){
                                            if (((ListCC) en).first.managed == false )
                                                if (!PVTS.orphan_list_of_lists.contains(en) && !PVTS.list_of_lists.contains(en) && en.getClass().getName().equals("ListCC")) {
                                                PVTS.orphan_list_of_lists.add(en);
                                                ListManager lm = new ListManager((ListCC) en);
                                                lm.setOffsets(PVTS.a,PVTS.b);
                                                lm.manage();
                                                PVTS.a = 64 + lm.giveMaxY();
                                                }
                                        }}}
                            }
                            
                        }
                        ReadEvalPrint.skipBlanks(); //                                       watch this FIX ME??
                        if (TextIO.peek() == ')') TextIO.getAnyChar(); // clear ")"
                        // Then call the function and pass-in the list of args.
                        // THIS IS DANGEROUS???? OR VERY GOOD                               watch this FIX ME>??
                        if (TextIO.peek() == '\'') {
                            TextIO.getAnyChar();
                            //PVTS.current_SExpression = exp;
                            return  PVTS.current_SExpression = do_list(args,env);
                        }
                        if (!e.isFunction())
                            return new ErrorMess("procedure application: expected procedure, given:"+e.toString());
                        int id = ((Function) e).getCode();
                        // execute primitive:
                        exp = eval_primitive(args,id,env);
                        
                    }
                }else {
                    if (st.equals("") && TextIO.peek() == '(') { //case where a function evaluates into a function!
                        exp = func_eval_func(env);
                    } else
                        exp = new ErrorMess("reference to undefined identifier: "+st +"@ parse_eval 178");
                }
            } else {
                if (Character.isLetter(nc) || ((nc == '\'') && PVTS.quote_mode)) {
                    
                    while (TextIO.peek() == '\'') {
                        st = st +"'";
                        TextIO.getAnyChar();
                    }
                    st = st+TextIO.getWord();
                    //  TextIO.putln("getting a word"); //DEBUG
                    if (PVTS.quote_mode == false) {
                        SExpression expres = lookup_variable_value(st,env);
                        return PVTS.current_SExpression = expres;
                    } else
                        exp = new SESymbol(st);
                } else if (nc == '#') {
                    String sb ="";
                    TextIO.getAnyChar();
                    char tempB = TextIO.getAnyChar();
                    //   TextIO.putln("getting a bool"); // DEBUG
                    if (tempB == 't') {
                        sb = "#t";
                        exp = new SEBool(sb);
                    } else if (tempB == 'f') {
                        sb = "#f";
                        exp = new SEBool(sb);
                    } else if(tempB == '\\') {
                        sb = TextIO.getWord();
                        if (sb.length() != 1)
                            exp = new ErrorMess("read: bad character constant: #\\" + sb);
                        else
                            exp = new SEChar("#\\"+sb);
                    } else
                        exp = new ErrorMess("invalid expression: "+sb);
                } else if (Character.isDigit(nc) || nc == '.' || nc == '-') {// assume it's an integer. Needs to be checked.
                    String s = TextIO.peekWord();
                    if (s.length() == 1 && nc == '-'){
                        TextIO.getAnyChar();
                        exp = new Primitive(s,((Function) env.get(s)).getCode());
                        // } //else if (s.length() == 1 && nc == '.') {
                        //  TextIO.getAnyChar();
                        //  exp = null;
                    } else
                        exp = getNumber(s);
                } else if (nc == '\'') {
                    exp = do_quote(env);
                } else if (nc == '"') {
                    TextIO.getAnyChar(); // get rid of the '"'
                    String s = TextIO.getWordForSEString();
                    if (TextIO.peek() != '"')
                        exp = new ErrorMess("read: expects '\"'");
                    else
                        TextIO.getAnyChar();//get rid of the '"'
                    exp = new SEString(s);
                } else {
                    String pr = TextIO.getWord();
                    if (SExpression.isPrimitive(pr)) {
                        exp = new Primitive(pr,((Function) env.get(pr)).getCode());
                    } else
                        exp = new ErrorMess("reference to undefined identifier: " + pr+"@ parse_eval 233");
                    
                }
                
            }
            
            return PVTS.current_SExpression = exp;
        }
        return new ErrorMess("error: please check syntax.");
    }// end parse_eval
    
    
    
    public SExpression getNumber(String s) {
        if (isInteger(s)) {
            int i = TextIO.getInt();
            return new SEInt(i);
        } else {
            double i = TextIO.getDouble();
            return new SEFloat(i);
        }
    }
    
    
    public SExpression func_eval_func(Environment env) {
        SExpression exp = new Mess("result unitialized, should never return this.");
        SExpression f = parse_eval(env);
        if (!f.isFunction())
            return new ErrorMess("error: expected a function to evaluate into a function not an Atom or a List");
        else if (f.isPrimitiveG()) {
            // First evaluate all arguments and put them in a list.
            ArrayList args = new ArrayList();
            while (TextIO.peek() != ')' && !PVTS.error) // check end of list
            {
                ReadEvalPrint.skipBlanks();
                if (TextIO.peek() == '\n') { PVTSFrame.put("Unexpected end of line."); break;}
                if (TextIO.peek() == ')') { break;}
                if (TextIO.peek() == '\'' && PVTS.define_body_func) { // capture the quote in define_body_func
                    args.add(new SESymbol("'"));
                    TextIO.getAnyChar(); // catch the quote
                }
                
                SExpression en = parse_eval(env);
                // if (PVTS.error)
                // break;
                args.add(en);
            }
            ReadEvalPrint.skipBlanks(); //                                       watch this FIX ME??
            if (TextIO.peek() == ')') TextIO.getAnyChar(); // clear ")"
            // Then call the function and pass-in the list of args.
            // THIS IS DANGEROUS???? OR VERY GOOD                               watch this FIX ME>??
            if (TextIO.peek() == '\'') {
                TextIO.getAnyChar();
                return  do_list(args,env);
            }
            if (!f.isFunction())
                return new ErrorMess("procedure application: expected procedure, given:"+f.toString());
            int id = ((Function) f).getCode();
            // execute primitive:
            exp = eval_primitive(args,id,env);
            
        } else if (f.isProcedure()){
            ArrayList args = new ArrayList();
            while (TextIO.peek() != ')') // check end of list
            {
                ReadEvalPrint.skipBlanks();
                if (TextIO.peek() == '\n') { PVTSFrame.put("Unexpected end of line."); break;}
                if (TextIO.peek() == ')') { break;}
                SExpression en = parse_eval(env);
                args.add(en);
            }
            ReadEvalPrint.skipBlanks(); //                                       watch this FIX ME??
            if (TextIO.peek() == ')') TextIO.getAnyChar(); // clear ")"
            // eval procedure:
            PVTS.evaluatingProcedure = true;
            exp = eval_procedure(args,(Procedure) f,env);
            PVTS.evaluatingProcedure = false;
            //PVTS.current_recursive_level = 0;
        } else return new ErrorMess("expected a valid SExpression (this should never happen though)");
        return exp;
        
    }
    
    public SExpression do_abs(ArrayList<SExpression> sexprs, Environment env) {
        if (!sexprs.get(0).isSENumber()) {
            return new ErrorMess("abs: expects argument of type <number>; given "+sexprs.get(0));
        } else {
            if (sexprs.get(0).isSEFloat()) {
                return new SEFloat(Math.abs(((SEFloat)sexprs.get(0)).getValue()));
            } else
                return new SEInt(Math.abs(((SEInt)sexprs.get(0)).getValue()));
        }
        
        
    }
    
    
    public SExpression do_car(ArrayList<SExpression> sexprs, Environment env) {
        SExpression exp;
        if (sexprs.get(0).isListCC()) {
            
            if (((ListCC) sexprs.get(0)).isEmpty())
                exp = new ErrorMess("car: expects a non-empty list");
            else
                exp = ((ListCC) sexprs.get(0)).car();
        } else
            exp = new ErrorMess("car: expects an argument of type <list>");
        
        return exp;
    }
    
    public SExpression do_cdr(ArrayList<SExpression> args, Environment env) {
        SExpression exp;
        if (args.get(0).isListCC()) {
            if (!((ListCC) args.get(0)).isEmpty()) {
                ListCC lc = (ListCC) args.get(0);
                if (lc.first.isDottedPair())
                    exp = lc.getFirst().getDotted();
                else
                    exp = lc.cdr();
            } else
                exp = new ErrorMess("cdr: expects an non-empty list");
        } else
            exp = new ErrorMess("cdr: expects an argument of type <list>");
        
        return exp;
    }
    
    public SExpression eval_primitive(ArrayList<SExpression> args,int id, Environment env) {
        SExpression exp;
        switch (id) {
            case 1:  // (list <args>)
                exp = do_list(args,env);
                break;
            case 2: // (car l)
                if (args.size() != 1)
                    exp = new ErrorMess("car: expects 1 argument, given "+args.size());
                else {
                    exp = do_car(args,env);
                }
                break;
            case 3: // (cdr l)
                if (args.size() != 1)
                    exp = new ErrorMess("cdr: expects 1 argument, given "+args.size());
                else {
                    
                    exp = do_cdr(args,env);
                }
                break;
            case 4: // (cons a l)
                if (args.size() != 2)
                    exp = new ErrorMess("cons: expects 2 arguments, given "+args.size());
                else {
                    exp = do_cons(args,env);
                }
                break;
            case 5: // (set-car! l a)
                if (args.size() != 2)
                    exp = new ErrorMess("set-car!: expects 2 arguments, given "+args.size());
                else {
                    exp = do_set_car(args,env);
                }
                break;
            case 6: // (set-cdr! l a)
                if (args.size() != 2)
                    exp = new ErrorMess("set-cdr!: expects 2 arguments, given "+args.size());
                else {
                    exp = do_set_cdr(args,env);
                }
                break;
            case 7: // (append l l)
                if (allLists(args))
                    exp = do_append(args,env);
                else
                    exp = new ErrorMess("append: expected argument of type <proper list>");
                break;
            case 8: // (> <args>)
                if (args.size() < 2)
                    exp = new ErrorMess(">: expects at least 2 arguments, given "+args.size());
                else
                    exp = do_greater(args,env);
                break;
            case 9: // (< <args>)
                if (args.size() < 2)
                    exp = new ErrorMess("<: expects at least 2 arguments, given "+args.size());
                else
                    exp = do_less(args,env);
                break;
            case 10: // (>= <args>)
                if (args.size() < 2)
                    exp = new ErrorMess(">=: expects at least 2 arguments, given "+args.size());
                else
                    exp = do_greater_or_equal(args,env);
                break;
            case 11: // (<= <args>)
                if (args.size() < 2)
                    exp = new ErrorMess("<=: expects at least 2 arguments, given "+args.size());
                else
                    exp = do_less_or_equal(args,env);
                break;
            case 12: // (= <args>)
                if (args.size() < 2)
                    exp = new ErrorMess("=: expects at least 2 arguments, given "+args.size());
                else
                    exp = do_equal(args,env);
                break;
            case 13: // (+ <args>)
                exp = do_add(args,env);// change to a different function
                break;
            case 14: // (- <args>)
                if (args.size() == 0)
                    exp = new ErrorMess("-: expects at least 1 arguments, given "+args.size());
                else
                    exp = do_minus(args,env);
                break;
            case 15:// (* <args>)
                exp = do_multiplication(args,env);
                break;
            case 16:// (/ <args>)
                if (args.size() == 0)
                    exp = new ErrorMess("/: expects at least 1 arguments, given "+args.size());
                else
                    exp = do_division(args,env);
                break;
            case 17:
                exp = do_and(args,env);
                break;
            case 18:
                exp = do_or(args,env);
                break;
            case 19:
                if (args.size() != 1)
                    exp = new ErrorMess("not: expects 1 argument, given "+args.size());
                else
                    exp = do_not(args,env);
                break;
            case 20:
                if (args.size() != 1)
                    exp = new ErrorMess("display: expects 1 argument, given "+args.size());
                else
                    exp = do_display(args,env);
                break;
            case 21:
                if (args.size() != 1)
                    exp = new ErrorMess("null?: expects 1 argument, given "+args.size());
                else
                    exp = do_null(args,env);
                break;
            case 22:
                if (args.size()!= 2)
                    exp = new ErrorMess("assoc: expects 2 arguments, given "+args.size());
                else
                    exp=  do_assoc(args,env);
                break;
            case 23:
                if (args.size()!= 2)
                    exp = new ErrorMess("equal?: expects 2 arguments, given "+args.size());
                else
                    exp=  do_printTheSame(args,env);
                break;
            case 24:
                if (args.size()!= 1)
                    exp = new ErrorMess("list?: expects 1 argument, given "+args.size());
                else
                    exp=  do_isList(args,env);
                break;
            case 25:
                if (args.size()!= 1)
                    exp = new ErrorMess("odd?: expects 1 argument, given "+args.size());
                else
                    exp=  do_isOdd(args,env);
                break;
            case 26:
                if (args.size()!= 1)
                    exp = new ErrorMess("even?: expects 1 argument, given "+args.size());
                else
                    exp=  do_isEven(args,env);
                break;
            case 27:
                if (args.size()!= 1)
                    exp = new ErrorMess("abs: expects 1 argument, given "+args.size());
                else
                    exp=  do_abs(args,env);
                break;
            case 28:
                if (args.size()!= 1)
                    exp = new ErrorMess("cos: expects 1 argument, given "+args.size());
                else
                    exp=  do_cos(args,env);
                break;
            case 29:
                if (args.size()!= 1)
                    exp = new ErrorMess("sin: expects 1 argument, given "+args.size());
                else
                    exp=  do_sin(args,env);
                break;
            case 30:
                if (args.size()!= 1)
                    exp = new ErrorMess("tan: expects 1 argument, given "+args.size());
                else
                    exp=  do_tan(args,env);
                break;
            case 31:
                if (args.size()!= 1)
                    exp = new ErrorMess("acos: expects 1 argument, given "+args.size());
                else
                    exp=  do_acos(args,env);
                break;
            case 32:
                if (args.size()!= 1)
                    exp = new ErrorMess("asin: expects 1 argument, given "+args.size());
                else
                    exp=  do_asin(args,env);
                break;
            case 33:
                if (args.size()!= 1)
                    exp = new ErrorMess("atan: expects 1 argument, given "+args.size());
                else
                    exp=  do_atan(args,env);
                break;
            case 34:
                if (args.size()!= 1)
                    exp = new ErrorMess("sqrt: expects 1 argument, given "+args.size());
                else
                    exp=  do_sqrt(args,env);
                break;
            case 35:
                if (args.size()!= 1)
                    exp = new ErrorMess("zero?: expects 1 argument, given "+args.size());
                else
                    exp=  do_isZero(args,env);
                break;
            case 36:
                if (args.size() < 1)
                    exp = new ErrorMess("max: expects at least 1 argument, given "+args.size());
                else
                    exp=  do_max(args,env);
                break;
            case 37:
                if (args.size() < 1)
                    exp = new ErrorMess("min: expects at least 1 argument, given "+args.size());
                else
                    exp=  do_min(args,env);
                break;
            case 38:
                if (args.size()!= 2)
                    exp = new ErrorMess("eq?: expects 2 arguments, given "+args.size());
                else
                    exp=  do_printTheSame(args,env);
                break;
            case 39:
                if (args.size()!= 2)
                    exp = new ErrorMess("eqv?: expects 2 arguments, given "+args.size());
                else
                    exp=  do_printTheSame(args,env);
                break;
            case 40: // (newline)
                if (args.size()!= 0)
                    exp = new ErrorMess("newline: expects no arguments, given "+args.size() );
                else
                    exp = do_newLine(args,env);
                break;
            case 41: // (exp <number>)
                if (args.size() != 1)
                    exp = new ErrorMess("exp: expects 1 argument, given "+args.size() );
                else
                    exp = do_exp(args,env);
                break;
            case 42: // (expt <number>)
                if (args.size() != 2)
                    exp = new ErrorMess("expt: expects 2 arguments, given "+args.size() );
                else
                    exp = do_expt(args,env);
                break;
            case 43: // (positive? <number>)
                if (args.size() != 1)
                    exp = new ErrorMess("positive?: expects 1 argument, given "+args.size() );
                else
                    exp = do_isPositive(args,env);
                break;
            case 44: // (negative? <number>)
                if (args.size() != 1)
                    exp = new ErrorMess("negative?: expects 1 argument, given "+args.size() );
                else
                    exp = do_isNegative(args,env);
                break;
            case 45: // (ceiling <number>)
                if (args.size() != 1)
                    exp = new ErrorMess("ceiling: expects 1 argument, given "+args.size() );
                else
                    exp = do_ceiling(args,env);
                break;
            case 46: // (floor <number>)
                if (args.size() != 1)
                    exp = new ErrorMess("floor: expects 1 argument, given "+args.size() );
                else
                    exp = do_floor(args,env);
                break;
            case 47: // (integer? <any>)
                if (args.size() != 1)
                    exp = new ErrorMess("integer?: expects 1 argument, given "+args.size() );
                else
                    exp = do_isInteger(args,env);
                break;
            case 48: // (real? <any>)
                if (args.size() != 1)
                    exp = new ErrorMess("real?: expects 1 argument, given "+args.size() );
                else
                    exp = do_isReal(args,env);
                break;
            case 49: // (number? <any>)
                if (args.size() != 1)
                    exp = new ErrorMess("number?: expects 1 argument, given "+args.size() );
                else
                    exp = do_isNumber(args,env);
                break;
            case 50: // (modulo <integer>)
                if (args.size() != 2)
                    exp = new ErrorMess("modulo: expects 2 arguments, given "+args.size() );
                else
                    exp = do_modulo(args,env);
                break;
            case 51: // (procedure? <any>)
                if (args.size() != 1)
                    exp = new ErrorMess("procedure?: expects 1 argument, given "+args.size() );
                else
                    exp = do_isProcedure(args,env);
                break;
            case 52: // (log <number>)
                if (args.size() != 1)
                    exp = new ErrorMess("log: expects 1 argument, given "+args.size() );
                else
                    exp = do_log(args,env);
                break;
            case 53: //(reverse l)
                if (args.size() != 1)
                    exp = new ErrorMess("log: expects 1 argument, given "+args.size() );
                else
                    exp = do_reverse(args,env);
                break;
            case 54: // (caar l)
                if (args.size() != 1)
                    exp = new ErrorMess("caar: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_car(args,env));
                    exp = do_car(a,env);
                }
                break;
            case 55: // (cadr l)
                if (args.size() != 1)
                    exp = new ErrorMess("cadr: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_cdr(args,env));
                    exp = do_car(a,env);
                }
                break;
            case 56: // (cdar l)
                if (args.size() != 1)
                    exp = new ErrorMess("cdar: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_car(args,env));
                    exp = do_cdr(a,env);
                }
                break;
            case 57: // (cddr l)
                if (args.size() != 1)
                    exp = new ErrorMess("cddr: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_cdr(args,env));
                    exp = do_cdr(a,env);
                }
                break;
            case 58: // (caaar l)
                if (args.size() != 1)
                    exp = new ErrorMess("caaar: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_car(args,env));
                    SExpression temp1 = do_car(a,env);
                    a.remove(0);
                    a.add(temp1);
                    exp = do_car(a,env);
                }
                break;
            case 59: // (caadr l)
                if (args.size() != 1)
                    exp = new ErrorMess("caadr: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_cdr(args,env));
                    SExpression temp1 = do_car(a,env);
                    a.remove(0);
                    a.add(temp1);
                    exp = do_car(a,env);
                }
                break;
            case 60: // (cadar l)
                if (args.size() != 1)
                    exp = new ErrorMess("cadar: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_car(args,env));
                    SExpression temp1 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp1);
                    exp = do_car(a,env);
                }
                break;
            case 61: // (cdaar l)
                if (args.size() != 1)
                    exp = new ErrorMess("cdaar: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_car(args,env));
                    SExpression temp1 = do_car(a,env);
                    a.remove(0);
                    a.add(temp1);
                    exp = do_cdr(a,env);
                }
                break;
            case 62: // (caddr l)
                if (args.size() != 1)
                    exp = new ErrorMess("caddr: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_cdr(args,env));
                    SExpression temp1 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp1);
                    exp = do_car(a,env);
                }
                break;
            case 63: // (cdadr l)
                if (args.size() != 1)
                    exp = new ErrorMess("cdadr: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_cdr(args,env));
                    SExpression temp1 = do_car(a,env);
                    a.remove(0);
                    a.add(temp1);
                    exp = do_cdr(a,env);
                }
                break;
            case 64: // (cddar l)
                if (args.size() != 1)
                    exp = new ErrorMess("cddar: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_car(args,env));
                    SExpression temp1 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp1);
                    exp = do_cdr(a,env);
                }
                break;
            case 65: // (cdddr l)
                if (args.size() != 1)
                    exp = new ErrorMess("cdddr: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_cdr(args,env));
                    SExpression temp1 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp1);
                    exp = do_cdr(a,env);
                }
                break;
            case 66: // (caaaar l)
                if (args.size() != 1)
                    exp = new ErrorMess("cdddr: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_car(args,env));
                    SExpression temp1 = do_car(a,env);
                    a.remove(0);
                    a.add(temp1);
                    SExpression temp2 = do_car(a,env);
                    a.remove(0);
                    a.add(temp2);
                    exp = do_car(a,env);
                }
                break;
            case 67: // (cdaaar l)
                if (args.size() != 1)
                    exp = new ErrorMess("cdddr: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_car(args,env));
                    SExpression temp1 = do_car(a,env);
                    a.remove(0);
                    a.add(temp1);
                    SExpression temp2 = do_car(a,env);
                    a.remove(0);
                    a.add(temp2);
                    exp = do_cdr(a,env);
                }
                break;
            case 68: // (cadaar l)
                if (args.size() != 1)
                    exp = new ErrorMess("cdddr: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_car(args,env));
                    SExpression temp1 = do_car(a,env);
                    a.remove(0);
                    a.add(temp1);
                    SExpression temp2 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp2);
                    exp = do_car(a,env);
                }
                break;
            case 69: // (caadar l)
                if (args.size() != 1)
                    exp = new ErrorMess("caadar: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_car(args,env));
                    SExpression temp1 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp1);
                    SExpression temp2 = do_car(a,env);
                    a.remove(0);
                    a.add(temp2);
                    exp = do_car(a,env);
                }
                break;
            case 70: // (caaadr l)
                if (args.size() != 1)
                    exp = new ErrorMess("caaadr: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_cdr(args,env));
                    SExpression temp1 = do_car(a,env);
                    a.remove(0);
                    a.add(temp1);
                    SExpression temp2 = do_car(a,env);
                    a.remove(0);
                    a.add(temp2);
                    exp = do_car(a,env);
                }
                break;
            case 71: // (cddaar l)
                if (args.size() != 1)
                    exp = new ErrorMess("cddaar: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_car(args,env));
                    SExpression temp1 = do_car(a,env);
                    a.remove(0);
                    a.add(temp1);
                    SExpression temp2 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp2);
                    exp = do_cdr(a,env);
                }
                break;
            case 72: // (caddar l)
                if (args.size() != 1)
                    exp = new ErrorMess("caddar: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_car(args,env));
                    SExpression temp1 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp1);
                    SExpression temp2 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp2);
                    exp = do_car(a,env);
                }
                break;
            case 73: // (caaddr l)
                if (args.size() != 1)
                    exp = new ErrorMess("caaddr: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_cdr(args,env));
                    SExpression temp1 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp1);
                    SExpression temp2 = do_car(a,env);
                    a.remove(0);
                    a.add(temp2);
                    exp = do_car(a,env);
                }
                break;
            case 74: // (cadadr l)
                if (args.size() != 1)
                    exp = new ErrorMess("cadadr: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_cdr(args,env));
                    SExpression temp1 = do_car(a,env);
                    a.remove(0);
                    a.add(temp1);
                    SExpression temp2 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp2);
                    exp = do_car(a,env);
                }
                break;
            case 75: // (cdadar l)
                if (args.size() != 1)
                    exp = new ErrorMess("cdadar: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_car(args,env));
                    SExpression temp1 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp1);
                    SExpression temp2 = do_car(a,env);
                    a.remove(0);
                    a.add(temp2);
                    exp = do_cdr(a,env);
                }
                break;
            case 76: // (cdaadr l)
                if (args.size() != 1)
                    exp = new ErrorMess("cdaadr: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_cdr(args,env));
                    SExpression temp1 = do_car(a,env);
                    a.remove(0);
                    a.add(temp1);
                    SExpression temp2 = do_car(a,env);
                    a.remove(0);
                    a.add(temp2);
                    exp = do_cdr(a,env);
                }
                break;
            case 77: // (cdddar l)
                if (args.size() != 1)
                    exp = new ErrorMess("cdddar: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_car(args,env));
                    SExpression temp1 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp1);
                    SExpression temp2 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp2);
                    exp = do_cdr(a,env);
                }
                break;
            case 78: // (cddaar l)
                if (args.size() != 1)
                    exp = new ErrorMess("cddaar: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_car(args,env));
                    SExpression temp1 = do_car(a,env);
                    a.remove(0);
                    a.add(temp1);
                    SExpression temp2 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp2);
                    exp = do_cdr(a,env);
                }
                break;
            case 79: // (cdaddr l)
                if (args.size() != 1)
                    exp = new ErrorMess("cdaddr: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_cdr(args,env));
                    SExpression temp1 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp1);
                    SExpression temp2 = do_car(a,env);
                    a.remove(0);
                    a.add(temp2);
                    exp = do_cdr(a,env);
                }
                break;
            case 80: // (cadddr l)
                if (args.size() != 1)
                    exp = new ErrorMess("cadddr: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_cdr(args,env));
                    SExpression temp1 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp1);
                    SExpression temp2 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp2);
                    exp = do_car(a,env);
                }
                break;
            case 81: // (cddddr l)
                if (args.size() != 1)
                    exp = new ErrorMess("cddddr: expects 1 argument, given "+args.size() );
                else {
                    ArrayList<SExpression> a = new ArrayList();
                    a.add(do_cdr(args,env));
                    SExpression temp1 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp1);
                    SExpression temp2 = do_cdr(a,env);
                    a.remove(0);
                    a.add(temp2);
                    exp = do_cdr(a,env);
                }
                break;
            case 82: //(map <func> <list>)
                if (args.size() != 2)
                    exp = new ErrorMess("map: expects 2 arguments, given "+args.size());
                else{
                    if (!args.get(0).isFunction())
                        exp = new ErrorMess("map: type <procedure> as 1st argument, given "+args.get(0).toString());
                    else if (!args.get(1).isListCC())
                        exp = new ErrorMess("map: type <proper list> as 1st argument, given "+args.get(0).toString());
                    else if (args.get(1).isListCC()) {
                        if (((ListCC)args.get(1)).isEmpty())
                            exp = new ListCC();
                        else if (((ListCC)args.get(1)).last.isDottedPair())
                            exp = new ErrorMess("map: type <proper list> as 1st argument, given "+args.get(0).toString());
                        else
                            exp = do_map(args,env);
                    } else
                        exp = new ErrorMess("map: type <proper list> as 1st argument, given "+args.get(0).toString());
                    
                }
                break;
            case 83: //(member a alist)
                if (args.size() != 2)
                    exp = new ErrorMess("member: expects 2 arguments, given "+args.size());
                else {
                    if (!args.get(1).isProperList())
                        exp = new ErrorMess("member: not a proper list: "+args.get(1));
                    else
                        exp = do_member(args,env);
                }
                break;
            case 84: //(list-ref list pos_int)
                if (args.size() != 2)
                    exp = new ErrorMess("list-ref: expects 2 arguments, given "+args.size());
                else {
                    if (!args.get(0).isProperList())
                        exp = new ErrorMess("list-ref: expects type <proper_list> as 1st argument, given "+args.get(0).toString());
                    if (args.get(1).isSEInt()) {
                        if (((SEInt)args.get(1)).num >= 0)
                            exp = do_list_ref(args,env);
                        else
                            exp = new ErrorMess("list-ref: expects type <non-negative exact integer> as 2nd argument, given "+args.get(1).toString());
                    } else
                        exp = new ErrorMess("list-ref: expects type <non-negative exact integer> as 2nd argument, given "+args.get(1).toString());
                }
                break;
            case 85: //(length list)
                if (args.size() != 1)
                    exp = new ErrorMess("length: expects 1 argument, given "+args.size());
                else
                    exp = do_length(args,env);
                break;
            case 86: //(begin <SExpression>)
                exp = do_begin(args,env);
                break;
            case 87: //(gcd int int)
                if (args.size() != 2)
                    exp = new ErrorMess("gcd: expects 2 arguments, given "+args.size());
                else
                    exp = do_gcd(args,env);
                break;
            case 88: //(char? int int)
                if (args.size() != 1)
                    exp = new ErrorMess("char?: expects 1 argument, given "+args.size());
                else
                    exp = do_isChar(args,env);
                break;
            case 89: //(string? int int)
                if (args.size() != 1)
                    exp = new ErrorMess("string?: expects 1 argument, given "+args.size());
                else
                    exp = do_isString(args,env);
                break;
            case 90: //(symbol? <exp>)
                if (args.size() != 1)
                    exp = new ErrorMess("symbol?: expects 1 argument, given "+args.size());
                else
                    exp = do_isSESymbol(args,env);
                break;
            case 91: //(for-each <proc> <list>)
                if (args.size() != 2)
                    exp = new ErrorMess("for-each: expects 2 arguments, given "+args.size());
                else
                    exp = do_forEach(args,env);
                break;
            default:
                //exp = new ErrorMess("error: the primitive"+"<primitive:"+st+">"+" has been modified!"); // the primitive has been redefined!
                exp = new ErrorMess("error: the primitive"+"<primitive:>"+" has been modified!"); // the primitive has been redefined!
        } // END switch
        return exp;
    }
    
    
    
    
    public SExpression do_forEach(ArrayList<SExpression> sexprs, Environment env) {
        //  ReadEvalPrint.skipBlanks();
        ListCC return_value = new ListCC();
        //  if (((ListCC) sexprs.get(1)).isEmpty())
        //      return new ListCC();
        //  else {
        Conscell c = ((ListCC) sexprs.get(1)).first;
        while (c != null) {
            ArrayList a = new ArrayList();
            a.add(c.car);
            if (sexprs.get(0).isPrimitiveG())
                return_value.add(new Conscell(eval_primitive(a,((Function) sexprs.get(0)).prim_code,env)));
            else if (sexprs.get(0).isFunction())
                return_value.add(new Conscell(eval_procedure(a,((Procedure) sexprs.get(0)),env)));
            else
                return new ErrorMess("map: could not find the function: "+sexprs.get(0).toString());
            c = c.cdr;
        }
        //}
        return new Mess("forEach done");
    }
    
    
    public SExpression do_isSESymbol(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).isSESymbol())
            return new SEBool("#t");
        else
            return new SEBool("#f");
    }
    
    public SExpression do_isString(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).isSEString())
            return new SEBool("#t");
        else
            return new SEBool("#f");
    }
    
    public SExpression do_isChar(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).isSEChar())
            return new SEBool("#t");
        else
            return new SEBool("#f");
    }
    
    public SExpression do_begin(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.size() == 0)
            return new Mess("(begin)");
        else
            return sexprs.get(sexprs.size()-1);
    }
    
    
    public SExpression do_length(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).isProperList()) {
            return new SEInt(((ListCC) sexprs.get(0)).size);
        } else
            return new ErrorMess("length: expects argument of type <proper list>; given" +sexprs.get(0).toString());
    }
    
    public SExpression do_list_ref(ArrayList<SExpression> sexprs, Environment env) {
        int index = ((SEInt) sexprs.get(1)).num;
        ListCC l = (ListCC) sexprs.get(0);
        if (l.isEmpty() || index > (l.size - 1))
            return new ErrorMess("list-ref: index "+index+" too large for list: "+l.toString());
        ListCC c = l;
        
        for (int i = 0; i < index; i++) {
            c = c.cdr();
        }
        return c.car();
    }
    
    
    public SExpression do_set(Environment env) {
        ReadEvalPrint.skipBlanks();
        String s = TextIO.getWord();
        SExpression exp = parse_eval(env);
        if (TextIO.peek() != ')') return new ErrorMess("set!: expected ')'");
        else TextIO.getAnyChar();
        // if ()
        if (env.containsKey(s)) {
            int level = env.getLevel(s);
            //PVTS.procedure_environments.elementAt(level).remove(s);
            // env.enviro.elementAt(level).get(s).hashCode() = exp;
            
            env.enviro.elementAt(level).remove(s);
            //PVTS.procedure_environments.elementAt(level).put(s,exp);
            env.enviro.elementAt(level).put(s,exp);
            return new Mess("(set! "+s+" "+exp.toString()+")");
        } else {
            return new ErrorMess("set!: cannot set undefined identifier: "+s);
        }
        
        
    }
    
    
    public SExpression do_member(ArrayList<SExpression> sexprs, Environment env) {
        SExpression a = sexprs.get(0);
        ListCC l = (ListCC) sexprs.get(1);
        if (l.isEmpty())
            return new SEBool("#f");
        ListCC c = l;
        
        while (c.first != null) {
            if (c.car().toString().equals(a.toString()))
                return c;
            else
                c = c.cdr();
        }
        return new SEBool("#f");
    }
    
    public SExpression do_reverse(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).isProperList())
            return ListCC.makeReverseList(sexprs);
        else
            return new ErrorMess("reverse: expects argument of type <proper list>; given "+sexprs.get(0).toString());
    }
    
    public SExpression do_read(Environment env) {
        ReadEvalPrint.skipBlanks();
        if (TextIO.peek() != ')')
            return new ErrorMess("read: expected a ')'");
        PVTSFrame.readFrame.setVisible(true);
        PVTSFrame.readFrame.setSize(new Dimension(340, 75));
        PVTSFrame.readFrame.setLocation(new Point(400,400));
        PVTSFrame.readFrame.validate();
        //Thread.currentThread().interrupt();
        
        return new Mess("read done");
        
    }
    
    
    
    public SExpression do_modulo(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).isSEInt() && sexprs.get(1).isSEInt()) {
            return new SEInt(((SEInt)sexprs.get(0)).getValue() % ((SEInt)sexprs.get(1)).getValue());
        } else
            return new ErrorMess("modulo: expects type <integer> as its arguments");
    }
    
    
    
    public SExpression do_isProcedure(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).isProcedure()) {
            return new SEBool("#t");
        } else
            return new SEBool("#f");
    }
    
    public SExpression do_isInteger(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).isSEInt()) {
            return new SEBool("#t");
        } else
            return new SEBool("#f");
    }
    
    public SExpression do_isReal(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).isReal()) {
            return new SEBool("#t");
        } else
            return new SEBool("#f");
    }
    
    public SExpression do_isNumber(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).isSENumber()) {
            return new SEBool("#t");
        } else
            return new SEBool("#f");
    }
    
    public SExpression do_isZero(ArrayList<SExpression> sexprs, Environment env) {
        if (!sexprs.get(0).isSENumber()) {
            return new ErrorMess("zero?: expects argument of type <number>; given "+sexprs.get(0));
        } else {
            if (sexprs.get(0).isSEFloat()) {
                if (((SEFloat) sexprs.get(0)).getValue() == 0)
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            } else {
                if (((SEInt) sexprs.get(0)).getValue() == 0)
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            }
            
        }
        
        
    }
    
    public SExpression do_log(ArrayList<SExpression> sexprs, Environment env) {
        if (!sexprs.get(0).isSENumber()) {
            return new ErrorMess("log: expects argument of type <number>; given "+sexprs.get(0));
        } else {
            if (sexprs.get(0).isSEFloat()) {
                return new SEFloat(Math.log(((SEFloat) sexprs.get(0)).getValue()));
            } else {
                return new SEFloat(Math.log(((SEInt) sexprs.get(0)).getValue()));
            }
            
        }
        
        
    }
    
    
    
    public SExpression do_isPositive(ArrayList<SExpression> sexprs, Environment env) {
        if (!sexprs.get(0).isSENumber()) {
            return new ErrorMess("positive?: expects argument of type <number>; given "+sexprs.get(0));
        } else {
            if (sexprs.get(0).isSEFloat()) {
                if (((SEFloat) sexprs.get(0)).getValue() > 0)
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            } else {
                if (((SEInt) sexprs.get(0)).getValue() > 0)
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            }
            
        }
        
        
    }
    
    
    
    
    public SExpression do_isNegative(ArrayList<SExpression> sexprs, Environment env) {
        if (!sexprs.get(0).isSENumber()) {
            return new ErrorMess("negative?: expects argument of type <number>; given "+sexprs.get(0));
        } else {
            if (sexprs.get(0).isSEFloat()) {
                if (((SEFloat) sexprs.get(0)).getValue() < 0)
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            } else {
                if (((SEInt) sexprs.get(0)).getValue() < 0)
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            }
            
        }
        
        
    }
    
    
    public SExpression do_ceiling(ArrayList<SExpression> sexprs, Environment env) {
        if (!sexprs.get(0).isSENumber()) {
            return new ErrorMess("negative?: expects argument of type <number>; given "+sexprs.get(0));
        } else {
            if (sexprs.get(0).isSEFloat()) {
                return new SEInt((int)Math.ceil(((SEFloat) sexprs.get(0)).getValue()));
                
            } else {
                return new SEInt((int)Math.ceil(((SEFloat) sexprs.get(0)).getValue()));
            }
            
        }
        
        
    }
    
    public SExpression do_floor(ArrayList<SExpression> sexprs, Environment env) {
        if (!sexprs.get(0).isSENumber()) {
            return new ErrorMess("negative?: expects argument of type <number>; given "+sexprs.get(0));
        } else {
            if (sexprs.get(0).isSEFloat()) {
                return new SEInt((int)Math.floor(((SEFloat) sexprs.get(0)).getValue()));
                
            } else {
                return new SEInt((int)Math.floor(((SEFloat) sexprs.get(0)).getValue()));
            }
            
        }
        
        
    }
    
    
    public SExpression do_newLine(ArrayList<SExpression> sexprs, Environment env) {
        PVTSFrame.put("\n");
        return new Mess("newLine OK");
    }
    
    
    public SExpression do_max(ArrayList<SExpression> sexprs, Environment env) {
        if (checkArgsForNumbers(sexprs,env)) {
            int index = 0;
            for (int i = 0; i < sexprs.size(); i ++) {
                if (sexprs.get(i).isSEFloat() && sexprs.get(index).isSEFloat()) {
                    if (((SEFloat)sexprs.get(i)).getValue() > ((SEFloat)sexprs.get(index)).getValue())
                        index = i;
                } else if (sexprs.get(i).isSEFloat() && sexprs.get(index).isSEInt()) {
                    if (((SEFloat)sexprs.get(i)).getValue() > ((SEInt)sexprs.get(index)).getValue())
                        index = i;
                } else if (sexprs.get(i).isSEInt() && sexprs.get(index).isSEFloat()) {
                    if (((SEInt)sexprs.get(i)).getValue() > ((SEFloat)sexprs.get(index)).getValue())
                        index = i;
                } else {
                    if (((SEInt)sexprs.get(i)).getValue() > ((SEInt)sexprs.get(index)).getValue())
                        index = i;
                }
                
            }
            return sexprs.get(index);
        } else {
            return new ErrorMess("max: expects argument of type <number>");
        }
    }
    
    
    public Boolean allLists(ArrayList<SExpression> sexprs) {
        Boolean temp = true;
        for (int i = 0; i < sexprs.size(); i++) {
            if (i != (sexprs.size()-1)) {
                if (!sexprs.get(i).isListCC())
                    temp = false;
                else {
                    if (!((ListCC)sexprs.get(i)).isEmpty()) {
                        if (((ListCC)sexprs.get(i)).last.isDottedPair())
                            temp = false;
                    }
                }
            } else {
                if (!sexprs.get(i).isListCC())
                    temp = false;
            }
            
        }
        return temp;
        
    }
    
    public SExpression do_exp(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).isSEInt()){
            return new SEFloat(Math.exp(((SEInt)sexprs.get(0)).getValue()));
            
        } else if (sexprs.get(0).isSEFloat()) {
            return new SEFloat(Math.exp(((SEFloat)sexprs.get(0)).getValue()));
        } else
            return new ErrorMess("exp: expects argument of type <number>");
    }
    
    public SExpression do_expt(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).isSEInt() && sexprs.get(1).isSEInt()){
            return new SEFloat(Math.pow(((SEInt)sexprs.get(0)).getValue(),((SEInt)sexprs.get(1)).getValue()));
            
        } else if (sexprs.get(0).isSEFloat() && sexprs.get(1).isSEFloat()) {
            return new SEFloat(Math.pow(((SEFloat)sexprs.get(0)).getValue(),((SEFloat)sexprs.get(1)).getValue()));
        } else if (sexprs.get(0).isSEInt() && sexprs.get(1).isSEFloat()) {
            return new SEFloat(Math.pow(((SEInt)sexprs.get(0)).getValue(),((SEFloat)sexprs.get(1)).getValue()));
        } else if (sexprs.get(0).isSEFloat() && sexprs.get(1).isSEInt()) {
            return new SEFloat(Math.pow(((SEFloat)sexprs.get(0)).getValue(),((SEInt)sexprs.get(1)).getValue()));
        } else
            return new ErrorMess("expt: expects argument of type <number>");
    }
    
    public SExpression do_min(ArrayList<SExpression> sexprs, Environment env) {
        if (checkArgsForNumbers(sexprs,env)) {
            int index = 0;
            for (int i = 0; i < sexprs.size(); i ++) {
                if (sexprs.get(i).isSEFloat() && sexprs.get(index).isSEFloat()) {
                    if (((SEFloat)sexprs.get(i)).getValue() < ((SEFloat)sexprs.get(index)).getValue())
                        index = i;
                } else if (sexprs.get(i).isSEFloat() && sexprs.get(index).isSEInt()) {
                    if (((SEFloat)sexprs.get(i)).getValue() < ((SEInt)sexprs.get(index)).getValue())
                        index = i;
                } else if (sexprs.get(i).isSEInt() && sexprs.get(index).isSEFloat()) {
                    if (((SEInt)sexprs.get(i)).getValue() < ((SEFloat)sexprs.get(index)).getValue())
                        index = i;
                } else {
                    if (((SEInt)sexprs.get(i)).getValue() < ((SEInt)sexprs.get(index)).getValue())
                        index = i;
                }
                
            }
            return sexprs.get(index);
        } else {
            return new ErrorMess("max: expects argument of type <number>");
        }
    }
    
    
    
    public SExpression do_cos(ArrayList<SExpression> sexprs, Environment env) {
        if (!sexprs.get(0).isSENumber()) {
            return new ErrorMess("cos: expects argument of type <number>; given "+sexprs.get(0));
        } else {
            if (sexprs.get(0).isSEFloat()) {
                return new SEFloat(Math.cos(((SEFloat)sexprs.get(0)).getValue()));
            } else
                return new SEFloat(Math.cos(((SEInt)sexprs.get(0)).getValue()));
        }
        
    }
    
    public SExpression do_tan(ArrayList<SExpression> sexprs, Environment env) {
        if (!sexprs.get(0).isSENumber()) {
            return new ErrorMess("tan: expects argument of type <number>; given "+sexprs.get(0));
        } else {
            if (sexprs.get(0).isSEFloat()) {
                return new SEFloat(Math.tan(((SEFloat)sexprs.get(0)).getValue()));
            } else
                return new SEFloat(Math.tan(((SEInt)sexprs.get(0)).getValue()));
        }
        
    }
    
    public SExpression do_sin(ArrayList<SExpression> sexprs, Environment env) {
        if (!sexprs.get(0).isSENumber()) {
            return new ErrorMess("sin: expects argument of type <number>; given "+sexprs.get(0));
        } else {
            if (sexprs.get(0).isSEFloat()) {
                return new SEFloat(Math.sin(((SEFloat)sexprs.get(0)).getValue()));
            } else
                return new SEFloat(Math.sin(((SEInt)sexprs.get(0)).getValue()));
        }
        
    }
    
    public SExpression do_sqrt(ArrayList<SExpression> sexprs, Environment env) {
        if (!sexprs.get(0).isSENumber()) {
            return new ErrorMess("sqrt: expects argument of type <number>; given "+sexprs.get(0));
        } else {
            if (sexprs.get(0).isSEFloat()) {
                return new SEFloat(Math.sqrt(((SEFloat)sexprs.get(0)).getValue()));
            } else
                return new SEFloat(Math.sqrt(((SEInt)sexprs.get(0)).getValue()));
            
        }
        
    }
    
    public SExpression do_acos(ArrayList<SExpression> sexprs, Environment env) {
        if (!sexprs.get(0).isSENumber()) {
            return new ErrorMess("cos: expects argument of type <number>; given "+sexprs.get(0));
        } else {
            if (sexprs.get(0).isSEFloat()) {
                return new SEFloat(Math.acos(((SEFloat)sexprs.get(0)).getValue()));
            } else
                return new SEFloat(Math.acos(((SEInt)sexprs.get(0)).getValue()));
        }
        
    }
    
    public SExpression do_asin(ArrayList<SExpression> sexprs, Environment env) {
        if (!sexprs.get(0).isSENumber()) {
            return new ErrorMess("sin: expects argument of type <number>; given "+sexprs.get(0));
        } else {
            if (sexprs.get(0).isSEFloat()) {
                return new SEFloat(Math.asin(((SEFloat)sexprs.get(0)).getValue()));
            } else
                return new SEFloat(Math.asin(((SEInt)sexprs.get(0)).getValue()));
        }
        
    }
    
    public SExpression do_atan(ArrayList<SExpression> sexprs, Environment env) {
        if (!sexprs.get(0).isSENumber()) {
            return new ErrorMess("tan: expects argument of type <number>; given "+sexprs.get(0));
        } else {
            if (sexprs.get(0).isSEFloat()) {
                return new SEFloat(Math.atan(((SEFloat)sexprs.get(0)).getValue()));
            } else
                return new SEFloat(Math.atan(((SEInt)sexprs.get(0)).getValue()));
        }
        
    }
    
    public SExpression do_isOdd(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).getClass().getName().equals("SEInt")) {
            if ((((SEInt) sexprs.get(0)).getValue() % 2) == 0)
                return new SEBool("#f");
            else
                return new SEBool("#t");
        } else
            return new ErrorMess("odd?: expects argument of type <integer>; given "+sexprs.get(0));
    }
    
    
    public SExpression do_isEven(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).getClass().getName().equals("SEInt")) {
            if ((((SEInt) sexprs.get(0)).getValue() % 2) == 0)
                return new SEBool("#t");
            else
                return new SEBool("#f");
        } else
            return new ErrorMess("even?: expects argument of type <integer>; given "+sexprs.get(0));
    }
    
    
    
    
    public SExpression do_isList(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).getClass().getName().equals("ListCC"))
            return new SEBool("#t");
        else
            return new SEBool("#f");
    }
    
    
    public SExpression do_and(ArrayList<SExpression> sexprs, Environment env) {
        SExpression return_expr = new SEBool("#t");
        for (SExpression sxpr : sexprs) {
            if (sxpr.getClass().getName().equals("SEBool")) {
                if (!((SEBool) sxpr).boolValue())
                    return new SEBool("#f");
                else
                    return_expr = new SEBool("#t");
            } else
                return_expr = sexprs.get(sexprs.size()-1);
            
        }
        
        return return_expr;
    }
    
    public SExpression do_or(ArrayList<SExpression> sexprs, Environment env) {
        
        for (SExpression sxpr : sexprs) {
            if (sxpr.getClass().getName().equals("SEBool")) {
                if (((SEBool) sxpr).boolValue())
                    return new SEBool("#t");
            } else
                return sxpr;
            
        }
        
        return new SEBool("#f");
    }
    
    public SExpression do_not(ArrayList<SExpression> sexprs, Environment env) {
        
        if (sexprs.get(0).getClass().getName().equals("SEBool")) {
            if (!((SEBool)(sexprs.get(0))).boolValue())
                return new SEBool("#t");
            else
                return new SEBool("#f");
        } else
            return new SEBool("#t");
    }
    
    public SExpression do_assoc(ArrayList<SExpression> sexprs, Environment env) {
        SExpression return_expr = new SEBool("#f");
        if (!sexprs.get(1).getClass().getName().equals("ListCC"))
            return new ErrorMess(" assoc: not a proper list: "+sexprs.get(1));
        ListCC cu = ((ListCC)sexprs.get(1));
        while (!cu.isEmpty()) {
            if (cu.car().getClass().getName().equals("ListCC")) {
                SExpression temp = ((ListCC)cu.car()).car();
                if (sexprs.get(0).toString().equals(temp.toString()))
                    return_expr = cu.car();
            } else
                return new ErrorMess("assoc: non-pair found in list:"+cu.car().toString());
            cu = cu.cdr();
        }
        return return_expr;
    }
    
    
    
    public SExpression do_printTheSame(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).toString().equals(sexprs.get(1).toString()))
            return new SEBool("#t");
        else
            return new SEBool("#f");
    }
    
    public SExpression do_display(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).getClass().getName().equals("SEString"))
            PVTSFrame.put(((SEString)sexprs.get(0)).display());
        else if (sexprs.get(0).isSEChar())
            PVTSFrame.put(((SEChar)sexprs.get(0)).display());
        else
            PVTSFrame.put(sexprs.get(0).toString());
        
        return new Mess("(display "+ArrayListToString(sexprs)+" )");
    }
    
    public String ArrayListToString(ArrayList<SExpression> sexprs) {
        String s="";
        for (int i=0; i<sexprs.size() ; i++) {
            s = s + " " + sexprs.get(i).toString();
        }
        return s;
    }
    
    public ListCC do_list(ArrayList ln, Environment env) { // have ( list
        if (PVTS.consMode) {
            PVTS.consMode = false;
            return ListCC.makeListDotted(ln);
            
        } else
            return ListCC.makeList(ln);
    }
    
    public SExpression do_add(ArrayList<SExpression> sexprs, Environment env) {
        if (checkArgsForFloats(sexprs,env)) {
            double sum = 0;
            for (SExpression sxpr : sexprs) {
                if (sxpr.isSEFloat()) {
                    SEFloat e = (SEFloat) sxpr;
                    sum = sum + e.getValue();
                } else {
                    
                    SEInt e = (SEInt) sxpr;
                    sum = sum + e.getValue();
                }
            }
            return new SEFloat(sum);
        } else {
            
            int sum = 0;
            for (SExpression sxpr : sexprs) {
                SEInt e = (SEInt) sxpr;
                sum = sum + e.getValue();
            }
            return new SEInt(sum);
        }
    }
    
    public boolean checkArgsForFloats(ArrayList<SExpression> sexprs, Environment env) {
        boolean temp = false;
        for (SExpression sxpr : sexprs) {
            if (sxpr.isSEFloat())
                temp = true;
        }
        return temp;
    }
    
    public boolean checkArgsForNumbers(ArrayList<SExpression> sexprs, Environment env) {
        boolean temp = true;
        for (SExpression sxpr : sexprs) {
            if (!sxpr.isSENumber())
                temp = false;
        }
        return temp;
    }
    
    public SExpression do_multiplication(ArrayList<SExpression> sexprs, Environment env) {
        if (checkArgsForFloats(sexprs,env)) {
            double mul = 1;
            for (SExpression sxpr : sexprs) {
                if (sxpr.isSEFloat()) {
                    SEFloat e = (SEFloat) sxpr;
                    mul = mul * e.getValue();
                } else {
                    
                    SEInt e = (SEInt) sxpr;
                    mul = mul * e.getValue();
                }
            }
            return new SEFloat(mul);
        } else {
            
            int mul = 1;
            for (SExpression sxpr : sexprs) {
                SEInt e = (SEInt) sxpr;
                mul = mul * e.getValue();
            }
            return new SEInt(mul);
        }
    }
    
    public SExpression do_division(ArrayList<SExpression> sexprs, Environment env) {
        //int div = 1;
        double f;
        if (sexprs.get(0).isSEFloat()) {
            f = ((SEFloat) sexprs.get(0)).getValue();
        } else
            f = (double) ((SEInt) sexprs.get(0)).getValue();
        
        if (sexprs.size() == 1) {
            if (sexprs.get(0).isSEInt()) {
                if (((SEInt)sexprs.get(0)).num == 1)
                    return new SEInt(1);
            }
            return new SEFloat((double) (1 / f));
        } else {
            f = (f * f);
            for (SExpression sxpr : sexprs) {
                if (sxpr.isSEFloat()) {
                    SEFloat e = (SEFloat) sxpr;
                    f = (double) (f/e.getValue());
                } else {
                    SEInt e = (SEInt) sxpr;
                    f = (double) (f / e.getValue());
                }
                
            }
            if (((java.lang.Double)f).intValue() == ((java.lang.Double)f).doubleValue())
                return new SEInt(((java.lang.Double)f).intValue());
            else
                return new SEFloat(f);
        }
    }
    
    
    
    public SExpression do_greater(ArrayList<SExpression> sexprs, Environment env) {
        for (SExpression sxpr : sexprs) {
            if (!sxpr.isSENumber())
                return new ErrorMess(">: expects type <number>");
        }
        if (sexprs.size() > 2)
            return new SEBool("#f");
        else {
            if (sexprs.get(0).isSEFloat() && sexprs.get(1).isSEFloat()) {
                if (((SEFloat) sexprs.get(0)).getValue() > ((SEFloat) sexprs.get(1)).getValue())
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            } else if (!sexprs.get(0).isSEFloat() && sexprs.get(1).isSEFloat()) {
                if (((SEInt) sexprs.get(0)).getValue() > ((SEFloat) sexprs.get(1)).getValue())
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            } else if (sexprs.get(0).isSEFloat() && !sexprs.get(1).isSEFloat()) {
                if (((SEFloat) sexprs.get(0)).getValue() > ((SEInt) sexprs.get(1)).getValue())
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            } else {
                if (((SEInt) sexprs.get(0)).getValue() > ((SEInt) sexprs.get(1)).getValue())
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            }
            
        }
    }
    
    public SExpression do_greater_or_equal(ArrayList<SExpression> sexprs, Environment env) {
        for (SExpression sxpr : sexprs) {
            if (!sxpr.isSENumber())
                return new ErrorMess(">=: expects type <number>");
        }
        if (sexprs.size() > 2)
            return new SEBool("#f");
        else {
            if (sexprs.get(0).isSEFloat() && sexprs.get(1).isSEFloat()) {
                if (((SEFloat) sexprs.get(0)).getValue() >= ((SEFloat) sexprs.get(1)).getValue())
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            } else if (!sexprs.get(0).isSEFloat() && sexprs.get(1).isSEFloat()) {
                if (((SEInt) sexprs.get(0)).getValue() >= ((SEFloat) sexprs.get(1)).getValue())
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            } else if (sexprs.get(0).isSEFloat() && !sexprs.get(1).isSEFloat()) {
                if (((SEFloat) sexprs.get(0)).getValue() >= ((SEInt) sexprs.get(1)).getValue())
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            } else {
                if (((SEInt) sexprs.get(0)).getValue() >= ((SEInt) sexprs.get(1)).getValue())
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            }
            
        }
    }
    
    public SExpression do_less(ArrayList<SExpression> sexprs, Environment env) {
        for (SExpression sxpr : sexprs) {
            if (!sxpr.isSENumber())
                return new ErrorMess("<: expects type <number>");
        }
        if (sexprs.size() > 2)
            return new SEBool("#f");
        else {
            if (sexprs.get(0).isSEFloat() && sexprs.get(1).isSEFloat()) {
                if (((SEFloat) sexprs.get(0)).getValue() < ((SEFloat) sexprs.get(1)).getValue())
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            } else if (!sexprs.get(0).isSEFloat() && sexprs.get(1).isSEFloat()) {
                if (((SEInt) sexprs.get(0)).getValue() < ((SEFloat) sexprs.get(1)).getValue())
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            } else if (sexprs.get(0).isSEFloat() && !sexprs.get(1).isSEFloat()) {
                if (((SEFloat) sexprs.get(0)).getValue() < ((SEInt) sexprs.get(1)).getValue())
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            } else {
                if (((SEInt) sexprs.get(0)).getValue() < ((SEInt) sexprs.get(1)).getValue())
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            }
            
        }
    }
    
    public SExpression do_less_or_equal(ArrayList<SExpression> sexprs, Environment env) {
        for (SExpression sxpr : sexprs) {
            if (!sxpr.isSENumber())
                return new ErrorMess("<=: expects type <number>");
        }
        if (sexprs.size() > 2)
            return new SEBool("#f");
        else {
            if (sexprs.get(0).isSEFloat() && sexprs.get(1).isSEFloat()) {
                if (((SEFloat) sexprs.get(0)).getValue() <= ((SEFloat) sexprs.get(1)).getValue())
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            } else if (!sexprs.get(0).isSEFloat() && sexprs.get(1).isSEFloat()) {
                if (((SEInt) sexprs.get(0)).getValue() <= ((SEFloat) sexprs.get(1)).getValue())
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            } else if (sexprs.get(0).isSEFloat() && !sexprs.get(1).isSEFloat()) {
                if (((SEFloat) sexprs.get(0)).getValue() <= ((SEInt) sexprs.get(1)).getValue())
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            } else {
                if (((SEInt) sexprs.get(0)).getValue() <= ((SEInt) sexprs.get(1)).getValue())
                    return new SEBool("#t");
                else
                    return new SEBool("#f");
            }
            
        }
    }
    
    public SExpression do_equal(ArrayList<SExpression> sexprs, Environment env) {
        for (SExpression sxpr : sexprs) {
            if (!sxpr.isSENumber())
                return new ErrorMess("=: expects type <Number>");
        }
        if (sexprs.get(0).isSEFloat()) {
            double temp = ((SEFloat) sexprs.get(0)).getValue();
            for (SExpression sxpr : sexprs) {
                if (sxpr.isSEFloat()) {
                    if (((SEFloat) sxpr).getValue() != temp)
                        return new SEBool("#f");
                } else {
                    if (((SEInt) sxpr).getValue() != temp)
                        return new SEBool("#f");
                }
            }
        } else {
            int temp = ((SEInt) sexprs.get(0)).getValue();
            
            for (SExpression sxpr : sexprs) {
                if (sxpr.isSEFloat()) {
                    if (((SEFloat) sxpr).getValue() != temp)
                        return new SEBool("#f");
                } else {
                    if (((SEInt) sxpr).getValue() != temp)
                        return new SEBool("#f");
                }
            }
        }
        return new SEBool("#t");
        
    }
    
    
    public SExpression do_set_car(ArrayList<SExpression> sexprs, Environment env) {
        int tempX = ((Conscell)((ListCC) sexprs.get(0)).getFirst()).getCar().getX();
        int tempY = ((Conscell)((ListCC) sexprs.get(0)).getFirst()).getCar().getY();
        ((Conscell)((ListCC) sexprs.get(0)).getFirst()).setCar(sexprs.get(1));
        if ((((Conscell)((ListCC) sexprs.get(0)).getFirst()).getCar().getX() == 0) && (((Conscell)((ListCC) sexprs.get(0)).getFirst()).getCar().getY() == 0)) {
            ((Conscell)((ListCC) sexprs.get(0)).getFirst()).getCar().setX(tempX);
            ((Conscell)((ListCC) sexprs.get(0)).getFirst()).getCar().setY(tempY);
        }
        return new Mess("no message");
    }
    
    public SExpression do_set_cdr(ArrayList<SExpression> sexprs, Environment env) {
        if (((ListCC) sexprs.get(0)).first.isDottedPair()) {
            if (((ListCC) sexprs.get(0)).first.dotted.isListCC()) {
                PVTS.orphan_list_of_lists.add((ListCC) ((ListCC) sexprs.get(0)).first.dotted);
            }
            if (sexprs.get(1).isListCC()) {
                ((ListCC) sexprs.get(0)).first.setDotted(null);
                ((ListCC) sexprs.get(0)).addL((ListCC) sexprs.get(1));
            } else
                ((ListCC) sexprs.get(0)).first.setDotted(sexprs.get(1));
        } else {
            PVTS.orphan_list_of_lists.add(((ListCC) sexprs.get(0)).cdr());
            ((ListCC) sexprs.get(0)).removeAllButFirst();
            ((ListCC) sexprs.get(0)).first.setCdr(null);
            if (sexprs.get(1).isListCC())
                ((ListCC) sexprs.get(0)).addL((ListCC) sexprs.get(1));
            else
                ((ListCC) sexprs.get(0)).first.setDotted(sexprs.get(1));
        }
        return new Mess("no message");
    }
    
    public ListCC do_cons(ArrayList<SExpression> sexprs, Environment env) // change the return type to SExpression?   FIX ME! prolly not look at do_list
    {
        if (!sexprs.get(1).isListCC()) {
            ListCC l = new ListCC(new Conscell(sexprs.get(0),sexprs.get(1)));
            return l;
        } else {
            
            ListCC l = new ListCC(new Conscell(sexprs.get(0)));
            l.addL((ListCC) sexprs.get(1));
            return l;
        }
        
    }
    
    
    public SExpression do_null(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).getClass().getName().equals("ListCC")) {
            if (((ListCC)sexprs.get(0)).isEmpty())
                return new SEBool("#t");
            else
                return new SEBool("#f");
        } else
            return new SEBool("#f");
        
    }
    
    public SExpression do_append(ArrayList<SExpression> sexprs, Environment env) {
        
        ListCC l = new ListCC();
        if (sexprs.size() == 0)
            return l;
        else {
            int temp_start_index = 0;
            // check if the first(s) lists are empty list in order to ignore them.
            while (((ListCC) (sexprs.get(temp_start_index))).isEmpty() && temp_start_index < sexprs.size() - 1) {
                temp_start_index ++;
            }
            if (temp_start_index == sexprs.size() - 1) {
                l = ((ListCC) (sexprs.get(temp_start_index)));
                return l;
            } else {
                l = ((ListCC) (sexprs.get(temp_start_index))).copy();
                temp_start_index ++;
                for (int i = temp_start_index; i < sexprs.size() - 1; i++) {
                    if(!(sexprs.get(i).getClass().getName().equals("ListCC")))
                        return new ErrorMess("append: expects arguments of type <proper list>; given "+ sexprs.get(i).getClass().getName());
                    else {
                        if (!((ListCC) (sexprs.get(i))).isEmpty()) {
                            ListCC temp = new ListCC();
                            temp = ((ListCC) (sexprs.get(i))).copy();
                            //   temp.print();
                            l.last.setCdr((Conscell) temp.getFirst());
                            l.last = (Conscell) temp.getLast();
                            l.size = l.size + ((ListCC)sexprs.get(i)).getSize();
                        }
                    }
                    
                }
                if (!((ListCC)sexprs.get(sexprs.size()-1)).isEmpty()) {
                    l.last.setCdr((Conscell)((ListCC)sexprs.get(sexprs.size()-1)).getFirst());
                    l.last = (Conscell)((ListCC)sexprs.get(sexprs.size()-1)).getLast();
                    l.size = l.size + ((ListCC)sexprs.get(sexprs.size()-1)).getSize();
                }
                return l;
            }
        }
    }
    
    public SExpression do_minus(ArrayList<SExpression> sexprs, Environment env) {
        int sum = 0;
        if (sexprs.size() == 1)
            return new SEInt(0- ((SEInt) sexprs.get(0)).getValue());
        for (SExpression sxpr : sexprs) {
            SEInt e = (SEInt) sxpr;
            sum = sum + e.getValue();
        }
        int temp = ((SEInt) sexprs.get(0)).getValue() * 2 - sum;
        return new SEInt(temp);
    }
    
    
    public SExpression do_gcd(ArrayList<SExpression> sexprs, Environment env) {
        if (sexprs.get(0).isSEInt() && sexprs.get(1).isSEInt()) {
            int ret_Val = gcd(((SEInt)sexprs.get(0)).num, ((SEInt)sexprs.get(1)).num);
            return new SEInt(ret_Val);
        } else
            return new ErrorMess("gcd: expects type <integer> as its arguments");
        
    }
    
    public int gcd(int m, int n) {
        
        if (m < n) {
            int t = m;
            m = n;
            n = t;
        }
        
        int r = m % n;
        
        if (r == 0) {
            return n;
        } else {
            return gcd(n, r);
        }
        
    }
    
    public SExpression do_map(ArrayList<SExpression> sexprs, Environment env) {
        //  ReadEvalPrint.skipBlanks();
        ListCC return_value = new ListCC();
        //  if (((ListCC) sexprs.get(1)).isEmpty())
        //      return new ListCC();
        //  else {
        Conscell c = ((ListCC) sexprs.get(1)).first;
        while (c != null) {
            ArrayList a = new ArrayList();
            a.add(c.car);
            if (sexprs.get(0).isPrimitiveG())
                return_value.add(new Conscell(eval_primitive(a,((Function) sexprs.get(0)).prim_code,env)));
            else if (sexprs.get(0).isFunction())
                return_value.add(new Conscell(eval_procedure(a,((Procedure) sexprs.get(0)),env)));
            else
                return new ErrorMess("map: could not find the function: "+sexprs.get(0).toString());
            c = c.cdr;
        }
        //}
        return return_value;
    }
    
    public boolean isInteger(String s) {
        boolean return_val = true;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.')
                return_val = false;
        }
        return return_val;
    }
    
    public SExpression do_if(Environment env) {
        SExpression condition = parse_eval(env);
        SExpression return_exp;
        if (condition.getClass().getName().equals("SEBool")) {
            if (((SEBool)condition).boolValue() == false) {
                skipSExpression(0,0);
                if (PVTS.anotherSExpr()) {
                    //PVTS.current_pos = TextIO.pos;
                    return_exp = parse_eval(env);
                    ReadEvalPrint.skipBlanks();
                    if (TextIO.peek() == ')')
                        TextIO.getAnyChar(); // get the ')'
                    else
                        return new ErrorMess("read: expected a ')'");
                    return return_exp;
                } else {
                    if (TextIO.peek() == ')') {
                        TextIO.getAnyChar(); // get the ')'
                        return new Mess("if: done! without return value");
                    } else
                        return new ErrorMess("if: bad syntax expected an SExpression or a ')'");
                }
                
            }
        }
        ReadEvalPrint.skipBlanks();
        //PVTS.current_pos = TextIO.pos;
        return_exp = parse_eval(env);
        // put code to skip the rest of the if expression
        skipSExpression(0,0);
        ReadEvalPrint.skipBlanks();
        // check the if finishes with a right parens.
        if (TextIO.peek() == ')')
            TextIO.getAnyChar(); // get the ')'
        else
            return new ErrorMess("read: expected a ')'");
        // then return
        return return_exp;
    }
    
    public void skipSExpression(int parens,int doublequotes) {
        char dummyChar;
        String dummyWord;
        ReadEvalPrint.skipBlanks();
        char c = TextIO.peek();
        if (c == '\'') {
            TextIO.getAnyChar();
            ReadEvalPrint.skipBlanks();
            c = TextIO.peek();
        }
        if (c == '"' && (doublequotes % 2 == 1))
            TextIO.getAnyChar();
        if (c == '(' || (c =='"' && (doublequotes % 2 == 0))) {
            dummyChar = TextIO.getAnyChar();
            if (c== '(')
                parens ++;
            else
                doublequotes++;
            ReadEvalPrint.skipBlanks();
            dummyWord = TextIO.getWord();
            ReadEvalPrint.skipBlanks();
            skipSExpression(parens,doublequotes);
        } else if (c == ')' && parens > 0) {
            dummyChar = TextIO.getAnyChar();
            parens --;
            if (parens > 0) {
                ReadEvalPrint.skipBlanks();
                dummyWord = TextIO.getWord();
                ReadEvalPrint.skipBlanks();
                skipSExpression(parens,doublequotes);
            }
        } else {
            if (parens > 0) {
                ReadEvalPrint.skipBlanks();
                dummyWord = TextIO.getWord();
                ReadEvalPrint.skipBlanks();
                skipSExpression(parens,doublequotes);
            } else {
                ReadEvalPrint.skipBlanks();
                if (TextIO.peek()=='"')
                    new ErrorMess("read: expected a closing '\"'");
                dummyWord = TextIO.getWord();
                ReadEvalPrint.skipBlanks();
            }
        }
        
    }
    
    
    public SExpression do_cond(Environment env) {
        SExpression return_expr = new Mess("no message");
        while (PVTS.anotherSExpr()) {
            ReadEvalPrint.skipBlanks();
            if (TextIO.peek() != '(')
                return new ErrorMess("error: bad syntax expected '('");
            else
                TextIO.getAnyChar(); // the '('
            ReadEvalPrint.skipBlanks();
            
            if (TextIO.peekWord().equalsIgnoreCase("else")) {
                String theelse = TextIO.getWord();
                if (return_expr.getClass().getName().equals("Mess")) {
                    return_expr = PVTS.repCond();
                    TextIO.getAnyChar(); // get rid of the ')';
                    break;
                }
            } else {
                SExpression condition = parse_eval(env);
                if (condition.getClass().getName().equals("SEBool")) {
                    if (((SEBool) condition).boolValue()) {
                        return_expr = PVTS.repCond();
                        TextIO.getAnyChar(); // get rid of the ')';
                        break;
                    } else { // get rid of this condition
                        while (TextIO.peek() != ')') {
                            ReadEvalPrint.skipBlanks();
                            skipSExpression(0,0);
                        }
                        ReadEvalPrint.skipBlanks();
                        if(TextIO.peek()!= ')')
                            return new ErrorMess("read: expected ')'");
                        else {
                            TextIO.getAnyChar(); // get rid of the final ')'
                        }
                    }
                    
                } else {
                    return_expr = PVTS.repCond();
                    TextIO.getAnyChar(); // get rid of the ')';
                    break;
                }
            }
        }
        while (PVTS.anotherSExpr()) {
            skipSExpression(0,0);
        }
        if(TextIO.peek()!= ')')
            return new ErrorMess("read: expected ')'");
        else {
            TextIO.getAnyChar(); // get rid of the final ')'
            return return_expr;
        }
    }
    
    public SExpression do_let(Environment env) {
        // push a new env in the stack
        env.enviro.push(new Hashtable());
        // do the defines:
        ReadEvalPrint.skipBlanks();
        if (TextIO.peek()!= '(') return new ErrorMess("let: bad syntax in: "+TextIO.peek());
        else TextIO.getAnyChar(); // clear the '('
        while(PVTS.anotherSExpr()) {
            ReadEvalPrint.skipBlanks();
            if (TextIO.peek()!= '(') return new ErrorMess("let: bad syntax in: "+TextIO.peek());
            else TextIO.getAnyChar(); // clear the '('
            // define part:
            ReadEvalPrint.skipBlanks();
            String id = TextIO.getWord();
            ReadEvalPrint.skipBlanks();
            if (TextIO.peek() == ')') return new ErrorMess("let: unexpected ')'");
            else {
                SExpression expr = parse_eval(env);
                env.put(id,expr);
            }
            // add to the environment:
            
            ReadEvalPrint.skipBlanks();
            if (TextIO.peek() != ')') return new ErrorMess("let: missing ')'");
            else TextIO.getAnyChar();
            ReadEvalPrint.skipBlanks();
        }
        // clear the last parenthesis of the definitions.
        if (TextIO.peek() != ')') return new ErrorMess("let: missing ')'");
        else TextIO.getAnyChar();
        if (PVTS.anotherSExpr()) {
            ReadEvalPrint.skipBlanks();
            SExpression return_expr = PVTS.repLet();
            if (TextIO.peek() != ')') return new ErrorMess("let: missing ')'");
            else TextIO.getAnyChar();
            return  return_expr;
        } else return new ErrorMess("let:  bad syntax (empty body)");
        // clear the las parenthesis of the let expression
        
    }
    
    
    
    
    public SExpression eval_procedure(ArrayList<SExpression> sexprs, Procedure proc, Environment env) {
        //PVTSFrame.recuTextArea.append(Environment.convertString(TextIO.buffer.substring(PVTS.current_pos,PVTS.backup_pos)));
        //if (PVTS.current_pos != 0)
        //    PVTSFrame.recuTextArea.append(TextIO.buffer.substring(PVTS.current_pos,PVTS.backup_pos));
        //PVTS.current_SExpression = proc.body.get(proc.body.size()-1);
        //if ()
        //env.enviro.push(proc.environment);
        //Hashtable table = new Hashtable();
        Procedure newp = proc.copyReturn();
        newp.environments.push(new Hashtable());
        int temp1 = proc.environments.size();
        int temp2 = newp.environments.size();
        for (int i = 0; i < temp1 ; i++)
            env.enviro.push(proc.environments.get(i));
        for (int i = 0; i < temp2 ; i++)
            env.enviro.push(newp.environments.get(i));
        //  env.enviro.push(proc.environment);
        // env.enviro.push(newp.environment);
        //PVTS.procedure_environments.push(proc.environment);
        String newBuffer = "";
        if  (sexprs.size() != proc.parameters.size() && !proc.isVariableNumberOfParameters)
            return new ErrorMess("procedure "+proc.op+": expects "+proc.parameters.size()+" argument(s), given "+sexprs.size());
        // add to the new enviroment the parameters and their value
        else {
            if (proc.isVariableNumberOfParameters) {
                
                newBuffer = newBuffer + "(define "+ proc.parameters.get(0).toString()+" (list ";
                for (int i = 0; i<sexprs.size(); i++) {
                    newBuffer = newBuffer + sexprs.get(i).toString()+ " ";
                }
                newBuffer = newBuffer + ") ) ";
            } else {
                // insert in a new buffer the defines
                newBuffer = "";
               /* for (int i = 0; i<sexprs.size(); i++) {
                    if (sexprs.get(i).getClass().getName().equals("ListCC"))
                        newBuffer = newBuffer +"(define " + proc.parameters.get(i).toString()+" " + ((ListCC)sexprs.get(i)).toStringList() + " )";
                    else if (sexprs.get(i).getClass().getName().equals("SESymbol"))
                        newBuffer = newBuffer +"(define " + proc.parameters.get(i).toString()+" '" + sexprs.get(i).toString() + " )";
                    else
                        newBuffer = newBuffer +"(define " + proc.parameters.get(i).toString()+" " + sexprs.get(i).toString() + " )";
                }*/
                for (int i = 0; i<sexprs.size(); i++) {
                    if (newp.environments.peek().containsKey(newp.parameters.get(i).toString()))
                        newp.environments.peek().remove(newp.environments.peek().get(proc.parameters.get(i).toString()));
                    newp.environments.peek().put(newp.parameters.get(i).toString(),sexprs.get(i));
                  /*  if (sexprs.get(i).isListCC()) {
                        PVTS.list_of_lists.add(sexprs.get(i));
                        ListManager lm = new ListManager((ListCC) sexprs.get(i));
                        lm.setOffsets(PVTS.a,PVTS.b);
                        lm.manage();
                        PVTS.a = 64 + lm.giveMaxY();
                        
                    }*/
                }
            }
            // done inserting the defines
            // now add the body to be evaluated:
            newBuffer = newBuffer + " " + proc.toString();
            // save the pos of the last buffer:
            PVTS.bufferStack.pop();
            PVTS.bufferStack.push(TextIO.getPos());
            int newpos = 0;
            PVTS.bufferStack.push(newBuffer);
            PVTS.bufferStack.push(newpos);
            TextIO.fillBuffer(newBuffer,newpos);
            SExpression return_expr;
            // before evaluate push an new env.
            // env.enviro.push(new Hashtable());
            
            
            // new function called from the Global environment
            if (PVTS.current_level == 0) {
                PVTS.list_of_trees.add(new FunctionManager());
                PVTSFrame.recuTextArea.append("\n");
            }
            
            //  for (int i = 0; i<sexprs.size(); i++) {
            //      env.enviro.peek().put(proc.parameters.get(i).toString(),sexprs.get(i));
            //  }
            
            
            ProcedureAndArgs f = new ProcedureAndArgs(proc.op,sexprs);
            ((FunctionManager)PVTS.list_of_trees.getLast()).add(f, PVTS.current_level,false);
            
            
            PVTS.current_level++;
            int posInRecuTextArea=0;
            int temp01;
            int temp02;
            String s01 = PVTSFrame.recuTextArea.getText();
            // CODE for recuFrame:
            try {
                temp01 = PVTSFrame.recuTextArea.getLineCount();
                temp02 = PVTSFrame.recuTextArea.getLineStartOffset(temp01-1);
                posInRecuTextArea = PVTSFrame.recuTextArea.getText().length() - temp02;
            }
            catch (Exception e) {
                
            }
            PVTSFrame.recuTextArea.append(proc.op+"("+sexprs.toString().substring(1,sexprs.toString().length()-1)+") => ");//+" => "+PVTS.current_SExpression+"\n");
            // end code for recuFrame
            
            return_expr = PVTS.repProcedure();
            //PVTSFrame.recuTextArea.append(")");
            PVTSFrame.recuTextArea.append(return_expr+"\n");
            for (int y=1 ; y <=posInRecuTextArea; y++){
                PVTSFrame.recuTextArea.append(" ");
            }

            if (return_expr.isProcedure()) {
               /* Enumeration<String> keys = proc.environment.keys();
                Enumeration<SExpression> ele = proc.environment.elements();
                String s;
                SExpression exp;
                while (keys.hasMoreElements()) {
                    s = keys.nextElement();
                    exp = ele.nextElement();
                    if (!newp.environment.containsKey(s))
                        newp.environment.put(s,exp);
                }*/
                for (int i = 0; i < temp1 ; i++)
                    ((Procedure)return_expr).environments.push(proc.environments.get(i));
                for (int i = 0; i < temp2 ; i++)
                    ((Procedure)return_expr).environments.push(newp.environments.get(i));

            }
            
            f.result = return_expr;
            ((FunctionManager)PVTS.list_of_trees.getLast()).add(f, PVTS.current_level,true); // adds the dummy;
            
            PVTS.current_level--;
            
            for (int i = 0; i < temp1; i++)
                env.enviro.pop();
            for (int i = 0; i < temp2; i++)
                env.enviro.pop();
            
            PVTS.bufferStack.pop(); // pop the pos
            PVTS.bufferStack.pop(); // pop the String
            String b = ((String)PVTS.bufferStack.elementAt(PVTS.bufferStack.size()-2));
            int p = ((Integer)PVTS.bufferStack.elementAt(PVTS.bufferStack.size()-1));
            TextIO.fillBuffer(b,p);
            //PVTS.procedure_environments.pop();
            return return_expr;
        }
        
    }
    
    
    public SExpression define(Environment env) {
        
        // check if it is a function definition or a var def:
        ReadEvalPrint.skipBlanks();
        if (TextIO.peek() == '('){ // we got a function definition
            // do func def:
            return do_define_func(env);
        } else {// we got a var def
            // extract the identifier
            String id = TextIO.getWord();
            // extract and evaluate the value
            if (env.enviro.get(0).containsKey(id)) {
                if (env.get(id).isPrimitiveG())
                    return new ErrorMess("error: cannot overload <primitive:"+id+">");
            }
            SExpression exp = parse_eval(env);
            if (exp.isListCC()) {
                if (env.containsListCCPeek((ListCC) exp)) {
                    SExpression l = env.getListCCPeek((ListCC) exp);
                    exp = l;
                }
            }
            exp.getNameObj().addName(id);
            exp.getNameObj().setEnvironmentLevel(env.enviro.size()-1);
            if (env.containsKeyPeek(id)) {
                if (env.get(id).getClass().getName().equals("ListCC")) {
                    PVTS.list_of_lists.remove(env.get(id));
                    ((ListCC) env.get(id)).getNameObj().removeName(id);
                    PVTS.orphan_list_of_lists.add(env.get(id));
                }
                env.removePeek(id);
            }
            env.put(id,exp);
            // PUT THE DEFINITION OF THE VARIABLE INTO THE ENVIRONMENT OF THE PROCEDURE
            
            // add (var,value) to env
            ReadEvalPrint.skipBlanks();
            if (TextIO.peek() == ')')
                TextIO.getAnyChar(); // get the ')'
            else
                return new ErrorMess("define: bad syntax (multiple expressions after identifier)");
            // add the expression to the draw list:
            if (exp.getClass().getName().equals("ListCC")) // check first if its a list
            {
                PVTS.list_of_lists.add(exp);
                ListManager lm = new ListManager((ListCC) exp);
                lm.setOffsets(PVTS.a,PVTS.b);
                lm.manage();
                PVTS.a = 64 + lm.giveMaxY();
            }
            if (exp.isSESymbol())
                return new Mess("(define "+id+" '"+exp+")");
            else
                return new Mess("(define "+id+" "+exp+")");
        }
    }
    
    
    public SExpression do_lambda(Environment env) {
        Procedure f = new Procedure("lambda_func");
        ReadEvalPrint.skipBlanks();
        if (TextIO.peek() == '(') {
            TextIO.getAnyChar();
            while (TextIO.peek() != ')') {
                f.addParameter(new SESymbol(TextIO.getWord()));
                ReadEvalPrint.skipBlanks();
            }
            TextIO.getAnyChar();// get rid of the closing parens ')'
        } else {
            f.isVariableNumberOfParameters = true;
            f.addParameter(new SESymbol(TextIO.getWord()));
            ReadEvalPrint.skipBlanks();
            //new ErrorMess("read: expected a '(' for argument definitions in lambda function");
        }
        
        
        ReadEvalPrint.skipBlanks();
        PVTS.define_body_func = true;
        PVTS.quote_mode = true; // turn quote mode on so does not eval the body
        while (!PVTS.error && PVTS.moreInput() && PVTS.anotherSExpr()) {
            ReadEvalPrint.skipBlanks();
            f.addToBody(parse_eval(env));
        }
        PVTS.quote_mode = false; // turn quote mode off
        PVTS.define_body_func = false;
        ReadEvalPrint.skipBlanks();
        if (TextIO.peek() != ')') {
            PVTS.error = true;
            return new ErrorMess("read: expected a ')'");
        } else
            TextIO.getAnyChar();
        return f;
    }
    
    
    public SExpression do_define_func(Environment env) {
        
        TextIO.getAnyChar(); // get the '('
        ReadEvalPrint.skipBlanks();
        Procedure func = new Procedure(TextIO.getWord());
        
        ReadEvalPrint.skipBlanks();
        while (TextIO.peek() != ')') {
            func.addParameter(new SESymbol(TextIO.getWord()));
            ReadEvalPrint.skipBlanks();
        }
        TextIO.getAnyChar();// get rid of the closing parens ')'
        ReadEvalPrint.skipBlanks();
        PVTS.define_body_func = true;
        PVTS.quote_mode = true; // turn quote mode on so does not eval the body
        //PVTS.procedure_environments.add(func.environment);
        while (!PVTS.error && PVTS.moreInput() && PVTS.anotherSExpr()) {
            ReadEvalPrint.skipBlanks();
            func.addToBody(parse_eval(env));
        }
        // if (PVTS.procedure_environments.size() != 0)
        //         PVTS.procedure_environments.peek().put(func.getOp(),func);
        //PVTS.procedure_environments.pop();
        PVTS.quote_mode = false; // turn quote mode off
        PVTS.define_body_func = false;
        ReadEvalPrint.skipBlanks();
        if (TextIO.peek() != ')') {
            PVTS.error = true;
            return new ErrorMess("read: expected a ')'");
        } else {
            TextIO.getAnyChar(); // get rid of the ')'
            if (env.containsKeyPeek(func.getOp())) {
                env.removePeek(func.getOp());
            }
            env.put(func.getOp(),func);
            // add (var,value) to env
            ReadEvalPrint.skipBlanks();
            //PVTS.procedure_environments.pop();
            return new Mess("(define ("+func.getOp()+" "+func.parametersToString()+") "+func.toString()+")");
            
        }
        
    }
    
    public SExpression lookup_variable_value(String st, Environment env) {
        if (env.containsKey(st))
            return (SExpression) env.get(st);
        else
            return new ErrorMess("reference to undefined identifier: "+st+"@ lookup_variable_value 2501");
    }
    
    public SExpression do_quote(Environment env) {
        boolean temp;
        if (PVTS.quote_mode)
            temp = true;
        else
            temp = false;
        if (!temp)
            PVTS.quote_mode = true;
        // insert code here:
        TextIO.getAnyChar();
        //if (Character.isLetter(TextIO.peek()))
        //    PVTSFrame.put("'");
        SExpression exp = parse_eval(env);
        // done doing quote
        if (!temp)
            PVTS.quote_mode = false;
        return exp;
        
    }
    public void do_visualize(SExpression sxpr, Environment env) {
        if (!PVTS.orphan_list_of_lists.contains(sxpr) && !PVTS.list_of_lists.contains(sxpr) && sxpr.getClass().getName().equals("ListCC")) {
            PVTS.orphan_list_of_lists.add(sxpr);
            ListManager lm = new ListManager((ListCC) sxpr);
            lm.setOffsets(PVTS.a,PVTS.b);
            lm.manage();
            PVTS.a = 64 + lm.giveMaxY();
        }
    }
    
    /*public SEInt valueOf(int i) { // works only for integers
        SEInt exp = new SEInt(i);
        return exp;
    }*/
} // END Interpreter
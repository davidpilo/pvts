/*
 * ReadEvalPrint.java
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

/*
 This class loads the primitives into the global environment
 */



public class ReadEvalPrint {
    
    
    static class ParseError extends Exception {
        // Represents a syntax found in the user's input.
        ParseError(String message) {
            super(message);
        }
    } // end nested class ParseError
    
    
    
    
    static void loadPrimitives(Environment env) {  // load current collection of system primitives
        env.put("SExpression!",new Mess("SExpression!",-1));
        env.put("list",new Primitive("list",1));
        env.put("car",new Primitive("car",2));
        env.put("cdr",new Primitive("cdr",3));
        env.put("cons",new Primitive("cons",4));
        env.put("set-car!",new Primitive("set-car!",5));
        env.put("set-cdr!",new Primitive("set-cdr!",6));
        env.put("append", new Primitive("append", 7));
        env.put(">", new Primitive(">", 8));
        env.put("<", new Primitive("<",9));
        env.put(">=", new Primitive(">=",10));
        env.put("<=", new Primitive("<=",11));
        env.put("=", new Primitive("=",12));
        env.put("+",new Primitive("+",13));
        env.put("-",new Primitive("-",14));
        env.put("*",new Primitive("*",15));
        env.put("/",new Primitive("/",16));
        env.put("and",new Primitive("and",17));
        env.put("or",new Primitive("or",18));
        env.put("not",new Primitive("not",19));
        env.put("display",new Primitive("display",20));
        env.put("null?",new Primitive("null?",21));
        env.put("assoc",new Primitive("assoc",22));
        env.put("equal?",new Primitive("equal?",23));
        env.put("list?",new Primitive("list?",24));
        env.put("odd?",new Primitive("odd?",25));
        env.put("even?",new Primitive("even?",26));
        env.put("abs",new Primitive("abs",27));
        env.put("cos",new Primitive("cos",28));
        env.put("sin",new Primitive("sin",29));
        env.put("tan",new Primitive("tan",30));
        env.put("acos",new Primitive("acos",31));
        env.put("asin",new Primitive("asin",32));
        env.put("atan",new Primitive("atan",33));
        env.put("sqrt",new Primitive("sqrt",34));
        env.put("zero?",new Primitive("zero?",35));
        env.put("max",new Primitive("max",36));
        env.put("min",new Primitive("min",37));
        env.put("eq?",new Primitive("eq?",38));
        env.put("eqv?",new Primitive("eqv?",39));
        env.put("newline",new Primitive("newline",40));
        env.put("exp",new Primitive("exp",41));
        env.put("expt",new Primitive("expt",42));
        env.put("positive?",new Primitive("positive?",43));
        env.put("negative?",new Primitive("negative?",44));
        env.put("ceiling",new Primitive("ceiling",45));
        env.put("floor",new Primitive("floor",46));
        env.put("integer?",new Primitive("integer?",47));
        env.put("real?",new Primitive("real?",48));
        env.put("number?",new Primitive("number?",49));
        env.put("modulo",new Primitive("modulo",50));
        env.put("procedure?",new Primitive("procedure?",51));
        env.put("log",new Primitive("log",52));
        env.put("reverse", new Primitive("reverse",53));
        env.put("caar", new Primitive("caar",54));
        env.put("cadr", new Primitive("cadr",55));
        env.put("cdar", new Primitive("cdar",56));
        env.put("cddr", new Primitive("cddr",57));
        env.put("caaar", new Primitive("caaar",58));
        env.put("caadr", new Primitive("caadr",59));
        env.put("cadar", new Primitive("cadar",60));
        env.put("cdaar", new Primitive("cdaar",61));
        env.put("caddr", new Primitive("caddr",62));
        env.put("cdadr", new Primitive("cdadr",63));
        env.put("cddar", new Primitive("cddar",64));
        env.put("cdddr", new Primitive("cdddr",65));
        env.put("caaaar", new Primitive("caaaar",66));
        env.put("cdaaar", new Primitive("cdaaar",67));
        env.put("cadaar", new Primitive("cadaar",68));
        env.put("caadar", new Primitive("caadar",69));
        env.put("caaadr", new Primitive("caaadr",70));
        env.put("cddaar", new Primitive("cddaar",71));
        env.put("caddar", new Primitive("caddar",72));
        env.put("caaddr", new Primitive("caaddr",73));
        env.put("cadadr", new Primitive("cadadr",74));
        env.put("cdadar", new Primitive("cdadar",75));
        env.put("cdaadr", new Primitive("cdaadr",76));
        env.put("cdddar", new Primitive("cdddar",77));
        env.put("cddarr", new Primitive("cddaar",78));
        env.put("cdaddr", new Primitive("cdaddr",79));
        env.put("cadddr", new Primitive("cadddr",80));
        env.put("cddddr", new Primitive("cddddr",81));
        env.put("map",new Primitive("map",82));
        env.put("member", new Primitive("member",83));
        env.put("list-ref", new Primitive("list-ref",84));
        env.put("length", new Primitive("length",85));
        env.put("begin", new Primitive("begin",86));
        env.put("gcd",new Primitive("gcd",87));
        env.put("char?", new Primitive("char?",88));
        env.put("string?", new Primitive("string?",89));
        env.put("symbol?", new Primitive("symbol?",90));
        env.put("for-each", new Primitive("for-each",91));
        
    }
    
    static void skipBlanks() {
        // Skip past any spaces and tabs on the current line of input.
        // Stop at a non-blank character or end-of-line.
        
        while  ( TextIO.peek() == ' ' || TextIO.peek() == '\t' || TextIO.peek() == '\n' || TextIO.peek() == '\r')
            TextIO.getAnyChar();
    }
    
} // end class SimpleParser3
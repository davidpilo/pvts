/*
 * Environment.java
 *
 * Created on June 20, 2006, 11:46 AM
 *
 * Author: David Pilo
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
public class Environment {
    //public static GlobalEnv = new Hashtable();
    public static Stack<Hashtable> enviro = new Stack();
    
    
    public static String convertString(String s) {
        Environment env =  new Environment();
        String return_string = "";
        int temp1 = 0;
        char c = s.charAt(0);
        for (int i = 0; i < s.length() - 1; i++) {
            c = s.charAt(i);
            //while ((c != ' ') || (c!= '\n')  || (c!='\t') || (c!=')') || (c!='(')) {
            if(!Character.isLetter(c))
                return_string = return_string + c;
            else {
                temp1 = i;
                while (((c != ' ') && (c!= '\n')  && (c!='\t') && (c!=')') && (c!='(')) && temp1 <= s.length()) {
                    temp1++;
                    c = s.charAt(temp1);
                }
                if (env.containsKey(s.substring(i,temp1)))
                    return_string = return_string + env.get(s.substring(i,temp1));
                else {
                    return_string = return_string + s.substring(i,temp1);
                    
                    i = temp1;
                    
                }
            }
            
        }
        return return_string;
    }
    
    /** Creates a new instance of Environment */
    public Environment() {
        
        
    }
    
    public SExpression get(String id) {
        //SExpression return_exp = new ErrorMess ("");
        
        for (int i=1; i <= enviro.size(); i++) {
            if (enviro.get(enviro.size()-i).containsKey(id.toLowerCase()))
                return (SExpression) enviro.get(enviro.size()-i).get(id.toLowerCase());
            //return_exp = (SExpression) enviro.get(enviro.size()-i).get(id);
            //if (return_exp != null)
            //    return return_exp;
            
        }
        return new Mess("undefined variable "+id);
    }
    
    public int getLevel(String id) {
        
        for (int i=1; i <= enviro.size(); i++) {
            if (enviro.get(enviro.size()-i).containsKey(id.toLowerCase()))
                return enviro.size()-i;
        }
        return -1; // this should never happen since we should always call containsKey before getLevel.
    }
    
    public void put(String id, SExpression o) {
        enviro.peek().put(id.toLowerCase(),o);
        //  if (PVTS.procedure_environments.size() != 0)
        //      PVTS.procedure_environments.peek().put(id.toLowerCase(),o);
    }
    
    public void add(Hashtable h) {
        enviro.add(h);
    }
    
    public boolean containsKey(String st) {
        
        for (int i=1; i <= enviro.size(); i++) {
            if (enviro.get(enviro.size()-i).containsKey(st.toLowerCase()))
                return true;
        }
        return false;
    }
    
    public boolean containsKeyPeek(String st)  {
        return enviro.peek().containsKey(st.toLowerCase());
    }
    
    
    
    
    public boolean containsListCCPeek(ListCC e)  {
        Boolean temp = false;
        Hashtable table = enviro.peek();
        Enumeration<SExpression> ele = table.elements();
        while (ele.hasMoreElements()) {
            SExpression next = ele.nextElement();
            if (next.isListCC())
                if (((ListCC) next).isSameListAs(e))
                    temp = true;
        }
        
        return temp;
    }
    
    public ListCC getListCCPeek(ListCC e)  {
        ListCC dummy = null;
        Hashtable table;
        Enumeration<SExpression> ele;
        table = enviro.peek();
        ele = table.elements();
        while (ele.hasMoreElements()) {
            SExpression next = ele.nextElement();
            if (next.isListCC())
                if (((ListCC) next).isSameListAs(e))
                    return ((ListCC) next);
        }
        return dummy; // should never get to this point.
    }
    
    
    public void removePeek(String st) {
        enviro.peek().remove(st.toLowerCase());
    }
    
    public LinkedList getAllVariables() {
        LinkedList varList =  new LinkedList();
        for (int i = 0; i < enviro.size(); i++) {
            Enumeration ele = enviro.get(0).elements();
            Enumeration keys = enviro.get(0).keys();
            varList.add(new LinkedList());
            while (ele.hasMoreElements()) {
                SExpression e = ((SExpression) ele.nextElement());
                String id = (String) keys.nextElement();
                if ((e.isAtom() && !e.isFunction())) {
                    ((LinkedList) varList.get(0)).add(id);
                    ((LinkedList) varList.get(0)).add(e);
                }
            }
        }
        return varList;
        
    }
    
    
    public LinkedList getAllLists() {
        LinkedList varList =  new LinkedList();
        for (int i = 0; i < enviro.size(); i++) {
            Enumeration ele = enviro.get(0).elements();
            Enumeration keys = enviro.get(0).keys();
            varList.add(new LinkedList());
            while (ele.hasMoreElements()) {
                SExpression e = ((SExpression) ele.nextElement());
                String id = (String) keys.nextElement();
                if ((e.getClass().getName().equals("ListCC"))) {
                    ((LinkedList) varList.get(0)).add(id);
                    ((LinkedList) varList.get(0)).add(e);
                }
            }
        }
        return varList;
        
    }
    
    
    public LinkedList getAllPrimitives() {
        LinkedList l =  new LinkedList();
        for (int i = 0; i < enviro.size(); i++) {
            Enumeration ele = enviro.get(0).elements();
            l.add(new LinkedList());
            while (ele.hasMoreElements()) {
                SExpression e = ((SExpression) ele.nextElement());
                if (e.isPrimitiveG()) {
                    ((LinkedList) l.get(0)).add(e);
                }
            }
        }
        return l;
        
    }
    
    
    public LinkedList getAllProcedures() {
        LinkedList l =  new LinkedList();
        for (int i = 0; i < enviro.size(); i++) {
            Enumeration ele = enviro.get(0).elements();
            Enumeration keys = enviro.get(0).keys();
            l.add(new LinkedList());
            while (ele.hasMoreElements()) {
                SExpression e = ((SExpression) ele.nextElement());
                String id = (String) keys.nextElement();
                if (e.isProcedure()) {
                    ((LinkedList) l.get(0)).add(id);
                    ((LinkedList) l.get(0)).add(e);
                }
            }
        }
        return l;
        
    }
    
    
}

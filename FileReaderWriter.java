/*
 * FileReaderWriter.java
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
 * 
 */

import javax.swing.*;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;



public class FileReaderWriter {
    
    public static FileReader reader;
    public static FileWriter writer;
    public static String currentFilePath;
    
    public static void save (JTextArea ta) {
        try {
            //PVTSFrame.s
            writer = new FileWriter(currentFilePath);
            BufferedWriter buf_writer = new BufferedWriter(writer);
            String s = ta.getText();
            buf_writer.write(s);
            buf_writer.close();
            java.awt.Toolkit.getDefaultToolkit ().beep();
        }
        
        catch (IOException e) {
            PVTSFrame.outputTextArea.append(e.toString());
            System.exit(1);
        }
    }
    
    public static void saveAs(String out_file, JTextArea ta) {
        try {
        writer = new FileWriter(out_file);
        currentFilePath = out_file;
        save(ta);
        }
        catch (IOException e) {
            PVTSFrame.outputTextArea.append(e.toString());
            System.exit(1);
        }
    }
    
    public static void readFile(String in_file, JTextArea ta) {
     try {
      reader = new FileReader(in_file);
      currentFilePath = in_file;
      BufferedReader buf_reader =
                       new BufferedReader(reader);
      //FileWriter writer = new FileWriter(in_file);
      //BufferedWriter buf_writer =
      //                 new BufferedWriter(writer);
      String ln = null;
      ta.setText("");
      while ((ln = buf_reader.readLine()) != null){
        //buf_writer.write(ln);
        //buf_writer.newLine();
        ta.append(ln+"\n");
      }
      buf_reader.close();
      //buf_writer.close();
    }
    catch (IOException e) {
      System.err.println(e);
      System.exit(1);
}
    }
 
}


/*
 * PVTSFrame.java
 *
 * author: David Pilo
 *
 * Created on August 22, 2006, 7:28 PM
 
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
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.geom.*;
//import javax.swing.filechooser.*;
/**
 *
 * @author  david
 */
public class PVTSFrame extends javax.swing.JFrame {
    // my code for variables:
    
    static boolean stopStepper = false;
    JFileChooser fc = new JFileChooser();
    static BufferedReader d;
    static String textIn;
    static boolean stepper_mode = false;
    static DrawingConscellsPanel drawingCCPanel = new DrawingConscellsPanel();
    static DrawingTreesPanel drawingTPanel =  new DrawingTreesPanel();
    static DrawingVarsPanel drawingVPanel = new DrawingVarsPanel();
    static LogoPanel lpanel = new LogoPanel();
    static JScrollPane scrollPaneCC = new JScrollPane(drawingCCPanel);
    static JScrollPane scrollPaneT = new JScrollPane(drawingTPanel);
    static JScrollPane scrollPaneV = new JScrollPane(drawingVPanel);
    static JFrame fcc = new JFrame();
    static JFrame ftrees = new JFrame();
    static JFrame fenv = new JFrame();
    JButton doStepButton = new JButton("Do Step");
    static JTextField functag = new JTextField();
    static int startHighlight = 0;
    static int endHighlight = 0;

    // end of my code for variables
    
    
    /**
     * Creates new form PVTSFrame
     */
    
   
    
    public PVTSFrame() {

        
        initComponents();
        lpanel.setBackground(new java.awt.Color(255, 255, 255));
        lpanel.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        lpanel.setToolTipText("PVTS by David A. Pilo Mansion");
        lpanel.setMaximumSize(new java.awt.Dimension(32767, 80));
        lpanel.setRequestFocusEnabled(false);
        jPanel11.add(lpanel);
        recuFrame.setSize(new Dimension(400,400));
        recuTextArea.setFont(new java.awt.Font("Courier", 0, 13));
        zoomSlider.setMaximumSize(new Dimension(150,50));
        zoomSlider.setSize(new Dimension(100,50));
        scrollPaneCC.setBorder(javax.swing.BorderFactory.createTitledBorder("Cons-cells Viewer Pane:"));
        scrollPaneT.setBorder(javax.swing.BorderFactory.createTitledBorder("Function Calls Viewer Pane:"));
        //scrollPaneV.setBorder(javax.swing.BorderFactory.createTitledBorder("Global Environment Viewer Pane:"));
        // set the vertical scroll pane pixel scroll to 10 for faster scrolling:
        scrollPaneCC.getVerticalScrollBar().setUnitIncrement(40);
        scrollPaneCC.getVerticalScrollBar().setBlockIncrement(40);
        scrollPaneT.getVerticalScrollBar().setUnitIncrement(40);
        scrollPaneT.getVerticalScrollBar().setBlockIncrement(40);
        fcc.add(scrollPaneCC);
        fenv.add(scrollPaneV);
        JPanel tpanel = new JPanel();
        JPanel tpanelaux = new JPanel();
        JPanel tpanelauxnorth = new JPanel();
        tpanelauxnorth.setLayout(new BorderLayout());
        tpanelaux.setLayout(new FlowLayout());
        tpanelaux.add(zoomSlider);
        tpanelaux.add(this.funcCallPanel);
        tpanelauxnorth.add(new JLabel("Current Function Call: "),BorderLayout.WEST);
        tpanelauxnorth.add(functag,BorderLayout.CENTER);
        tpanel.setLayout(new BorderLayout());
        tpanel.add(scrollPaneT,BorderLayout.CENTER);
        tpanel.add(new JScrollPane(tpanelaux),BorderLayout.SOUTH);
        tpanel.add(tpanelauxnorth, BorderLayout.NORTH);
        ftrees.add(tpanel);
        this.setSize(new Dimension(640,600));
        this.setLocation(420,150);
        
        fenv.setSize(new Dimension(320,240));
        fenv.setTitle("Environment Viewer");
        fenv.setLocation(320,0);
        ftrees.setSize(new Dimension(320,240));
        ftrees.setTitle("Function Calls Viewer");
        ftrees.setLocation(0,260);
        fcc.setSize(new Dimension(320,240));
        fcc.setTitle("Cons-cells Viewer");
        //this.scrollPaneT.getVerticalScrollBar().setBlockIncrement(100);
        this.ccColorPanel.setBackground(DrawingConscellsPanel.color);
        this.ccBColorPanel.setBackground(DrawingConscellsPanel.bcolor);
        this.fColorPanel.setBackground(DrawingTreesPanel.color);
        this.fBColorPanel.setBackground(DrawingTreesPanel.bcolor);
        
        doStepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doStepButtonActionPerformed(evt);
            }
        });
        
        fcc.addWindowListener(new java.awt.event.WindowListener() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                
            }
            public void windowActivated(java.awt.event.WindowEvent evt) {
                
            }
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                
            }
            public void windowIconified(java.awt.event.WindowEvent evt) {
                
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                ccToggleButton.setSelected(false);
                ccBoxMenuItem.setSelected(false);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                
            }
            
        });
        ftrees.addWindowListener(new java.awt.event.WindowListener() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                
            }
            public void windowActivated(java.awt.event.WindowEvent evt) {
                
            }
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                
            }
            public void windowIconified(java.awt.event.WindowEvent evt) {
                
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                treesToggleButton.setSelected(false);
                treesBoxMenuItem.setSelected(false);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                
            }
            
        });
        fenv.addWindowListener(new java.awt.event.WindowListener() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                
            }
            public void windowActivated(java.awt.event.WindowEvent evt) {
                
            }
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                
            }
            public void windowIconified(java.awt.event.WindowEvent evt) {
                
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                envToggleButton.setSelected(false);
                envBoxMenuItem.setSelected(false);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                
            }
            
        });
        
        
    }
    
    // HIGHLIGHT CODE:
    
    
    
    public void highlightParenthesis(JTextComponent textComp, Integer loc, int currentpos) {
        // First remove all old highlights
        removeHighlights(textComp);
        
        try {
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            
            hilite.addHighlight((int)loc,(int) loc +1,myHighlightPainter2);
            hilite.addHighlight(currentpos-1,currentpos,myHighlightPainter2);
            
        } catch (BadLocationException e) {
        }
    }
    
    
    public void highlightCurrentSExpression(JTextComponent textComp, int a, int b) {
        // First remove all old highlights
        removeHighlights(textComp);
        
        try {
            Highlighter hilite = textComp.getHighlighter();
            
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            
            hilite.addHighlight(a,b,myHighlightPainter);
            
        } catch (BadLocationException e) {
        }
    }
    
    
    public void repaintAllVisuals() {
        fcc.repaint();
        ftrees.repaint();
        fenv.repaint();
    }
    
    public void setParenthesisStack() {
        PVTS.parenthesisLocStack.removeAllElements();
        int currentpos = inputTextArea.getCaretPosition();
        //PVTSFrame.put(inputTextArea.getCaretPosition());
        String s = inputTextArea.getText();
        char c;
        for (int i = 0; i < currentpos-1; i++) {
            c = s.charAt(i);
            if (c == '(')
                PVTS.parenthesisLocStack.push((Integer)i);
            if (c == ')' && !PVTS.parenthesisLocStack.isEmpty())
                PVTS.parenthesisLocStack.pop();
        }
    }
    
    
    public void removeHighlights(JTextComponent textComp) {
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();
        
        for (int i=0; i<hilites.length; i++) {
            if (hilites[i].getPainter() instanceof MyHighlightPainter) {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }
    
    // An instance of the private subclass of the default highlight painter
    Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.orange);
    Highlighter.HighlightPainter myHighlightPainter2 = new MyHighlightPainter(Color.pink);
    
    // A private subclass of the default highlight painter
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter(Color color) {
            super(color);
        }
    }
    // END OF HIGHLIGHT CODE
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        optFrame = new javax.swing.JFrame();
        optionsPanel = new javax.swing.JPanel();
        colorPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        ccColorPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        fColorPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        ccBColorPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        fBColorPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        envPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        listCheckBox = new javax.swing.JCheckBox();
        varCheckBox = new javax.swing.JCheckBox();
        procCheckBox = new javax.swing.JCheckBox();
        primCheckBox = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        fontSizeComboBox = new javax.swing.JComboBox();
        lineWrapCheckBox = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        tabSizeComboBox = new javax.swing.JComboBox();
        zoomSlider = new javax.swing.JSlider();
        funcCallPanel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        alignmentComboBox = new javax.swing.JComboBox();
        recuButton = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        funcNameDisplay = new javax.swing.JCheckBox();
        result_bubbleCheckBox = new javax.swing.JCheckBox();
        aboutFrame = new javax.swing.JFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        readFrame = new javax.swing.JFrame();
        jLabel7 = new javax.swing.JLabel();
        readTextField = new javax.swing.JTextField();
        readButton = new javax.swing.JButton();
        ccColorFrame = new javax.swing.JFrame();
        ccColorColorChooser = new javax.swing.JColorChooser();
        fColorFrame = new javax.swing.JFrame();
        fColorColorChooser = new javax.swing.JColorChooser();
        ccBColorFrame = new javax.swing.JFrame();
        ccBColorColorChooser = new javax.swing.JColorChooser();
        fBColorFrame = new javax.swing.JFrame();
        fBColorColorChooser = new javax.swing.JColorChooser();
        recuFrame = new javax.swing.JFrame();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        recuTextArea = new javax.swing.JTextArea();
        jSplitPane1 = new javax.swing.JSplitPane();
        inputPanel = new javax.swing.JPanel();
        mainButtonsPanel = new javax.swing.JPanel();
        controlsPanel = new javax.swing.JPanel();
        stepperButton = new javax.swing.JToggleButton();
        runButton = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        ccToggleButton = new javax.swing.JToggleButton();
        treesToggleButton = new javax.swing.JToggleButton();
        envToggleButton = new javax.swing.JToggleButton();
        inputScrollPane = new javax.swing.JScrollPane();
        inputTextArea = new javax.swing.JTextArea();
        outputPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        outputTextArea = new javax.swing.JTextArea();
        jPanel11 = new javax.swing.JPanel();
        lnColTextField = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        closeMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        printMenu = new javax.swing.JMenu();
        printInputMenuItem = new javax.swing.JMenuItem();
        printOutputMenuItem = new javax.swing.JMenuItem();
        printCCMenuItem = new javax.swing.JMenuItem();
        printTreesMenuItem = new javax.swing.JMenuItem();
        printVarsMenuItem = new javax.swing.JMenuItem();
        printRecuMenuItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        quitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        ccBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        treesBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        envBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        mosaicMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        optMenuItem = new javax.swing.JMenuItem();
        examplesMenu = new javax.swing.JMenu();
        conscellsMenuItem = new javax.swing.JMenuItem();
        appendMenuItem = new javax.swing.JMenuItem();
        factorialMenuItem = new javax.swing.JMenuItem();
        fibonacciMenuItem = new javax.swing.JMenuItem();
        baMenuItem = new javax.swing.JMenuItem();
        sortingMenuItem = new javax.swing.JMenuItem();
        setStructureMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();
        langMenuItem = new javax.swing.JMenuItem();
        updateMenuItem = new javax.swing.JMenuItem();

        optFrame.setTitle("Preferences");
        optionsPanel.setLayout(new javax.swing.BoxLayout(optionsPanel, javax.swing.BoxLayout.Y_AXIS));

        colorPanel.setLayout(new javax.swing.BoxLayout(colorPanel, javax.swing.BoxLayout.Y_AXIS));

        colorPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Color Options:"));
        colorPanel.setMaximumSize(new java.awt.Dimension(10000, 140));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(1000, 27));
        jPanel1.setPreferredSize(new java.awt.Dimension(186, 27));
        ccColorPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        ccColorPanel.setToolTipText("Changes the color of the cons-cells");
        ccColorPanel.setMaximumSize(new java.awt.Dimension(50, 25));
        ccColorPanel.setMinimumSize(new java.awt.Dimension(50, 25));
        ccColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ccColorPanelMouseReleased(evt);
            }
        });

        jPanel1.add(ccColorPanel);

        jLabel2.setText("   Cons-cells color");
        jPanel1.add(jLabel2);

        colorPanel.add(jPanel1);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.X_AXIS));

        jPanel2.setMaximumSize(new java.awt.Dimension(1000, 27));
        fColorPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        fColorPanel.setToolTipText("Changes the color of the function calls");
        fColorPanel.setMaximumSize(new java.awt.Dimension(50, 25));
        fColorPanel.setMinimumSize(new java.awt.Dimension(50, 25));
        fColorPanel.setPreferredSize(new java.awt.Dimension(50, 25));
        fColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fColorPanelMouseReleased(evt);
            }
        });

        jPanel2.add(fColorPanel);

        jLabel3.setText("   Function calls color");
        jPanel2.add(jLabel3);

        colorPanel.add(jPanel2);

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.X_AXIS));

        jPanel3.setMaximumSize(new java.awt.Dimension(1000, 27));
        ccBColorPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        ccBColorPanel.setToolTipText("Changes the color of the cons-cells background");
        ccBColorPanel.setMaximumSize(new java.awt.Dimension(50, 25));
        ccBColorPanel.setMinimumSize(new java.awt.Dimension(50, 25));
        ccBColorPanel.setPreferredSize(new java.awt.Dimension(50, 25));
        ccBColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ccBColorPanelMouseReleased(evt);
            }
        });

        jPanel3.add(ccBColorPanel);

        jLabel1.setText("   Cons-cells Background color");
        jPanel3.add(jLabel1);

        colorPanel.add(jPanel3);

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.X_AXIS));

        jPanel4.setMaximumSize(new java.awt.Dimension(1000, 27));
        fBColorPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        fBColorPanel.setToolTipText("Changes the color of the function calls background");
        fBColorPanel.setMaximumSize(new java.awt.Dimension(50, 25));
        fBColorPanel.setMinimumSize(new java.awt.Dimension(50, 25));
        fBColorPanel.setPreferredSize(new java.awt.Dimension(50, 25));
        fBColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fBColorPanelMouseReleased(evt);
            }
        });

        jPanel4.add(fBColorPanel);

        jLabel6.setText("   Functions calls Background color");
        jPanel4.add(jLabel6);

        colorPanel.add(jPanel4);

        optionsPanel.add(colorPanel);

        envPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        envPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Environment Options:"));
        envPanel.setMaximumSize(new java.awt.Dimension(10000, 116));
        envPanel.setPreferredSize(new java.awt.Dimension(500, 116));
        jLabel4.setText("Viewable Elements:");
        envPanel.add(jLabel4);

        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.Y_AXIS));

        listCheckBox.setSelected(true);
        listCheckBox.setText("Lists Variables Table");
        listCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        listCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        listCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listCheckBoxActionPerformed(evt);
            }
        });

        jPanel5.add(listCheckBox);

        varCheckBox.setSelected(true);
        varCheckBox.setText("Other Variables Table");
        varCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        varCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        varCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                varCheckBoxActionPerformed(evt);
            }
        });

        jPanel5.add(varCheckBox);

        procCheckBox.setSelected(true);
        procCheckBox.setText("Procedures Table");
        procCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        procCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        procCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                procCheckBoxActionPerformed(evt);
            }
        });

        jPanel5.add(procCheckBox);

        primCheckBox.setText("Primitives Table");
        primCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        primCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        primCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                primCheckBoxActionPerformed(evt);
            }
        });

        jPanel5.add(primCheckBox);

        envPanel.add(jPanel5);

        optionsPanel.add(envPanel);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Font:"));
        jLabel8.setText("Font size:");
        jPanel10.add(jLabel8);

        fontSizeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "8", "10", "11", "12", "13", "14", "16", "18", "20", "22", "24", "26", "30" }));
        fontSizeComboBox.setSelectedIndex(4);
        fontSizeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontSizeComboBoxActionPerformed(evt);
            }
        });

        jPanel10.add(fontSizeComboBox);

        lineWrapCheckBox.setSelected(true);
        lineWrapCheckBox.setText("Line Wrap");
        lineWrapCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lineWrapCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        lineWrapCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lineWrapCheckBoxActionPerformed(evt);
            }
        });

        jPanel10.add(lineWrapCheckBox);

        jLabel9.setText("   Tab Size");
        jPanel10.add(jLabel9);

        tabSizeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        tabSizeComboBox.setSelectedIndex(1);
        tabSizeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tabSizeComboBoxActionPerformed(evt);
            }
        });

        jPanel10.add(tabSizeComboBox);

        optionsPanel.add(jPanel10);

        optFrame.getContentPane().add(optionsPanel, java.awt.BorderLayout.CENTER);

        zoomSlider.setMajorTickSpacing(5);
        zoomSlider.setMaximum(10);
        zoomSlider.setMinorTickSpacing(1);
        zoomSlider.setPaintLabels(true);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setSnapToTicks(true);
        zoomSlider.setToolTipText("Use it to zoom in and out of the graphs");
        zoomSlider.setValue(4);
        zoomSlider.setBorder(javax.swing.BorderFactory.createTitledBorder("Function calls zoom:"));
        zoomSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                zoomSliderStateChanged(evt);
            }
        });

        funcCallPanel.setLayout(new javax.swing.BoxLayout(funcCallPanel, javax.swing.BoxLayout.Y_AXIS));

        funcCallPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Function Calls Options:"));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel5.setText("View modes:");
        jPanel8.add(jLabel5);

        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.Y_AXIS));

        alignmentComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Center Aligned (wide)", "Center Aligned (narrow)", "Left Aligned (wide)", "Left Aligned (narrow)" }));
        alignmentComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alignmentComboBoxActionPerformed(evt);
            }
        });

        jPanel6.add(alignmentComboBox);

        jPanel8.add(jPanel6);

        recuButton.setText("Trace");
        recuButton.setToolTipText("Displays the trace in text format");
        recuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recuButtonActionPerformed(evt);
            }
        });

        jPanel8.add(recuButton);

        funcCallPanel.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        funcNameDisplay.setSelected(true);
        funcNameDisplay.setText("Trim function names");
        funcNameDisplay.setToolTipText("If checked, it only shows the first letter of the function names");
        funcNameDisplay.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        funcNameDisplay.setMargin(new java.awt.Insets(0, 0, 0, 0));
        funcNameDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                funcNameDisplayActionPerformed(evt);
            }
        });

        jPanel9.add(funcNameDisplay);

        result_bubbleCheckBox.setSelected(true);
        result_bubbleCheckBox.setText("Show results");
        result_bubbleCheckBox.setToolTipText("Shows the resuts of each function call to the right of the oval");
        result_bubbleCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        result_bubbleCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        result_bubbleCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                result_bubbleCheckBoxActionPerformed(evt);
            }
        });

        jPanel9.add(result_bubbleCheckBox);

        funcCallPanel.add(jPanel9);

        aboutFrame.setTitle("About PVTS");
        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("\n    PVTS version 0.243, Copyright (C) 2007 David A. Pilo Mansion\n    PVTS comes with ABSOLUTELY NO WARRANTY; for details see the help menu.\n    This is free software, and you are welcome to redistribute it\n    under certain conditions; see the help menu for details.\n\nAuthor:\nDavid A. Pilo Mansion.\nBachelor of Arts in Mathematics and Computer Science at Rollins College, Winter Park, FL. (May 2007).\n\nContact info:\nemail: davidpilo@gmail.com\nwebsite: www.davidpilo.com/pvts/\n\nIf you have any suggestions for my programs or found any bugs feel free to contact me.\n\n\t    GNU GENERAL PUBLIC LICENSE\n\t       Version 2, June 1991\n\n Copyright (C) 1989, 1991 Free Software Foundation, Inc.,\n 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA\n Everyone is permitted to copy and distribute verbatim copies\n of this license document, but changing it is not allowed.\n\n\t\t    Preamble\n\n  The licenses for most software are designed to take away your\nfreedom to share and change it.  By contrast, the GNU General Public\nLicense is intended to guarantee your freedom to share and change free\nsoftware--to make sure the software is free for all its users.  This\nGeneral Public License applies to most of the Free Software\nFoundation's software and to any other program whose authors commit to\nusing it.  (Some other Free Software Foundation software is covered by\nthe GNU Lesser General Public License instead.)  You can apply it to\nyour programs, too.\n\n  When we speak of free software, we are referring to freedom, not\nprice.  Our General Public Licenses are designed to make sure that you\nhave the freedom to distribute copies of free software (and charge for\nthis service if you wish), that you receive source code or can get it\nif you want it, that you can change the software or use pieces of it\nin new free programs; and that you know you can do these things.\n\n  To protect your rights, we need to make restrictions that forbid\nanyone to deny you these rights or to ask you to surrender the rights.\nThese restrictions translate to certain responsibilities for you if you\ndistribute copies of the software, or if you modify it.\n\n  For example, if you distribute copies of such a program, whether\ngratis or for a fee, you must give the recipients all the rights that\nyou have.  You must make sure that they, too, receive or can get the\nsource code.  And you must show them these terms so they know their\nrights.\n\n  We protect your rights with two steps: (1) copyright the software, and\n(2) offer you this license which gives you legal permission to copy,\ndistribute and/or modify the software.\n\n  Also, for each author's protection and ours, we want to make certain\nthat everyone understands that there is no warranty for this free\nsoftware.  If the software is modified by someone else and passed on, we\nwant its recipients to know that what they have is not the original, so\nthat any problems introduced by others will not reflect on the original\nauthors' reputations.\n\n  Finally, any free program is threatened constantly by software\npatents.  We wish to avoid the danger that redistributors of a free\nprogram will individually obtain patent licenses, in effect making the\nprogram proprietary.  To prevent this, we have made it clear that any\npatent must be licensed for everyone's free use or not licensed at all.\n\n  The precise terms and conditions for copying, distribution and\nmodification follow.\n\n\t    GNU GENERAL PUBLIC LICENSE\n   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION\n\n  0. This License applies to any program or other work which contains\na notice placed by the copyright holder saying it may be distributed\nunder the terms of this General Public License.  The \"Program\", below,\nrefers to any such program or work, and a \"work based on the Program\"\nmeans either the Program or any derivative work under copyright law:\nthat is to say, a work containing the Program or a portion of it,\neither verbatim or with modifications and/or translated into another\nlanguage.  (Hereinafter, translation is included without limitation in\nthe term \"modification\".)  Each licensee is addressed as \"you\".\n\nActivities other than copying, distribution and modification are not\ncovered by this License; they are outside its scope.  The act of\nrunning the Program is not restricted, and the output from the Program\nis covered only if its contents constitute a work based on the\nProgram (independent of having been made by running the Program).\nWhether that is true depends on what the Program does.\n\n  1. You may copy and distribute verbatim copies of the Program's\nsource code as you receive it, in any medium, provided that you\nconspicuously and appropriately publish on each copy an appropriate\ncopyright notice and disclaimer of warranty; keep intact all the\nnotices that refer to this License and to the absence of any warranty;\nand give any other recipients of the Program a copy of this License\nalong with the Program.\n\nYou may charge a fee for the physical act of transferring a copy, and\nyou may at your option offer warranty protection in exchange for a fee.\n\n  2. You may modify your copy or copies of the Program or any portion\nof it, thus forming a work based on the Program, and copy and\ndistribute such modifications or work under the terms of Section 1\nabove, provided that you also meet all of these conditions:\n\n    a) You must cause the modified files to carry prominent notices\n    stating that you changed the files and the date of any change.\n\n    b) You must cause any work that you distribute or publish, that in\n    whole or in part contains or is derived from the Program or any\n    part thereof, to be licensed as a whole at no charge to all third\n    parties under the terms of this License.\n\n    c) If the modified program normally reads commands interactively\n    when run, you must cause it, when started running for such\n    interactive use in the most ordinary way, to print or display an\n    announcement including an appropriate copyright notice and a\n    notice that there is no warranty (or else, saying that you provide\n    a warranty) and that users may redistribute the program under\n    these conditions, and telling the user how to view a copy of this\n    License.  (Exception: if the Program itself is interactive but\n    does not normally print such an announcement, your work based on\n    the Program is not required to print an announcement.)\n\nThese requirements apply to the modified work as a whole.  If\nidentifiable sections of that work are not derived from the Program,\nand can be reasonably considered independent and separate works in\nthemselves, then this License, and its terms, do not apply to those\nsections when you distribute them as separate works.  But when you\ndistribute the same sections as part of a whole which is a work based\non the Program, the distribution of the whole must be on the terms of\nthis License, whose permissions for other licensees extend to the\nentire whole, and thus to each and every part regardless of who wrote it.\n\nThus, it is not the intent of this section to claim rights or contest\nyour rights to work written entirely by you; rather, the intent is to\nexercise the right to control the distribution of derivative or\ncollective works based on the Program.\n\nIn addition, mere aggregation of another work not based on the Program\nwith the Program (or with a work based on the Program) on a volume of\na storage or distribution medium does not bring the other work under\nthe scope of this License.\n\n  3. You may copy and distribute the Program (or a work based on it,\nunder Section 2) in object code or executable form under the terms of\nSections 1 and 2 above provided that you also do one of the following:\n\n    a) Accompany it with the complete corresponding machine-readable\n    source code, which must be distributed under the terms of Sections\n    1 and 2 above on a medium customarily used for software interchange; or,\n\n    b) Accompany it with a written offer, valid for at least three\n    years, to give any third party, for a charge no more than your\n    cost of physically performing source distribution, a complete\n    machine-readable copy of the corresponding source code, to be\n    distributed under the terms of Sections 1 and 2 above on a medium\n    customarily used for software interchange; or,\n\n    c) Accompany it with the information you received as to the offer\n    to distribute corresponding source code.  (This alternative is\n    allowed only for noncommercial distribution and only if you\n    received the program in object code or executable form with such\n    an offer, in accord with Subsection b above.)\n\nThe source code for a work means the preferred form of the work for\nmaking modifications to it.  For an executable work, complete source\ncode means all the source code for all modules it contains, plus any\nassociated interface definition files, plus the scripts used to\ncontrol compilation and installation of the executable.  However, as a\nspecial exception, the source code distributed need not include\nanything that is normally distributed (in either source or binary\nform) with the major components (compiler, kernel, and so on) of the\noperating system on which the executable runs, unless that component\nitself accompanies the executable.\n\nIf distribution of executable or object code is made by offering\naccess to copy from a designated place, then offering equivalent\naccess to copy the source code from the same place counts as\ndistribution of the source code, even though third parties are not\ncompelled to copy the source along with the object code.\n\n  4. You may not copy, modify, sublicense, or distribute the Program\nexcept as expressly provided under this License.  Any attempt\notherwise to copy, modify, sublicense or distribute the Program is\nvoid, and will automatically terminate your rights under this License.\nHowever, parties who have received copies, or rights, from you under\nthis License will not have their licenses terminated so long as such\nparties remain in full compliance.\n\n  5. You are not required to accept this License, since you have not\nsigned it.  However, nothing else grants you permission to modify or\ndistribute the Program or its derivative works.  These actions are\nprohibited by law if you do not accept this License.  Therefore, by\nmodifying or distributing the Program (or any work based on the\nProgram), you indicate your acceptance of this License to do so, and\nall its terms and conditions for copying, distributing or modifying\nthe Program or works based on it.\n\n  6. Each time you redistribute the Program (or any work based on the\nProgram), the recipient automatically receives a license from the\noriginal licensor to copy, distribute or modify the Program subject to\nthese terms and conditions.  You may not impose any further\nrestrictions on the recipients' exercise of the rights granted herein.\nYou are not responsible for enforcing compliance by third parties to\nthis License.\n\n  7. If, as a consequence of a court judgment or allegation of patent\ninfringement or for any other reason (not limited to patent issues),\nconditions are imposed on you (whether by court order, agreement or\notherwise) that contradict the conditions of this License, they do not\nexcuse you from the conditions of this License.  If you cannot\ndistribute so as to satisfy simultaneously your obligations under this\nLicense and any other pertinent obligations, then as a consequence you\nmay not distribute the Program at all.  For example, if a patent\nlicense would not permit royalty-free redistribution of the Program by\nall those who receive copies directly or indirectly through you, then\nthe only way you could satisfy both it and this License would be to\nrefrain entirely from distribution of the Program.\n\nIf any portion of this section is held invalid or unenforceable under\nany particular circumstance, the balance of the section is intended to\napply and the section as a whole is intended to apply in other\ncircumstances.\n\nIt is not the purpose of this section to induce you to infringe any\npatents or other property right claims or to contest validity of any\nsuch claims; this section has the sole purpose of protecting the\nintegrity of the free software distribution system, which is\nimplemented by public license practices.  Many people have made\ngenerous contributions to the wide range of software distributed\nthrough that system in reliance on consistent application of that\nsystem; it is up to the author/donor to decide if he or she is willing\nto distribute software through any other system and a licensee cannot\nimpose that choice.\n\nThis section is intended to make thoroughly clear what is believed to\nbe a consequence of the rest of this License.\n\n  8. If the distribution and/or use of the Program is restricted in\ncertain countries either by patents or by copyrighted interfaces, the\noriginal copyright holder who places the Program under this License\nmay add an explicit geographical distribution limitation excluding\nthose countries, so that distribution is permitted only in or among\ncountries not thus excluded.  In such case, this License incorporates\nthe limitation as if written in the body of this License.\n\n  9. The Free Software Foundation may publish revised and/or new versions\nof the General Public License from time to time.  Such new versions will\nbe similar in spirit to the present version, but may differ in detail to\naddress new problems or concerns.\n\nEach version is given a distinguishing version number.  If the Program\nspecifies a version number of this License which applies to it and \"any\nlater version\", you have the option of following the terms and conditions\neither of that version or of any later version published by the Free\nSoftware Foundation.  If the Program does not specify a version number of\nthis License, you may choose any version ever published by the Free Software\nFoundation.\n\n  10. If you wish to incorporate parts of the Program into other free\nprograms whose distribution conditions are different, write to the author\nto ask for permission.  For software which is copyrighted by the Free\nSoftware Foundation, write to the Free Software Foundation; we sometimes\nmake exceptions for this.  Our decision will be guided by the two goals\nof preserving the free status of all derivatives of our free software and\nof promoting the sharing and reuse of software generally.\n\n\t\t    NO WARRANTY\n\n  11. BECAUSE THE PROGRAM IS LICENSED FREE OF CHARGE, THERE IS NO WARRANTY\nFOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE LAW.  EXCEPT WHEN\nOTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER PARTIES\nPROVIDE THE PROGRAM \"AS IS\" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED\nOR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF\nMERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.  THE ENTIRE RISK AS\nTO THE QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU.  SHOULD THE\nPROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING,\nREPAIR OR CORRECTION.\n\n  12. IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING\nWILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MAY MODIFY AND/OR\nREDISTRIBUTE THE PROGRAM AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES,\nINCLUDING ANY GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING\nOUT OF THE USE OR INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED\nTO LOSS OF DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY\nYOU OR THIRD PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE WITH ANY OTHER\nPROGRAMS), EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE\nPOSSIBILITY OF SUCH DAMAGES.\n\n\t     END OF TERMS AND CONDITIONS\n\n   \n\n    Pilo's Visualization Tools for Scheme (PVTS) version 0.242. A Basic Visual Scheme Interpreter. \n    Copyright (C) 2007  David A. Pilo Mansion\n\n    This program is free software; you can redistribute it and/or modify\n    it under the terms of the GNU General Public License as published by\n    the Free Software Foundation; either version 2 of the License, or\n    (at your option) any later version.\n\n    This program is distributed in the hope that it will be useful,\n    but WITHOUT ANY WARRANTY; without even the implied warranty of\n    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n    GNU General Public License for more details.\n\n    You should have received a copy of the GNU General Public License along\n    with this program; if not, write to the Free Software Foundation, Inc.,\n    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.\n\nContact information:\nDavid Pilo\ndavidpilo@gmail.com\n    ");
        jScrollPane1.setViewportView(jTextArea1);

        aboutFrame.getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        readFrame.getContentPane().setLayout(new java.awt.FlowLayout());

        readFrame.setTitle("Input from (read)");
        jLabel7.setText("Input:");
        readFrame.getContentPane().add(jLabel7);

        readTextField.setPreferredSize(new java.awt.Dimension(180, 22));
        readFrame.getContentPane().add(readTextField);

        readButton.setText("Accept");
        readButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readButtonActionPerformed(evt);
            }
        });

        readFrame.getContentPane().add(readButton);

        ccColorFrame.setTitle("Conscells Color Chooser");
        ccColorColorChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                ccColorColorChooserPropertyChange(evt);
            }
        });

        ccColorFrame.getContentPane().add(ccColorColorChooser, java.awt.BorderLayout.CENTER);

        fColorFrame.setTitle("Conscells Color Chooser");
        fColorColorChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fColorColorChooserPropertyChange(evt);
            }
        });

        fColorFrame.getContentPane().add(fColorColorChooser, java.awt.BorderLayout.CENTER);

        ccBColorFrame.setTitle("Conscells Color Chooser");
        ccBColorColorChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                ccBColorColorChooserPropertyChange(evt);
            }
        });

        ccBColorFrame.getContentPane().add(ccBColorColorChooser, java.awt.BorderLayout.CENTER);

        fBColorFrame.setTitle("Conscells Color Chooser");
        fBColorColorChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fBColorColorChooserPropertyChange(evt);
            }
        });

        fBColorFrame.getContentPane().add(fBColorColorChooser, java.awt.BorderLayout.CENTER);

        recuFrame.setTitle("Trace");
        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Trace Displayer"));
        recuTextArea.setColumns(20);
        recuTextArea.setEditable(false);
        recuTextArea.setRows(5);
        jScrollPane3.setViewportView(recuTextArea);

        jPanel12.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        recuFrame.getContentPane().add(jPanel12, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("(untitled) - Pilo's Visual Tools for Scheme");
        jSplitPane1.setDividerLocation(375);
        jSplitPane1.setDividerSize(10);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        inputPanel.setLayout(new java.awt.BorderLayout());

        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        mainButtonsPanel.setLayout(new javax.swing.BoxLayout(mainButtonsPanel, javax.swing.BoxLayout.X_AXIS));

        mainButtonsPanel.setMinimumSize(new java.awt.Dimension(200, 30));
        controlsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Controls:"));
        controlsPanel.setMinimumSize(new java.awt.Dimension(0, 60));
        controlsPanel.setPreferredSize(new java.awt.Dimension(200, 60));
        stepperButton.setText("Start Stepper");
        stepperButton.setToolTipText("Starts the Stepper");
        stepperButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stepperButtonActionPerformed(evt);
            }
        });

        controlsPanel.add(stepperButton);

        runButton.setText("Run");
        runButton.setToolTipText("Runs the Interpreter");
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });

        controlsPanel.add(runButton);

        mainButtonsPanel.add(controlsPanel);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Viewers:"));
        jPanel7.setMinimumSize(new java.awt.Dimension(0, 60));
        jPanel7.setPreferredSize(new java.awt.Dimension(338, 60));
        ccToggleButton.setText("Cons-cells");
        ccToggleButton.setToolTipText("Toggles on/off Conscells Viewer");
        ccToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ccToggleButtonActionPerformed(evt);
            }
        });

        jPanel7.add(ccToggleButton);

        treesToggleButton.setText("Function Calls");
        treesToggleButton.setToolTipText("Toggles on/off Function Calls Viewer");
        treesToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                treesToggleButtonActionPerformed(evt);
            }
        });

        jPanel7.add(treesToggleButton);

        envToggleButton.setText("Enviroment");
        envToggleButton.setToolTipText("Toggles on/off Environment Viewer");
        envToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                envToggleButtonActionPerformed(evt);
            }
        });

        jPanel7.add(envToggleButton);

        mainButtonsPanel.add(jPanel7);

        inputPanel.add(mainButtonsPanel, java.awt.BorderLayout.SOUTH);

        inputTextArea.setColumns(20);
        inputTextArea.setFont(new java.awt.Font("Courier", 0, 13));
        inputTextArea.setLineWrap(true);
        inputTextArea.setRows(5);
        inputTextArea.setTabSize(2);
        inputTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputTextAreaKeyReleased(evt);
            }
        });
        inputTextArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                inputTextAreaMouseReleased(evt);
            }
        });

        inputScrollPane.setViewportView(inputTextArea);

        inputPanel.add(inputScrollPane, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(inputPanel);

        outputPanel.setLayout(new java.awt.BorderLayout());

        outputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Output:"));
        outputTextArea.setColumns(20);
        outputTextArea.setEditable(false);
        outputTextArea.setFont(new java.awt.Font("Courier", 0, 13));
        outputTextArea.setLineWrap(true);
        outputTextArea.setRows(5);
        outputTextArea.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane2.setViewportView(outputTextArea);

        outputPanel.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.Y_AXIS));

        jPanel11.setPreferredSize(new java.awt.Dimension(100, 10));
        lnColTextField.setEditable(false);
        lnColTextField.setText("1:0");
        lnColTextField.setToolTipText("Displays the cursor position");
        lnColTextField.setMaximumSize(new java.awt.Dimension(78, 22));
        lnColTextField.setMinimumSize(new java.awt.Dimension(78, 22));
        lnColTextField.setPreferredSize(new java.awt.Dimension(78, 22));
        jPanel11.add(lnColTextField);

        outputPanel.add(jPanel11, java.awt.BorderLayout.EAST);

        jSplitPane1.setBottomComponent(outputPanel);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        fileMenu.setText("File");
        newMenuItem.setText("New File...");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(newMenuItem);

        openMenuItem.setText("Open File...");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(openMenuItem);

        saveMenuItem.setText("Save File");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setText("Save As...");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(saveAsMenuItem);

        closeMenuItem.setText("Close File");
        closeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(closeMenuItem);

        fileMenu.add(jSeparator1);

        printMenu.setText("Print...");
        printInputMenuItem.setText("Text Input");
        printInputMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printInputMenuItemActionPerformed(evt);
            }
        });

        printMenu.add(printInputMenuItem);

        printOutputMenuItem.setText("Text Output");
        printOutputMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printOutputMenuItemActionPerformed(evt);
            }
        });

        printMenu.add(printOutputMenuItem);

        printCCMenuItem.setText("Concells visualization");
        printCCMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printCCMenuItemActionPerformed(evt);
            }
        });

        printMenu.add(printCCMenuItem);

        printTreesMenuItem.setText("Function calls visualization");
        printTreesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printTreesMenuItemActionPerformed(evt);
            }
        });

        printMenu.add(printTreesMenuItem);

        printVarsMenuItem.setText("Environment");
        printVarsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printVarsMenuItemActionPerformed(evt);
            }
        });

        printMenu.add(printVarsMenuItem);

        printRecuMenuItem.setText("Function calls text");
        printRecuMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printRecuMenuItemActionPerformed(evt);
            }
        });

        printMenu.add(printRecuMenuItem);

        fileMenu.add(printMenu);

        fileMenu.add(jSeparator4);

        quitMenuItem.setText("Quit PVTS");
        quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(quitMenuItem);

        jMenuBar1.add(fileMenu);

        editMenu.setText("Edit");
        cutMenuItem.setText("Cut");
        cutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cutMenuItemActionPerformed(evt);
            }
        });

        editMenu.add(cutMenuItem);

        copyMenuItem.setText("Copy");
        copyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyMenuItemActionPerformed(evt);
            }
        });

        editMenu.add(copyMenuItem);

        pasteMenuItem.setText("Paste");
        pasteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteMenuItemActionPerformed(evt);
            }
        });

        editMenu.add(pasteMenuItem);

        jMenuBar1.add(editMenu);

        viewMenu.setText("View");
        ccBoxMenuItem.setText("Conscells");
        ccBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ccBoxMenuItemActionPerformed(evt);
            }
        });

        viewMenu.add(ccBoxMenuItem);

        treesBoxMenuItem.setText("Function calls");
        treesBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                treesBoxMenuItemActionPerformed(evt);
            }
        });

        viewMenu.add(treesBoxMenuItem);

        envBoxMenuItem.setText("Environment");
        envBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                envBoxMenuItemActionPerformed(evt);
            }
        });

        viewMenu.add(envBoxMenuItem);

        viewMenu.add(jSeparator2);

        mosaicMenuItem.setText("Tile windows");
        mosaicMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mosaicMenuItemActionPerformed(evt);
            }
        });

        viewMenu.add(mosaicMenuItem);

        viewMenu.add(jSeparator3);

        optMenuItem.setText("Preferences...");
        optMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optMenuItemActionPerformed(evt);
            }
        });

        viewMenu.add(optMenuItem);

        jMenuBar1.add(viewMenu);

        examplesMenu.setText("Examples");
        conscellsMenuItem.setText("Lists");
        conscellsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conscellsMenuItemActionPerformed(evt);
            }
        });

        examplesMenu.add(conscellsMenuItem);

        appendMenuItem.setText("Append");
        appendMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                appendMenuItemActionPerformed(evt);
            }
        });

        examplesMenu.add(appendMenuItem);

        factorialMenuItem.setText("Factorial");
        factorialMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                factorialMenuItemActionPerformed(evt);
            }
        });

        examplesMenu.add(factorialMenuItem);

        fibonacciMenuItem.setText("Fibonacci");
        fibonacciMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fibonacciMenuItemActionPerformed(evt);
            }
        });

        examplesMenu.add(fibonacciMenuItem);

        baMenuItem.setText("Bank Account");
        baMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                baMenuItemActionPerformed(evt);
            }
        });

        examplesMenu.add(baMenuItem);

        sortingMenuItem.setText("Sorting Algortithms");
        sortingMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortingMenuItemActionPerformed(evt);
            }
        });

        examplesMenu.add(sortingMenuItem);

        setStructureMenuItem.setText("Sets and List Structures");
        setStructureMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setStructureMenuItemActionPerformed(evt);
            }
        });

        examplesMenu.add(setStructureMenuItem);

        jMenuBar1.add(examplesMenu);

        helpMenu.setText("Help");
        aboutMenuItem.setText("About...");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });

        helpMenu.add(aboutMenuItem);

        langMenuItem.setText("PVTS Language");
        langMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                langMenuItemActionPerformed(evt);
            }
        });

        helpMenu.add(langMenuItem);

        updateMenuItem.setText("Download latest version");
        updateMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateMenuItemActionPerformed(evt);
            }
        });

        helpMenu.add(updateMenuItem);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-694)/2, (screenSize.height-573)/2, 694, 573);
    }// </editor-fold>//GEN-END:initComponents

    private void updateMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateMenuItemActionPerformed
// TODO add your handling code here:
        showInBrowser("http://www.davidpilo.com/pvts/download.html",null);
    }//GEN-LAST:event_updateMenuItemActionPerformed

    
    
    static boolean showInBrowser(String url, Frame frame)
  {
    //minimizes the app
    if (frame != null)
        frame.setExtendedState(JFrame.ICONIFIED);
    
    String os = System.getProperty("os.name").toLowerCase();
    Runtime rt = Runtime.getRuntime();
    try
    {
            if (os.indexOf( "win" ) >= 0)
            {
              String[] cmd = new String[4];
              cmd[0] = "cmd.exe";
              cmd[1] = "/C";
              cmd[2] = "start";
              cmd[3] = url;
              rt.exec(cmd);
            }
            else if (os.indexOf( "mac" ) >= 0)
            {
                rt.exec( "open " + url);
            }
            else
            {
              //prioritized 'guess' of users' preference
              String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                  "netscape","opera","links","lynx"};
 
              StringBuffer cmd = new StringBuffer();
              for (int i=0; i<browsers.length; i++)
                cmd.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + url + "\" ");
 
              rt.exec(new String[] { "sh", "-c", cmd.toString() });
              //rt.exec("firefox http://www.google.com");
              //System.out.println(cmd.toString());
              
           }
    }
    catch (IOException e)
    {
        e.printStackTrace();
        JOptionPane.showMessageDialog(frame,
                                            "\n\n The system failed to invoke your default web browser while attempting to access: \n\n " + url + "\n\n",
                                            "Browser Error",
                                            JOptionPane.WARNING_MESSAGE);
 
        return false;
    }
    return true;
}
    private void langMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_langMenuItemActionPerformed
// TODO add your handling code here:
    showInBrowser("http://www.davidpilo.com/pvts/PVTSLanguage.htm",null);
    }//GEN-LAST:event_langMenuItemActionPerformed

    private void setStructureMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setStructureMenuItemActionPerformed
// TODO add your handling code here:
        String s = "; By David A. Pilo Mansion\n" +
                "; davidpilo@gmail.com\n; 2007\n" +
                ";\n" +
                "; NOTE: for this example use both the Cons-cells Viewer\n" +
                ";       and the Function Calls Viewer.\n" +
                "\"SETS AND LIST STRUCTURES EXAMPLE\"\n" +
                "(define (isIn a lis)\n" +
                "\t(cond\n" +
                "\t\t((null? lis) #f)\n" +
                "\t\t((equal? (car lis) a) #t)\n" +
                "\t\t(else (isIn a (cdr lis)))))\n" +
                "\n" +
                "(define (convertListToSet lis)\n" +
                "\t(cond\n" +
                "\t\t((null? lis) '())\n" +
                "\t\t((not (isIn (car lis) (cdr lis))) (cons (car lis) (convertListToSet (cdr lis))))\n" +
                "\t\t(else (convertListToSet (cdr lis)))))\n" +
                "\n" +
                "(define (unionOfTwoSets set1 set2)\n" +
                "\t(cond\n" +
                "\t\t((null? set1) set2)\n" +
                "\t\t((not (isIn (car set1) set2)) (cons (car set1)(unionOfTwoSets (cdr set1) set2)))\n" +
                "\t\t(else (unionOfTwoSets (cdr set1) set2))))\n" +
                "\n" +
                "(define list1 (list 1 1 2 4 5))\n" +
                "(define set1 (convertListToSet list1))\n" +
                "(define list2 (list (list 3 4) 2 1 1 (list (list 4) 4)))\n" +
                "(define set2(convertListToSet list2))\n" +
                "(define list3 (list 3 4 (list 1 2) 8 (list 1 2)))\n" +
                "(define set3 (convertListToSet list3))\n" +
                "\n" +
                "\n" +
                "(define (teststruct lis1 lis2)\n" +
                "\t(cond\n" +
                "\t\t((and (null? lis1) (null? lis2)) #t)\n" +
                "\t\t((or (and (null? lis1) (not(null? lis2))) (and (null? lis2) (not(null? lis1)))) #f)\n" +
                "\t\t((or (and (list? lis1)(not(list? lis2))) (and (list? lis2) (not(list? lis1)))) #f)\n" +
                "\t\t((and (list? lis1)(list? lis2)) (and (teststruct (car lis1) (car lis2)) (teststruct (cdr lis1) (cdr lis2))))((and (not(list? lis1)) (not(list? lis1))) #t)\n" +
                "\t\t(else #f)))\n" +
                "\n" +
                "(teststruct '(3 5 3) 9)\n" +
                "(teststruct '(1 2 3) '(3 2 1))\n" +
                "(teststruct '(1 (2 (2 3))) '((23) 2))\n" +
                "(teststruct '(1 (2 2) (3 (4 3))) '(4 (55 5) (3 (5 4))))\n" +
                "(teststruct '(1 (2 2) (3 (4 3))) '(4 (55 5) (3 (5 3 4))))";
                inputTextArea.setText(s);
    }//GEN-LAST:event_setStructureMenuItemActionPerformed

    private void sortingMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortingMenuItemActionPerformed
// TODO add your handling code here:
        String s = "; By David A. Pilo Mansion\n" +
                "; davidpilo@gmail.com\n; 2007\n" +
                ";\n" +
                "; NOTE: for this example use both the Cons-cells Viewer\n" +
                ";       and the Function Calls Viewer.\n" +
                "\"SORTING ALGORITHMS  EXAMPLE:\"\n" +
                ";insertion sort:\n" +
                "(define (insertionSort lis)\n" +
                "\t(cond\n" +
                "\t\t((null? lis) lis)\n" +
                "\t\t(else (insertElmt (car lis) (insertionSort (cdr lis))))))\n" +
                "(define (insertElmt ele lis)\n" +
                "\t(cond\n" +
                "\t\t((null? lis) (list ele))\n" +
                "\t\t((< ele (car lis)) (cons ele lis))\n" +
                "\t\t(else (cons (car lis) (insertElmt ele (cdr lis))))))\n" +
                "(insertionSort '(1 2 3 4))\n" +
                "(insertionSort '(4 3 2 1))\n" +
                ";qsort test:\n" +
                "(define (qsort lis order)\n" +
                "\t(define (pivotlist lis pivot order switch)\n" +
                "\t\t(cond\n" +
                "\t\t\t((null? lis) (list))\n" +
                "\t\t\t((= 1 switch) (if (order pivot (car lis)) (cons (car lis) (pivotlist (cdr lis) pivot order 1)) (pivotlist (cdr lis) pivot order 1)))\n" +
                "\t\t\t(else (if (not(order pivot (car lis))) (cons (car lis) (pivotlist (cdr lis) pivot order 0))(pivotlist (cdr lis) pivot order 0)))))\n" +
                "\t(cond\n" +
                "\t\t((null? lis) (list))\n" +
                "\t\t(else (append(append (qsort (pivotlist (cdr lis) (car lis) order 0) order) (list(car lis))) (qsort (pivotlist (cdr lis) (car lis) order 1) order)))))\n" +
                "(qsort '(5 23 3 42 2 3) >)";


        inputTextArea.setText(s);
             
    }//GEN-LAST:event_sortingMenuItemActionPerformed

    private void closeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeMenuItemActionPerformed
// TODO add your handling code here:
        //if (this.getTitle().substring(this.getTitle().length() - 34,this.getTitle().length() - 35).equals("*"))
        //    new JDialog(this,"would you like to save before closing?");
        PVTS.resetPVTS("");
        this.setTitle("(untitled) - Pilo's Visual Tools for Scheme");
        inputTextArea.setText("");
        //inputTextArea.setEnabled(false);
        outputTextArea.setText("");
        repaintAllVisuals();
        FileReaderWriter.reader = null;
        FileReaderWriter.currentFilePath = null;
    }//GEN-LAST:event_closeMenuItemActionPerformed

    private void conscellsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conscellsMenuItemActionPerformed
// TODO add your handling code here:
         String listExample = "; By David A. Pilo Mansion\n" +
                "; davidpilo@gmail.com\n" +
                "; 2007\n" +
                ";\n" +
                "; NOTE: for this example use the Cons-cells Viewer.\n" +
                "\n" +
                "\"LISTS  EXAMPLE:\"\n" +
                 "; There is different ways to create lists:\n" +
                 "'(1 2 3)\n" +
                 "(list 1 2 3)\n" +
                 "(cons 1 (cons 2 (cons 3 ())))\n" +
                 "; lists are heterogeneous:\n" +
                 "'(1 1.4 #f \"hello\" #\\j (1 2 3))\n" +
                 "; car accesses the first element of a list\n" +
                 "(car '(1 2 3))\n" +
                 "; while cdr accesses the rest of the list\n" +
                 "(cdr '(1 2 3))\n" +
                 "; There is also what we call dotted pairs:\n" +
                 "(define a (cons 1 2))\n" +
                 "(define b (cons 1 (cons 2 ())))\n" +
                 "; what is the difference between a and b?\n" +
                 "a b\n" +
                 ";\n" +
                 "; Here is an example of complex list:\n" +
                 "'((1) (((2))) (((3 4 5) 6 ((7(8)) 9) 10 (11 12 13) (14 15) ((16))) 17 ((18 (19))) 20 21) 22 (23))\n";
                
                 inputTextArea.setText(listExample);
    }//GEN-LAST:event_conscellsMenuItemActionPerformed

    private void baMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_baMenuItemActionPerformed
// TODO add your handling code here:
        String baExample = "; By David A. Pilo Mansion\n" +
                "; davidpilo@gmail.com\n" +
                "; 2007\n" +
                ";\n" +
                "\"BANK ACCOUNT EXAMPLE:\"\n" +
                "; The bank account problem because it evidences\n" +
                "; the fact that, in scheme, procedures have their own\n" +
                "; environment.\n"+
                "(define (make-account balance)\n" +
                "\t(define (deposit amount)\n" +
                "\t\t(cond\n" +
                "\t\t\t((>= amount 0) (set! balance (+ balance amount)) balance)\n" +
                "\t\t\t(else \"Can't deposit a negative amount.\")))\n" +
                "\t(define (withdraw amount)\n" +
                "\t\t(set! balance (- balance amount)) balance)\n" +
                "\t(define (dispatch s)\n" +
                "\t\t(cond\n" +
                "\t\t\t((eq? s 'withdraw) withdraw)\n" +
                "\t\t\t(else deposit)))\n" +
                "\tdispatch)\n" +
                "(define acc1 (make-account 500))\n" +
                "; what is acc1?\n" +
                "acc1\n" +
                "; what is (acc1 'withdraw)\n" +
                "(acc1 'withdraw)\n" +
                "; lets withdraw $70 from the account and then make a $30 deposit.\n"+
                "((acc1 'withdraw) 70)\n" +
                "((acc1 'deposit) 30)\n" +
                "; why is there $460 left in the account?\n" +
                "; acc1 has its own enviroment where balance is defined.";
        
        inputTextArea.setText(baExample);
    }//GEN-LAST:event_baMenuItemActionPerformed

    private void fibonacciMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fibonacciMenuItemActionPerformed
// TODO add your handling code here:
        String fiboExample = "; By David A. Pilo Mansion\n" +
                "; davidpilo@gmail.com\n" +
                "; 2007\n" +
                ";\n" +
                "; NOTE: for this example use the Function Calls Viewer.\n" +
                ";\n" +
                "\"FIBONACCI EXAMPLE:\"\n" +
                "; The Fibonacci function is a widely use in\n" +
                "; functional programming languages to introduce\n" +
                "; doubly recursive function.\n"+
                "; We usually define it as fib(n) = fib(n-1) + fib(n-2)\n" +
                ";	                        fib(1) = 1\n" +
                ";	                        fib(0) = 0\n" +
                "; Here is an example of how it works:\n" +
                "; fib(3) = fib(2) + fib(1)\n" +
                "; fib(3) = fib(1) + fib(0) + fib(1)\n" +
                "; fib(3) = 1 + 0 + 1\n" +
                "; fib(3) = 2\n" +
                "; Lets define Fibonacci:\n" +
                "(define (fib n)\n" +
                "\t(cond\n" +
                "\t\t((= n 1) 1)\n" +
                "\t\t((= n 0) 0)\n" +
                "\t\t(else (+ (fib (- n 1)) (fib (- n 2))))))\n" +
                "; (fib 3) evaluates to\n" +
                "(fib 3)\n" +
                "; (fib 4) evaluates to\n" +
                "(fib 4)\n" +
                "; (fib 6) evaluates to\n" +
                "(fib 6)\n";
        
                inputTextArea.setText(fiboExample);
    }//GEN-LAST:event_fibonacciMenuItemActionPerformed

    private void factorialMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_factorialMenuItemActionPerformed
// TODO add your handling code here:
        String factorialExample = "; By David A. Pilo Mansion\n" +
                "; davidpilo@gmail.com\n" +
                "; 2007\n" +
                ";\n" +
                "; NOTE: for this example use the Function Calls Viewer.\n" +
                ";\n" +
                "\"FACTORIAL EXAMPLE:\"\n" +
                "; The factorial function is a widely use in\n" +
                "; functional programming languages to introduce\n" +
                "; recursive function calls. The mathematical notation\n" +
                "; for factorial is f(n) = n!\n" +
                "; We usually define it as f(n) = n x f(n-1)\n" +
                ";	                        f(1) = 1\n" +
                "; Here is an example of how it works:\n" +
                "; f(3) = 3 x f(2)\n" +
                "; f(3) = 3 x 2 x f(1)\n" +
                "; f(3) = 3 x 2 x 1\n" +
                "; f(3) = 3 x 2\n" +
                "; f(3) = 6\n" +
                "; Lets define factorial:\n" +
                "(define (factorial n)\n" +
                "\t(if (= n 1)\n" +
                "\t\t1\n" +
                "\t\t(* n (factorial (- n 1)))))\n" +
                "; (factorial 3) evaluates to\n" +
                "(factorial 3)\n" +
                "; (factorial 4) evaluates to\n" +
                "(factorial 4)\n" +
                "; (factorial 12) evaluates to\n" +
                "(factorial 12)\n";
        
                inputTextArea.setText(factorialExample);
    }//GEN-LAST:event_factorialMenuItemActionPerformed

    private void appendMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appendMenuItemActionPerformed
// TODO add your handling code here:
        String appendExample = "; By David A. Pilo Mansion\n" +
                "; davidpilo@gmail.com\n" +
                "; 2007\n" +
                ";\n" +
                "; NOTE: for this example use the Cons-cells Viewer.\n" +
                ";\n" +
                "\"APPEND EXAMPLE:\" \n" +
                "; This example is a very interesting one since first time\n" +
                "; Scheme users don’t know how the append function works. In\n" +
                "; plain English, append takes any number of lists and appends\n" +
                "; them together. That is:\n" +
                "; (append '(1 2 3) '(4 5 6)) evaluates to\n" +
                "(append '(1 2 3) '(4 5 6))\n" +
                "; There are several ways that append could work with the concells in the lists passed to it.\n" +
                "; It could make copies of one or more of the lists or" +
                "; it could just connect the existing cells." +
                "; Let's see how it actually works.\n" +
                "(define a '(1 2 3))\n" +
                "(define b '(4 5 6))\n" +
                "; lets append both lists and call it c\n" +
                "(define c (append a b))\n" +
                "; c evals to\n" +
                "c\n" +
                "; Now lets modify the list a\n" +
                "(set-car! a 0)\n" +
                "; so a evals to\n" +
                "a\n" +
                "; and b evals to\n" +
                "b\n" +
                "; and c evals to\n" +
                "c\n" +
                "; c evaluates to (1 2 3 4 5 6) WHY?\n" +
                "; append copied the list a and appended that copy\n" +
                "; to the list b. So if we modify the list b\n" +
                "(set-car! b 0)\n" +
                "; b evals to\n" +
                "b\n" +
                "; c evals to\n" +
                "c\n" +
                "; --------------\n" +
                "; Let's implement our own version of append\n" +
                "; where this time append will just connect the existing cells\n" +
                "(define (last l)\n" +
                "\t(if (null? (cdr l))\n" +
                "\t\tl\n" +
                "\t\t(last (cdr l))))\n" +
                "(define (append! l1 l2)\n" +
                "\t(set-cdr! (last l1) l2)l1)\n" +
                "(define list1 '(20 21 22))\n" +
                "(define list2 '(23 24 25))\n" +
                "(define list3 (append! list1 list2))\n" +
                "\"END OF APPEND EXAMPLE\"";

        inputTextArea.setText(appendExample);
    }//GEN-LAST:event_appendMenuItemActionPerformed

    private void printOutputMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printOutputMenuItemActionPerformed
// TODO add your handling code here:
        PrintUtilities.printComponent(outputTextArea);
    }//GEN-LAST:event_printOutputMenuItemActionPerformed

    private void printVarsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printVarsMenuItemActionPerformed
// TODO add your handling code here:
        PrintUtilities.printComponent(drawingVPanel);
    }//GEN-LAST:event_printVarsMenuItemActionPerformed

    private void printTreesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printTreesMenuItemActionPerformed
// TODO add your handling code here:
        PrintUtilities.printComponent(drawingTPanel);
    }//GEN-LAST:event_printTreesMenuItemActionPerformed

    private void printCCMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printCCMenuItemActionPerformed
// TODO add your handling code here:
        PrintUtilities.printComponent(drawingCCPanel);
    }//GEN-LAST:event_printCCMenuItemActionPerformed

    private void printInputMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printInputMenuItemActionPerformed
// TODO add your handling code here:
        PrintUtilities.printComponent(inputTextArea);
    }//GEN-LAST:event_printInputMenuItemActionPerformed

    private void printRecuMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printRecuMenuItemActionPerformed
// TODO add your handling code here:
        PrintUtilities.printComponent(recuTextArea);
    }//GEN-LAST:event_printRecuMenuItemActionPerformed

    private void tabSizeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tabSizeComboBoxActionPerformed
// TODO add your handling code here:
        inputTextArea.setTabSize(tabSizeComboBox.getSelectedIndex()+1);
    }//GEN-LAST:event_tabSizeComboBoxActionPerformed

    private void pasteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteMenuItemActionPerformed
// TODO add your handling code here:
        this.inputTextArea.paste();
    }//GEN-LAST:event_pasteMenuItemActionPerformed

    private void copyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyMenuItemActionPerformed
// TODO add your handling code here:
        this.inputTextArea.copy();
    }//GEN-LAST:event_copyMenuItemActionPerformed

    private void cutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cutMenuItemActionPerformed
// TODO add your handling code here:
        this.inputTextArea.cut();
    }//GEN-LAST:event_cutMenuItemActionPerformed

    private void recuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recuButtonActionPerformed
// TODO add your handling code here:
        recuFrame.setVisible(true);
    }//GEN-LAST:event_recuButtonActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
// TODO add your handling code here:
        int returnVal = fc.showSaveDialog(PVTSFrame.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            FileReaderWriter.saveAs(fc.getSelectedFile().getAbsolutePath(),inputTextArea);
            this.setTitle(fc.getSelectedFile().getAbsolutePath()+" - Pilo's Visual Tools for Scheme");
            }
        
    
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
// TODO add your handling code here:
        //inputTextArea.setEnabled(true);
        int returnVal = fc.showOpenDialog(PVTSFrame.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
                fc.setMultiSelectionEnabled(false);
                FileReaderWriter.readFile(fc.getSelectedFile().getAbsolutePath(),inputTextArea);
            this.setTitle(fc.getSelectedFile().getAbsolutePath()+" - Pilo's Visual Tools for Scheme");
        }
        
     //   this.openfileChooserFrame.setTitle("Open File...");
     //   this.openfileChooserFrame.setVisible(true);
     //   this.openfileChooserFrame.setSize(600,400);
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
// TODO add your handling code here:
        PVTS.resetPVTS("");
        this.setTitle("(untitled) - Pilo's Visual Tools for Scheme");
        inputTextArea.setText("");
        //inputTextArea.setEnabled(true);
        outputTextArea.setText("");
        repaintAllVisuals();
        FileReaderWriter.reader = null;
        FileReaderWriter.currentFilePath = null;
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void lineWrapCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lineWrapCheckBoxActionPerformed
        if (lineWrapCheckBox.isSelected()) {
            inputTextArea.setLineWrap(true);
            outputTextArea.setLineWrap(true);
        } else {
            inputTextArea.setLineWrap(false);
            outputTextArea.setLineWrap(false);
        }
    }//GEN-LAST:event_lineWrapCheckBoxActionPerformed

    private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_quitMenuItemActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        if (FileReaderWriter.currentFilePath == null) {
            int returnVal = fc.showSaveDialog(PVTSFrame.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            FileReaderWriter.saveAs(fc.getSelectedFile().getAbsolutePath(),inputTextArea);
            this.setTitle(fc.getSelectedFile().getAbsolutePath()+" - Pilo's Visual Tools for Scheme");
            }
        }
        else {
            FileReaderWriter.save(inputTextArea);
            this.setTitle(fc.getSelectedFile().getAbsolutePath()+" - Pilo's Visual Tools for Scheme");
        }
    }//GEN-LAST:event_saveMenuItemActionPerformed
        
    private void fBColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fBColorPanelMouseReleased
        fBColorFrame.setVisible(true);
        fBColorFrame.setSize(new Dimension(400,400));
    }//GEN-LAST:event_fBColorPanelMouseReleased
    
    private void ccBColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ccBColorPanelMouseReleased
        ccBColorFrame.setVisible(true);
        ccBColorFrame.setSize(new Dimension(400,400));
    }//GEN-LAST:event_ccBColorPanelMouseReleased
    
    private void fBColorColorChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fBColorColorChooserPropertyChange
        Color c = fBColorColorChooser.getColor();
        DrawingTreesPanel.bcolor = c;
        this.fBColorPanel.setBackground(c);
        ftrees.repaint();
    }//GEN-LAST:event_fBColorColorChooserPropertyChange
    
    private void ccBColorColorChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_ccBColorColorChooserPropertyChange
        Color c = ccBColorColorChooser.getColor();
        DrawingConscellsPanel.bcolor = c;
        this.ccBColorPanel.setBackground(c);
        fcc.repaint();
    }//GEN-LAST:event_ccBColorColorChooserPropertyChange
    
    private void fColorColorChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fColorColorChooserPropertyChange
        Color c = fColorColorChooser.getColor();
        DrawingTreesPanel.color = c;
        this.fColorPanel.setBackground(c);
        ftrees.repaint();
    }//GEN-LAST:event_fColorColorChooserPropertyChange
    
    private void fColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fColorPanelMouseReleased
        fColorFrame.setVisible(true);
        fColorFrame.setSize(new Dimension(400,400));
    }//GEN-LAST:event_fColorPanelMouseReleased
    
    private void ccColorColorChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_ccColorColorChooserPropertyChange
        Color c = ccColorColorChooser.getColor();
        DrawingConscellsPanel.color = c;
        this.ccColorPanel.setBackground(c);
        fcc.repaint();
    }//GEN-LAST:event_ccColorColorChooserPropertyChange
    
    private void ccColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ccColorPanelMouseReleased
        ccColorFrame.setVisible(true);
        ccColorFrame.setSize(new Dimension(400,400));
    }//GEN-LAST:event_ccColorPanelMouseReleased
    
    private void fontSizeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontSizeComboBoxActionPerformed
        String size = (String) fontSizeComboBox.getSelectedItem();
        Integer i = new Integer(size);
        Font f = new Font("Courier", Font.PLAIN, i);
        //f.getSize();
        //f
        inputTextArea.setFont(f);
        outputTextArea.setFont(f);
        recuTextArea.setFont(f);
    }//GEN-LAST:event_fontSizeComboBoxActionPerformed

    private void result_bubbleCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_result_bubbleCheckBoxActionPerformed
        FunctionManager.result_bubble = result_bubbleCheckBox.isSelected();
        if (ftrees.isVisible())
            ftrees.repaint();
    }//GEN-LAST:event_result_bubbleCheckBoxActionPerformed
    
    private void alignmentComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alignmentComboBoxActionPerformed
        DrawingTreesPanel.currentlySelected = null;
        PVTSFrame.functag.setText("");
        FunctionManager.viewMode = alignmentComboBox.getSelectedIndex();
        if (ftrees.isVisible())
            ftrees.repaint();
    }//GEN-LAST:event_alignmentComboBoxActionPerformed
    
    private void readButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readButtonActionPerformed
        String st = readTextField.getText();
        String newBuffer = TextIO.buffer.substring(0,TextIO.getPos()) + st + TextIO.buffer.substring(TextIO.getPos(),TextIO.buffer.length()-1);
        TextIO.fillBuffer(newBuffer, TextIO.getPos());
        //Thread.currentThread().yield();
        
    }//GEN-LAST:event_readButtonActionPerformed
    
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        this.aboutFrame.setVisible(true);
        this.aboutFrame.setSize(new Dimension(640,480));
        this.aboutFrame.validate();
    }//GEN-LAST:event_aboutMenuItemActionPerformed
    
    private void inputTextAreaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inputTextAreaMouseReleased
        int col=0;
        int ln=1;
        int pos = inputTextArea.getCaretPosition();
        for (int i = 0; i < pos ; i++){
            char s = inputTextArea.getText().charAt(i);
            if (s == '\n') {
                ln ++;
                col = 0;
            } else
                col++;
        }
        setLnCol(ln,col);
        removeHighlights(inputTextArea);
        setParenthesisStack();
        int currentpos = inputTextArea.getCaretPosition();
        int temp = inputTextArea.getText().length();
        if ((temp != 0) && (currentpos != 0)) {
            char s = inputTextArea.getText().charAt(currentpos-1);
            if (!PVTS.parenthesisLocStack.isEmpty() && s == ')') {
                highlightParenthesis(inputTextArea,PVTS.parenthesisLocStack.pop(),currentpos);
            }
        }
        //myHighlightPainter.paint()
        inputTextArea.repaint();

    }//GEN-LAST:event_inputTextAreaMouseReleased
    
    
    private void setLnCol(int ln, int col) {
        this.lnColTextField.setText(ln+":"+col);
    }
    
    private void inputTextAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputTextAreaKeyReleased
        if (FileReaderWriter.currentFilePath == null)
            this.setTitle("(untitled)* - Pilo's Visual Tools for Scheme");
        else
            this.setTitle(FileReaderWriter.currentFilePath+"* - Pilo's Visual Tools for Scheme");
        int col=0;
        int ln=1;
        int pos = inputTextArea.getCaretPosition();
        for (int i = 0; i < pos ; i++){
            char s = inputTextArea.getText().charAt(i);
            if (s == '\n') {
                ln ++;
                col = 0;
            } else
                col++;
        }
        //Font prev = inputTextArea.getFont();
        //Font commentFont = new Font()
        // code to color the comments:
        //
        setLnCol(ln,col);
        removeHighlights(inputTextArea);
        setParenthesisStack();
        int currentpos = inputTextArea.getCaretPosition();
        int temp = inputTextArea.getText().length();
        if ((temp != 0) && (currentpos != 0)) {
            char s = inputTextArea.getText().charAt(currentpos-1);
            if (!PVTS.parenthesisLocStack.isEmpty() && s == ')') {
                highlightParenthesis(inputTextArea,PVTS.parenthesisLocStack.pop(),currentpos);
            }
        }
        
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            String s1 = inputTextArea.getText().substring(0,currentpos-1);
            String s2 = inputTextArea.getText().substring(currentpos-1,temp);
            int num_of_tabs = PVTS.parenthesisLocStack.size();
            String tabs = "";
            if (temp != 0) {
                for (int i = 0; i < num_of_tabs; i++) {
                    tabs = tabs + "\t";
                }
            inputTextArea.setText(s1 +"\n"+ tabs + s2.substring(1,s2.length()));
            inputTextArea.setCaretPosition(currentpos + num_of_tabs);
            }
        }
      
        
        
        inputTextArea.repaint();

        
    }//GEN-LAST:event_inputTextAreaKeyReleased
    
    private void mosaicMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mosaicMenuItemActionPerformed
        Dimension screenSize =
                Toolkit.getDefaultToolkit().getScreenSize();
        fcc.setVisible(true);
        this.ccBoxMenuItem.setSelected(true);
        this.ccToggleButton.setSelected(true);
        fenv.setVisible(true);
        this.envBoxMenuItem.setSelected(true);
        this.envToggleButton.setSelected(true);
        ftrees.setVisible(true);
        this.treesBoxMenuItem.setSelected(true);
        this.treesToggleButton.setSelected(true);
        fcc.setLocation(0,0);
        fenv.setLocation(screenSize.width/2,0);
        ftrees.setLocation(0,screenSize.height/2);
        this.setLocation(screenSize.width/2,screenSize.height/2);
        fcc.setSize(new Dimension(screenSize.width/2,screenSize.height/2));
        fenv.setSize(new Dimension(screenSize.width/2,screenSize.height/2));
        ftrees.setSize(new Dimension(screenSize.width/2,screenSize.height/2));
        this.setSize(new Dimension(screenSize.width/2,screenSize.height/2));
        ftrees.validate();
        fenv.validate();
        fcc.validate();
    }//GEN-LAST:event_mosaicMenuItemActionPerformed
    
    
    public void stopStepper() {
        inputTextArea.setEditable(true);
            stepper_mode = false;
            controlsPanel.remove(doStepButton);
            stepperButton.setText("Start Stepper");
            stepperButton.setSelected(false);
            controlsPanel.add(runButton);
            textIn = inputTextArea.getText();
            mainButtonsPanel.revalidate();
            mainButtonsPanel.repaint();
    }
    private void stepperButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stepperButtonActionPerformed
        if (stepper_mode) {
            //inputTextArea.setEnabled(true);
            inputTextArea.setEditable(true);
            stepper_mode = false;
            controlsPanel.remove(doStepButton);
            stepperButton.setText("Start Stepper");
            stepperButton.setSelected(false);
            controlsPanel.add(runButton);
            textIn = inputTextArea.getText();
            //PVTS.resetPVTS(textIn);
            //outputTextArea.setText("");
        } else {
            //inputTextArea.setEnabled(false);
            inputTextArea.setEditable(false);
            stepper_mode = true;
            controlsPanel.add(doStepButton);
            stepperButton.setText("Stop Stepper");
            stepperButton.setSelected(true);
            controlsPanel.remove(runButton);
            textIn = inputTextArea.getText();
            PVTS.resetPVTS(textIn);
            outputTextArea.setText("");
            textIn = inputTextArea.getText();
            PVTS.globalBuffer = textIn;
            TextIO.fillBuffer(textIn,0);
            PVTSFrame.put("system: Stepper Activated.\n");
            fenv.repaint();
            ftrees.repaint();
            fcc.repaint();
            
            
        }
        mainButtonsPanel.revalidate();
        mainButtonsPanel.repaint();
    }//GEN-LAST:event_stepperButtonActionPerformed
    
    private void primCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_primCheckBoxActionPerformed
        if (primCheckBox.isSelected()) {
            DrawingVarsPanel.primTableShow = true;
        } else
            DrawingVarsPanel.primTableShow = false;
        fenv.repaint();
    }//GEN-LAST:event_primCheckBoxActionPerformed
    
    private void procCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_procCheckBoxActionPerformed
        if (procCheckBox.isSelected()) {
            DrawingVarsPanel.procTableShow = true;
        } else
            DrawingVarsPanel.procTableShow = false;
        fenv.repaint();
    }//GEN-LAST:event_procCheckBoxActionPerformed
    
    private void varCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_varCheckBoxActionPerformed
        if (varCheckBox.isSelected()) {
            DrawingVarsPanel.varTableShow = true;
        } else
            DrawingVarsPanel.varTableShow = false;
        fenv.repaint();
    }//GEN-LAST:event_varCheckBoxActionPerformed
    
    private void listCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listCheckBoxActionPerformed
        if (listCheckBox.isSelected()) {
            DrawingVarsPanel.listTableShow = true;
        } else
            DrawingVarsPanel.listTableShow = false;
        fenv.repaint();
    }//GEN-LAST:event_listCheckBoxActionPerformed
    
    private void envToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_envToggleButtonActionPerformed
        if (envToggleButton.isSelected()) {
            envBoxMenuItem.setSelected(true);
            fenv.setVisible(true);
            fenv.repaint();
        } else {
            envBoxMenuItem.setSelected(false);
            fenv.setVisible(false);
        }
    }//GEN-LAST:event_envToggleButtonActionPerformed
    
    private void treesToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_treesToggleButtonActionPerformed
        if (treesToggleButton.isSelected()) {
            treesBoxMenuItem.setSelected(true);
            ftrees.setVisible(true);
            ftrees.repaint();
        } else {
            treesBoxMenuItem.setSelected(false);
            ftrees.setVisible(false);
        }
    }//GEN-LAST:event_treesToggleButtonActionPerformed
    
    private void ccToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ccToggleButtonActionPerformed
        if (ccToggleButton.isSelected()) {
            ccBoxMenuItem.setSelected(true);
            fcc.setVisible(true);
            fcc.repaint();
        } else {
            ccBoxMenuItem.setSelected(false);
            fcc.setVisible(false);
        }
    }//GEN-LAST:event_ccToggleButtonActionPerformed
    
    private void zoomSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_zoomSliderStateChanged
// TODO add your handling code here:
        // DrawingTreesPanel.currentlySelected = null;
        double funcZoom = zoomSlider.getValue()+1;
        FunctionManager.zoom = 1.0 * (funcZoom / 5.0);
        //DrawingConscellsPanel.zoom = 16 * (((int)mainZoom) / 5);
        //jTabbedPane1.repaint();
        ftrees.repaint();
    }//GEN-LAST:event_zoomSliderStateChanged
    
    private void envBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_envBoxMenuItemActionPerformed
        if (envBoxMenuItem.isSelected()) {
            envToggleButton.setSelected(true);
            fenv.setVisible(true);
            fenv.repaint();
        } else {
            fenv.setVisible(false);
            envToggleButton.setSelected(false);
        }
    }//GEN-LAST:event_envBoxMenuItemActionPerformed
    
    private void treesBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_treesBoxMenuItemActionPerformed
        if (treesBoxMenuItem.isSelected()) {
            treesToggleButton.setSelected(true);
            ftrees.setVisible(true);
            ftrees.repaint();
        } else {
            ftrees.setVisible(false);
            treesToggleButton.setSelected(false);
        }
    }//GEN-LAST:event_treesBoxMenuItemActionPerformed
    
    private void ccBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ccBoxMenuItemActionPerformed
        if (ccBoxMenuItem.isSelected()) {
            ccToggleButton.setSelected(true);
            fcc.setVisible(true);
            fcc.repaint();
        } else {
            fcc.setVisible(false);
            ccToggleButton.setSelected(false);
        }
    }//GEN-LAST:event_ccBoxMenuItemActionPerformed
    
    private void optMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optMenuItemActionPerformed
        optFrame.setVisible(true);
        optFrame.setLocation(300,300);
        optFrame.setSize(640,400);
    }//GEN-LAST:event_optMenuItemActionPerformed
        
    private void funcNameDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_funcNameDisplayActionPerformed
        
        if (funcNameDisplay.isSelected())
            FunctionManager.funcNameDisplay = true;
        else
            FunctionManager.funcNameDisplay = false;
        ftrees.repaint();
    }//GEN-LAST:event_funcNameDisplayActionPerformed
    
    
    
    
    
    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
        
        removeHighlights(inputTextArea);
        
        outputTextArea.setText("");
        textIn = inputTextArea.getText();
        PVTS.resetPVTS(textIn);
        PVTS.globalBuffer = textIn;
        TextIO.fillBuffer(textIn,0);
        
        PVTS.rep();
        
        if (PVTS.error) {
            removeHighlights(inputTextArea);
            highlightCurrentSExpression(inputTextArea,ErrorMess.pos-1,ErrorMess.pos);
            inputTextArea.repaint();
            PVTSFrame.put(ErrorMess.allmessages);
        }
        
        fenv.repaint();
        ftrees.repaint();
        fcc.repaint();
        //  fenv.validate();
        //  fcc.validate();
        //  ftrees.validate();
    }//GEN-LAST:event_runButtonActionPerformed
    
    
 
    
    private void doStepButtonActionPerformed(java.awt.event.ActionEvent evt) {
        
        if (PVTSFrame.stopStepper) {
            stopStepper();
            PVTSFrame.stopStepper = false;
        }
        ReadEvalPrint.skipBlanks(); // watch out with this.
        startHighlight = TextIO.getPos();
        PVTS.rep();
        endHighlight = TextIO.getPos();
        
        mainButtonsPanel.revalidate();
        mainButtonsPanel.repaint();
        //jTabbedPane1.repaint();
        fenv.repaint();
        ftrees.repaint();
        fcc.repaint();
        
        highlightCurrentSExpression(inputTextArea,startHighlight,endHighlight);
        if (startHighlight < inputTextArea.getText().length())
            inputTextArea.setCaretPosition(startHighlight);
        
        
    }
    
    public static void put(Object o) {
        if(o.toString().equalsIgnoreCase("read: bad syntax, unexpected )"))
            o = 0;
        outputTextArea.append(o.toString());
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PVTSFrame().setVisible(true);
                // my code :
                new PVTS();
                
                // end of my code:
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame aboutFrame;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JComboBox alignmentComboBox;
    private javax.swing.JMenuItem appendMenuItem;
    private javax.swing.JMenuItem baMenuItem;
    private javax.swing.JColorChooser ccBColorColorChooser;
    private javax.swing.JFrame ccBColorFrame;
    private javax.swing.JPanel ccBColorPanel;
    private javax.swing.JCheckBoxMenuItem ccBoxMenuItem;
    private javax.swing.JColorChooser ccColorColorChooser;
    private javax.swing.JFrame ccColorFrame;
    private javax.swing.JPanel ccColorPanel;
    private javax.swing.JToggleButton ccToggleButton;
    private javax.swing.JMenuItem closeMenuItem;
    private javax.swing.JPanel colorPanel;
    private javax.swing.JMenuItem conscellsMenuItem;
    private javax.swing.JPanel controlsPanel;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JCheckBoxMenuItem envBoxMenuItem;
    private javax.swing.JPanel envPanel;
    private javax.swing.JToggleButton envToggleButton;
    private javax.swing.JMenu examplesMenu;
    private javax.swing.JColorChooser fBColorColorChooser;
    private javax.swing.JFrame fBColorFrame;
    private javax.swing.JPanel fBColorPanel;
    private javax.swing.JColorChooser fColorColorChooser;
    private javax.swing.JFrame fColorFrame;
    private javax.swing.JPanel fColorPanel;
    private javax.swing.JMenuItem factorialMenuItem;
    private javax.swing.JMenuItem fibonacciMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JComboBox fontSizeComboBox;
    private javax.swing.JPanel funcCallPanel;
    private javax.swing.JCheckBox funcNameDisplay;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JScrollPane inputScrollPane;
    private javax.swing.JTextArea inputTextArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private static javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private static javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JMenuItem langMenuItem;
    private javax.swing.JCheckBox lineWrapCheckBox;
    private javax.swing.JCheckBox listCheckBox;
    private javax.swing.JTextField lnColTextField;
    private javax.swing.JPanel mainButtonsPanel;
    private javax.swing.JMenuItem mosaicMenuItem;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JFrame optFrame;
    private javax.swing.JMenuItem optMenuItem;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JPanel outputPanel;
    public static javax.swing.JTextArea outputTextArea;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JCheckBox primCheckBox;
    private javax.swing.JMenuItem printCCMenuItem;
    private javax.swing.JMenuItem printInputMenuItem;
    private javax.swing.JMenu printMenu;
    private javax.swing.JMenuItem printOutputMenuItem;
    private javax.swing.JMenuItem printRecuMenuItem;
    private javax.swing.JMenuItem printTreesMenuItem;
    private javax.swing.JMenuItem printVarsMenuItem;
    private javax.swing.JCheckBox procCheckBox;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JButton readButton;
    public static javax.swing.JFrame readFrame;
    private javax.swing.JTextField readTextField;
    private javax.swing.JButton recuButton;
    public static javax.swing.JFrame recuFrame;
    public static javax.swing.JTextArea recuTextArea;
    private javax.swing.JCheckBox result_bubbleCheckBox;
    private javax.swing.JButton runButton;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem setStructureMenuItem;
    private javax.swing.JMenuItem sortingMenuItem;
    private javax.swing.JToggleButton stepperButton;
    private javax.swing.JComboBox tabSizeComboBox;
    private javax.swing.JCheckBoxMenuItem treesBoxMenuItem;
    private javax.swing.JToggleButton treesToggleButton;
    private javax.swing.JMenuItem updateMenuItem;
    private javax.swing.JCheckBox varCheckBox;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JSlider zoomSlider;
    // End of variables declaration//GEN-END:variables
    
    
    
    
    
}

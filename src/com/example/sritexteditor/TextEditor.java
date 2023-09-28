package com.example.sritexteditor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.IOException;
import java.util.ArrayList;

public class TextEditor{
    static boolean flag=false;
    static String sfilepath, filename="Untitled";
    static StringBuffer buffer = new StringBuffer();
    static StringBuffer text = new StringBuffer();
    static ArrayList<Character> charr = new ArrayList<>();
    static ArrayList<Integer> intarr = new ArrayList<>();

    public static void main(String[] args) {

        JFrame mf=new JFrame(filename+" - Sri TextE");
        mf.setSize(700,700);
        mf.setLayout(new BorderLayout(80,80));
        mf.setLocationRelativeTo(null);

        //JMenuItem and JMenuBar
        JMenuBar mb= new JMenuBar();
        JMenu file, edit;
        JMenuItem new1,open,save,undo,redo,saveas,exit,cut,copy,paste,selectall;
        file= new JMenu("File");
        edit= new JMenu("Edit");
        new1= new JMenuItem("New");
        open= new JMenuItem("Open...");
        save= new JMenuItem("Save");
        saveas= new JMenuItem("Save As...            ");
        exit= new JMenuItem("Exit");
        undo= new JMenuItem("Undo             Ctrl+Z");
        redo= new JMenuItem("Redo             Ctrl+Y");
        cut= new JMenuItem("Cut");
        copy= new JMenuItem("Copy");
        paste= new JMenuItem("Paste");
        selectall= new JMenuItem("Select All     Ctrl+A");
        file.add(new1);
        file.add(open);
        file.add(save);
        file.add(saveas);
        file.add(exit);
        edit.add(undo);
        edit.add(redo);
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(selectall);
        mb.add(file);
        mb.add(edit);

        //font style and size
        Font f=new Font("Times New Roman",Font.PLAIN,24);

        //The text area
        JTextArea ta= new JTextArea();
        ta.setFont(f);
        Border border = BorderFactory.createLineBorder(Color.white,5);
        ta.setBorder(border);
        ta.setSelectionColor(new Color(79, 8, 93));
        ta.setSelectedTextColor(new Color(255, 255, 255));
        ta.setWrapStyleWord(true);
        ta.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(ta);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        //popup menu
        JPopupMenu popup = new JPopupMenu("Edit");
        JMenuItem cutp,copyp,pastep,selectallp;
        cutp= new JMenuItem("Cut");
        copyp= new JMenuItem("Copy");
        pastep= new JMenuItem("Paste");
        selectallp= new JMenuItem("Select All");
        popup.add(cutp);
        popup.add(copyp);
        popup.add(pastep);
        popup.add(selectallp);

        mf.add(popup);
        mf.setJMenuBar(mb);
        mf.add(scrollPane);
        mf.setFont(f);
        mf.setVisible(true);
        mf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        //Event handling
        new1.addActionListener(e -> {
            filename = "Untitled";
            flag=false;
            ta.setText(" ");
            ta.setText("");
            mf.setTitle(filename + " - SriTextE");
        });

        open.addActionListener(e -> {
            flag=false;
            JFileChooser fc= new JFileChooser();
            fc.setDialogTitle("Open a File");
            int i=fc.showOpenDialog(mf);
            if(i==JFileChooser.APPROVE_OPTION){
                File f12 =fc.getSelectedFile();
                filename= f12.getName();
                sfilepath= f12.getPath();
                try{
                    BufferedReader br= new BufferedReader(new FileReader(sfilepath));
                    String s1;
                    StringBuilder s2= new StringBuilder();
                    while((s1=br.readLine())!=null){
                        s2.append(s1).append("\n");
                    }
                    ta.setText(s2.toString());
                    mf.setTitle(filename + " - SriTextE");
                    br.close();
                    flag=true;
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        save.addActionListener(e -> {
            if (flag) {
                mf.setTitle(filename + " - SriTextE");
                try {
                    FileWriter fWriter = new FileWriter(sfilepath);
                    fWriter.write(ta.getText());
                    fWriter.close();
                }
                catch (IOException ioe) {
                    System.out.println("An error occurred.");
                    ioe.printStackTrace();
                }
            }
            else{
                JFileChooser fc1= new JFileChooser();
                fc1.setDialogTitle("Save the File");
                int j= fc1.showSaveDialog(mf);
                if(j== JFileChooser.APPROVE_OPTION) {
                    File f1 = fc1.getSelectedFile();
                    if (!f1.getName().toLowerCase().endsWith(".txt")) {
                        f1 = new File(f1.getParentFile(), f1.getName() + ".txt");
                    }
                    filename = f1.getName();
                    sfilepath = f1.getPath();
                    try {
                        FileWriter fWriter = new FileWriter(sfilepath);
                        fWriter.write(ta.getText());
                        fWriter.close();
                        flag = true;
                        mf.setTitle(filename + " - SriTextE");
                    }
                    catch (IOException ioe) {
                        System.out.println("An error occurred.");
                        ioe.printStackTrace();
                    }
                }
            }
        });

        saveas.addActionListener(e -> {
            JFileChooser fc1= new JFileChooser();
            fc1.setDialogTitle("Save As the File");
            int j= fc1.showSaveDialog(mf);
            if(j== JFileChooser.APPROVE_OPTION) {
                File f1 = fc1.getSelectedFile();
                if (!f1.getName().toLowerCase().endsWith(".txt")) {
                    f1 = new File(f1.getParentFile(), f1.getName() + ".txt");
                }
                filename = f1.getName();
                sfilepath = f1.getPath();
                try {
                    FileWriter fWriter = new FileWriter(sfilepath);
                    fWriter.write(ta.getText());
                    fWriter.close();
                    flag = true;
                    mf.setTitle(filename + " - SriTextE");
                }
                catch (IOException ioe) {
                    System.out.println("An error occurred.");
                    ioe.printStackTrace();
                }
            }
        });

        exit.addActionListener(e -> mf.dispose());

        cut.addActionListener(e -> {
            buffer.delete(0,buffer.length());
            buffer.append(ta.getSelectedText());
            ta.replaceSelection("");
        });

        copy.addActionListener(e -> {
            buffer.delete(0,buffer.length());
            buffer.append(ta.getSelectedText());
        });

        paste.addActionListener(e -> {
            int currentCaretPosition = ta.getCaretPosition();
            ta.insert(buffer.toString(),currentCaretPosition);
        });

        selectall.addActionListener(e -> ta.selectAll());

        cutp.addActionListener(e -> {
            buffer.delete(0,buffer.length());
            buffer.append(ta.getSelectedText());
            ta.replaceSelection("");
        });

        copyp.addActionListener(e -> {
            buffer.delete(0,buffer.length());
            buffer.append(ta.getSelectedText());
        });

        pastep.addActionListener(e -> {
            int currentCaretPosition = ta.getCaretPosition();
            ta.insert(buffer.toString(),currentCaretPosition);
        });

        selectallp.addActionListener(e -> ta.selectAll());

        ta.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                mf.setTitle("*" + filename + " - SriTextE");
                text.delete(0,text.length());
                text.append(ta.getText());
                char ch = e.getKeyChar() ;
                charr.add(ch);
                int caretPosition = ta.getCaretPosition();
                intarr.add(caretPosition);
            }
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
        });

        ta.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)){
                    popup.show(ta,e.getX(),e.getY());
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        mf.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                String title= mf.getTitle();
                if(title.startsWith("*")) {
                    int a;
                    String[] options = {"Save", "Don't Save"};
                    options[0] = "Save";
                    options[1] = "Don't Save";

                    if(title.contains("Untitled")) {
                        a = JOptionPane.showOptionDialog(mf, "Do you want to save changes to Untitled?", "SriTextE", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                    }
                    else{
                        a = JOptionPane.showOptionDialog(mf, "Do you want to save changes to "+sfilepath, "SriTextE", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                    }

                    if (a == JOptionPane.YES_OPTION) {
                        if (flag) {
                            mf.setTitle(filename + " - SriTextE");
                            try {
                                FileWriter fWriter = new FileWriter(sfilepath);
                                fWriter.write(ta.getText());
                                fWriter.close();
                            } catch (IOException ioe) {
                                System.out.println("An error occurred.");
                                ioe.printStackTrace();
                            }
                        }
                        else {
                            JFileChooser fc1 = new JFileChooser();
                            fc1.setDialogTitle("Save the File");
                            int j = fc1.showSaveDialog(mf);
                            if (j == JFileChooser.APPROVE_OPTION) {
                                File f1 = fc1.getSelectedFile();
                                if (!f1.getName().toLowerCase().endsWith(".txt")) {
                                    f1 = new File(f1.getParentFile(), f1.getName() + ".txt");
                                }
                                filename = f1.getName();
                                sfilepath = f1.getPath();
                                try {
                                    FileWriter fWriter = new FileWriter(sfilepath);
                                    fWriter.write(ta.getText());
                                    fWriter.close();
                                    flag = true;
                                    mf.setTitle(filename + " - SriTextE");
                                } catch (IOException ioe) {
                                    System.out.println("An error occurred.");
                                    ioe.printStackTrace();
                                }
                            }
                        }
                        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    }
                    else if (a == JOptionPane.NO_OPTION) {
                        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    }
                }
                else{
                    mf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                }
            }
        });

        //undo and redo
        KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK);
        KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK);

        UndoManager undoManager = new UndoManager();

        Document document = ta.getDocument();
        document.addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));

        // Add ActionListeners
        undo.addActionListener((ActionEvent e) -> {
            try {
                undoManager.undo();
            }
            catch (CannotUndoException ignored) {}
        });
        redo.addActionListener((ActionEvent e) -> {
            try {
                undoManager.redo();
            }
            catch (CannotRedoException ignored) {}
        });

        // Map undo action
        ta.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(undoKeyStroke, "undoKeyStroke");
        ta.getActionMap().put("undoKeyStroke", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    undoManager.undo();
                }
                catch (CannotUndoException ignored) {}
            }
        });
        // Map redo action
        ta.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(redoKeyStroke, "redoKeyStroke");
        ta.getActionMap().put("redoKeyStroke", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    undoManager.redo();
                }
                catch (CannotRedoException ignored) {}
            }
        });
    }
}

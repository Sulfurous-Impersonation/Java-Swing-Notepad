//
// Name:       Watson, Juniper 
// Project:    #6 
// Due:        12/3/2021 
// Course:     cs-2450-01-f21 
// 
// Description: 
// A version of Windows Notepad using Swing.
//

/**TO DO
 * 
 */

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Notepad
{
    private JTextArea defaultArea = new JTextArea();
    private File activeFile;
    private Font defaultFont = new Font("Courier New", Font.PLAIN, 12);

    public Notepad()
    {
        JFrame jfrm = new JFrame("Untitled -- Notepad");
        jfrm.setFont(defaultFont);
        jfrm.setLayout(new BorderLayout());
        jfrm.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jfrm.setPreferredSize(new Dimension(800, 600));

        ImageIcon image = new ImageIcon("Notepad.png");
        jfrm.setIconImage(image.getImage());

        //create text area
        JTextArea textArea = new JTextArea();
        textArea.setFont(defaultFont);
        jfrm.add(textArea, BorderLayout.CENTER);
        defaultArea.setText(textArea.getText());
        textArea.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e) 
            {
                checkChanged(jfrm, textArea);
            }

            public void removeUpdate(DocumentEvent e)
            {
                checkChanged(jfrm, textArea);
            }

            public void changedUpdate(DocumentEvent e) 
            {
                checkChanged(jfrm, textArea);
            }
        });

        jfrm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)
            {
                if (textArea.getText().equals(defaultArea.getText()))
                {
                    //do nothing
                    jfrm.dispose();
                }
                else
                {
                    int confirmed = JOptionPane.showConfirmDialog(null, "Progress will not be saved. Exit program?", "Warning!", JOptionPane.WARNING_MESSAGE);
                    if (confirmed == JOptionPane.YES_OPTION)
                        jfrm.dispose();
                } 
            }
        });

        //make textArea scrollable
        JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jfrm.add(scroll);

        JMenuBar bar = new JMenuBar();
        bar.setFont(defaultFont);
            //File
            JMenu file = new JMenu("File");
            file.setFont(defaultFont);
            file.setMnemonic('F');
                //File > New
                JMenuItem n = new JMenuItem("New");
                n.setFont(defaultFont);
                n.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) 
                    {
                        textArea.setText("");
                    }
                });
                n.setMnemonic('N');
                n.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
                //n.setFont(new Font(n.getFont().getFontName(), Font.BOLD, n.getFont().getSize()));
                file.add(n);

                //File > Open
                JMenuItem open = new JMenuItem("Open...");
                open.setFont(defaultFont);
                open.setMnemonic('O');
                open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
                //open.setFont(new Font(open.getFont().getFontName(), Font.BOLD, open.getFont().getSize()));
                open.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) 
                    {
                        JFileChooser fileSel = new JFileChooser();
                        //FileFilter fileFilter = new FileNameExtensionFilter("JAVA ");
                        fileSel.setCurrentDirectory(new File(System.getProperty("user.home")));
                        fileSel.setFileSelectionMode(JFileChooser.FILES_ONLY);

                        fileSel.addChoosableFileFilter(new FileNameExtensionFilter("JAVA Files and Text Documents", "java", "txt"));

                        fileSel.setAcceptAllFileFilterUsed(true);

                        int returnVal = fileSel.showOpenDialog(open);
                        if (returnVal == JFileChooser.APPROVE_OPTION)
                        {
                            //set active file to selected file
                            activeFile = fileSel.getSelectedFile();
                            
                            //overwrite textArea with file contents
                            try {
                                Scanner reader = new Scanner(activeFile);
                                textArea.setText(null);
                                while(reader.hasNextLine())
                                    textArea.append(reader.nextLine() + "\n");
                            } catch (FileNotFoundException e1) {
                                System.out.println("Can't find file");
                                e1.printStackTrace();
                            }

                            //update defaultArea
                            defaultArea.setText(textArea.getText());
                            //update jfrm title
                            jfrm.setTitle(activeFile.getName() + " -- Notepad");
                        }
                        activeFile = fileSel.getSelectedFile();
                    }
                    
                });
                file.add(open);

                //File > Save
                JMenuItem save = new JMenuItem("Save");
                save.setFont(defaultFont);
                save.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) 
                    {
                        if (activeFile == null)
                        {
                            if (saveAs(save, textArea))
                            {
                                //update jfrm title
                                jfrm.setTitle(activeFile.getName() + " -- Notepad");

                                //update default area
                                defaultArea.setText(textArea.getText());
                            }     
                        }
                        else
                            save(activeFile, textArea);
                    }
                });
                save.setMnemonic('S');
                save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
                //save.setFont(new Font(save.getFont().getFontName(), Font.BOLD, save.getFont().getSize()));
                file.add(save);

                //File > Save As
                JMenuItem saveAs = new JMenuItem("Save As...");
                saveAs.setFont(defaultFont);
                saveAs.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) 
                    {
                        if (saveAs(save, textArea))
                        {
                            //update jfrm title
                            jfrm.setTitle(activeFile.getName() + " -- Notepad");

                            //update default area
                            defaultArea.setText(textArea.getText());
                        }
                    }
                });
                saveAs.setMnemonic('A');
                //saveAs.setFont(new Font(saveAs.getFont().getFontName(), Font.BOLD, saveAs.getFont().getSize()));
                file.add(saveAs);

                //File separator
                JMenuItem separator0 = new JMenuItem("------");
                separator0.setFont(defaultFont);
                (file.add(separator0)).setEnabled(false);

                //File > Page Setup (unimplemented)
                JMenuItem setup = new JMenuItem("Page Setup");
                setup.setFont(defaultFont);
                setup.setMnemonic('U');
                file.add(setup);

                //File > Print (unimplemented)
                JMenuItem print = new JMenuItem("Print...");
                print.setFont(defaultFont);
                print.setMnemonic('P');
                print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
                file.add(print);

                //separator
                JMenuItem separator1 = new JMenuItem("------");
                separator1.setFont(defaultFont);
                (file.add(separator1)).setEnabled(false);

                //File > Exit
                JMenuItem exit = new JMenuItem("Exit");
                exit.setFont(defaultFont);
                exit.setMnemonic('X');
                exit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if(textArea.getText().equals(defaultArea.getText()))
                            jfrm.dispose();
                        else
                        {
                            int confirmed = JOptionPane.showConfirmDialog(null, "Progress will not be saved. Exit program?", "Warning!", JOptionPane.WARNING_MESSAGE);
                            if (confirmed == JOptionPane.YES_OPTION)
                                jfrm.dispose();
                        }
                    }
                });
                file.add(exit);
            bar.add(file);
            
            //Edit
            JMenu edit = new JMenu("Edit");
            edit.setMnemonic('E');
            edit.setFont(defaultFont);
                //Edit > Undo (unimplemented)
                JMenuItem undo = new JMenuItem("Undo");
                undo.setFont(defaultFont);
                undo.setMnemonic('U');
                edit.add(undo);

                //separator
                JMenuItem separator2 = new JMenuItem("------");
                separator2.setFont(defaultFont);
                (edit.add(separator2)).setEnabled(false);

                //Edit > Cut
                JMenuItem cut = new JMenuItem("Cut");
                cut.setFont(defaultFont);
                cut.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) 
                    {
                        textArea.cut();
                    }
                });
                cut.setMnemonic('T');
                cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                edit.add(cut);

                //Edit > Copy
                JMenuItem copy = new JMenuItem("Copy");
                copy.setFont(defaultFont);
                copy.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) 
                    {
                        textArea.copy();
                    }
                });
                copy.setMnemonic('C');
                copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
                edit.add(copy);

                //Edit > Paste
                JMenuItem paste = new JMenuItem("Paste");
                paste.setFont(defaultFont);
                paste.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) 
                    {
                        textArea.paste();
                    }
                });
                paste.setMnemonic('P');
                paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
                edit.add(paste);

                //Edit > Delete
                JMenuItem delete = new JMenuItem("Delete");
                delete.setFont(defaultFont);
                delete.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) 
                    {
                        textArea.replaceSelection("");
                    }
                });
                delete.setMnemonic('L');
                delete.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
                edit.add(delete);

                //separator
                JMenuItem separator3 = new JMenuItem("------");
                separator3.setFont(defaultFont);
                (edit.add(separator3)).setEnabled(false);

                //Edit > Find
                //Must use modeless dialog
                JMenuItem find = new JMenuItem("Find...");
                find.setFont(defaultFont);
                find.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) 
                    {
                        FindDlg.showDialog(jfrm, textArea);
                    }
                });
                find.setMnemonic('F');
                find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
                edit.add(find);

                //Edit > Find Next (unimplemented)
                JMenuItem findNext = new JMenuItem("Find Next");
                findNext.setFont(defaultFont);
                findNext.setMnemonic('N');
                edit.add(findNext);

                //Edit > Replace (unimplemented)
                JMenuItem replace = new JMenuItem("Replace...");
                replace.setFont(defaultFont);
                replace.setMnemonic('R');
                replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
                edit.add(replace);

                //Edit > Go To
                JMenuItem go = new JMenuItem("Go To...");
                go.setFont(defaultFont);
                go.setMnemonic('G');
                go.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
                go.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        GotoDlg dialog = new GotoDlg();

                        JPanel dialogPanel = new JPanel();

                        int line = dialog.showDialog(jfrm);
                        if(line != -1)
                            dialogPanel.add(new JLabel("" + line));
                        else
                            dialogPanel.add(new JLabel("Cancel"));

                        jfrm.setContentPane(dialogPanel);
                    }
                });
                edit.add(go);

                //separator
                JMenuItem separator4 = new JMenuItem("------");
                separator4.setFont(defaultFont);
                (edit.add(separator4)).setEnabled(false);

                //Edit > Select All
                JMenuItem selectAll = new JMenuItem("Select All");
                selectAll.setFont(defaultFont);
                selectAll.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) 
                    {
                        textArea.selectAll();
                    }
                });
                selectAll.setMnemonic('A');
                selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
                edit.add(selectAll);

                //Edit > Time/Date
                JMenuItem time = new JMenuItem("Time/Date");
                time.setFont(defaultFont);
                time.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) 
                    {
                        //Date date = java.util.Calendar.getInstance().getTime();
                        Calendar calendar = new GregorianCalendar();
                        String date = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " ";
                        if (calendar.get(Calendar.AM_PM) == Calendar.AM)
                            date += "AM";
                        else
                            date += "PM";
                        date += " ";

                        if (calendar.get(Calendar.MONTH) < Calendar.OCTOBER)
                            date += "0";
                        date += (calendar.get(Calendar.MONTH) + 1) + "/";
                        if(calendar.get(Calendar.DAY_OF_MONTH) < 10)
                            date += "0";
                        date += calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR);

                        textArea.append(" " + date);
                    }
                });
                time.setMnemonic('D');
                time.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
                edit.add(time);

            bar.add(edit);

            //Format
            JMenu format = new JMenu("Format");
            format.setMnemonic('O');
            format.setFont(defaultFont);
                //Format > Word Wrap
                JCheckBoxMenuItem wrap = new JCheckBoxMenuItem("Word Wrap", false);
                wrap.setFont(defaultFont);
                wrap.addItemListener(new ItemListener(){
                    public void itemStateChanged(ItemEvent e) 
                    {
                        if (e.getStateChange() == ItemEvent.SELECTED)
                            textArea.setLineWrap(true);
                        else
                            textArea.setLineWrap(false);
                    }
                });
                wrap.setMnemonic('W');
                //wrap.setFont(new Font(wrap.getFont().getFontName(), Font.BOLD, wrap.getFont().getSize()));
                format.add(wrap);

                //Format > Font
                JMenuItem font = new JMenuItem("Font...");
                font.setFont(defaultFont);
                font.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Font fontSel = JFontChooser.showDialog(jfrm, textArea.getFont());
                        textArea.setFont(fontSel);
                    }
                });
                font.setMnemonic('F');
                //font.setFont(new Font(font.getFont().getFontName(), Font.BOLD, font.getFont().getSize()));
                format.add(font);
            bar.add(format);

            //View
            JMenu view = new JMenu("View");
            view.setMnemonic('V');
            view.setFont(defaultFont);
                //View > Status Bar (unimplemented)
                JMenuItem statusBar = new JMenuItem("Status Bar");
                statusBar.setFont(defaultFont);
                statusBar.setMnemonic('S');
                view.add(statusBar);
            bar.add(view);

            //Help
            JMenu help = new JMenu("Help");
            help.setMnemonic('H');
            help.setFont(defaultFont);
                //Help > View Help (unimplemented)
                JMenuItem viewHelp = new JMenuItem("View Help");
                viewHelp.setFont(defaultFont);
                viewHelp.setMnemonic('H');
                help.add(viewHelp);

                //Help > Extra Credits
                JMenuItem extraCredits = new JMenuItem("Extra Credits...");
                extraCredits.setFont(defaultFont);
                extraCredits.setMnemonic('X');
                help.add(extraCredits);

                //separator
                JMenuItem separator5 = new JMenuItem("------");
                separator5.setFont(defaultFont);
                (help.add(separator5)).setEnabled(false);

                //Help > About
                JMenuItem about = new JMenuItem("About Notepad");
                about.setFont(defaultFont);
                about.setMnemonic('A');
                about.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        JPanel panel = new JPanel();
                        JPanel labelPanel = new JPanel();
                        labelPanel.setLayout(new BorderLayout());
                        
                        JLabel name = new JLabel("by J. Watson");
                        name.setFont(new Font(name.getFont().getFontName(), Font.ITALIC, name.getFont().getSize()));
                        JLabel preLab = new JLabel("Notepad V0.1");
                        preLab.setFont(new Font(preLab.getFont().getFontName(), Font.ITALIC, preLab.getFont().getSize()));

                        labelPanel.add(preLab, BorderLayout.NORTH);
                        labelPanel.add(name, BorderLayout.SOUTH);

                        Icon icon = new ImageIcon("Notepad.png");
                        JLabel image = new JLabel(icon);

                        panel.add(image);
                        panel.add(labelPanel);
                        JOptionPane.showMessageDialog(null, panel, "About", JOptionPane.PLAIN_MESSAGE);
                    }
                });
                help.add(about);

            bar.add(help);

        //Popup menu for Cut, Copy, and Paste
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setFont(defaultFont);
        popupMenu.add(cut);
        popupMenu.add(copy);
        popupMenu.add(paste);

        textArea.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e)
            {
                if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1)
                {
                    popupMenu.show(jfrm, e.getX()+5, e.getY()+50);
                }
            }
        });

        jfrm.setJMenuBar(bar);
        jfrm.pack();
        jfrm.setLocationRelativeTo(null);
        jfrm.setVisible(true);
    }

    private boolean save(File file, JTextArea textArea)
    {
        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write(textArea.getText());
            writer.close();
        }
        catch (IOException e) {
            return false;
        }

        defaultArea.setText(textArea.getText());
        return true;
    }

    private boolean saveAs(JComponent parent, JTextArea textArea)
    {
        boolean success = false;
        JFileChooser saveFileChooser = new JFileChooser();
        saveFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        saveFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        saveFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JAVA Files and Text Documents", "java", "txt"));

        saveFileChooser.setAcceptAllFileFilterUsed(true);

        if (saveFileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            success = true;
            //set active file to selected file
            activeFile = saveFileChooser.getSelectedFile();
            
            save(activeFile, textArea);
        }
        activeFile = saveFileChooser.getSelectedFile();  
        
        return success;
    }

    private void checkChanged(JFrame jfrm, JTextArea textArea)
    {
        if(textArea.getText().equals(defaultArea.getText()))
                {
                    if(activeFile != null)
                        jfrm.setTitle(activeFile.getName() + " -- Notepad");
                    else
                        jfrm.setTitle("Untitled -- Notepad");
                } 
                else 
                {
                    if(activeFile != null)
                        jfrm.setTitle("*" + activeFile.getName() + " -- Notepad");
                    else
                        jfrm.setTitle("*Untitled -- Notepad");
                }
    }

    public static void main (String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                new Notepad();
            }
        });
    }
}


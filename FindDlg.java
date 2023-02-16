import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;

public class FindDlg extends JDialog
{
    private static boolean matchCase = false;
    private static boolean wrap = false;
    private static boolean searchUp = false;

    FindDlg()
    {
        super();
    }

    public static void showDialog(JFrame parent, JTextArea textArea)
    {
        JDialog dialog = createDialog(parent, textArea);

        dialog.setVisible(true);
        //dialog.dispose();
        //dialog = null;
    }

    public static JDialog createDialog(JFrame parent, JTextArea textArea)
    {
        //create panel for Find Dialog
        JPanel jfrm = new JPanel();
        jfrm.setLayout(new BorderLayout());

        //Find what and text field
        JPanel fieldPanel = new JPanel();
            JLabel findLabel = new JLabel("Find what:");
            findLabel.setDisplayedMnemonic('N');
            fieldPanel.add(findLabel);
            JTextField findField = new JTextField();
            findField.setColumns(10);
            findLabel.setLabelFor(findField);
            fieldPanel.add(findField);
        jfrm.add(fieldPanel, BorderLayout.CENTER);

        //create dialog
        JDialog dialog = new JDialog(parent, "Find", false);
        dialog.setSize(400, 125);

        //create panel for Go to/Cancel
        JPanel dialogButtonPanel = new JPanel();
            dialogButtonPanel.setLayout(new BorderLayout());
            JButton nextButton = new JButton("Find Next");
            nextButton.setMnemonic('F');
            nextButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) 
                {
                    //highlight next instance of findField.getText()
                    find(findField.getText(), textArea);
                }
            });
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) 
                {
                    //Exit panel
                    dialog.dispose();
                }
            });

            dialogButtonPanel.add(nextButton, BorderLayout.NORTH);
            dialogButtonPanel.add(cancelButton, BorderLayout.SOUTH);
        jfrm.add(dialogButtonPanel, BorderLayout.EAST);

        //create panel for checkboxes
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BorderLayout());
            JCheckBox matchCase = new JCheckBox("Match case", false);
            matchCase.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    FindDlg.matchCase = !FindDlg.matchCase;
                }
            });
            matchCase.setMnemonic('C');
            JCheckBox wrapAround = new JCheckBox("Wrap around", false);
            wrapAround.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) 
                {
                    wrap = !wrap;
                }
            });
            //wrapAround.addActionListener();
            wrapAround.setMnemonic('R');

            checkBoxPanel.add(matchCase, BorderLayout.NORTH);
            checkBoxPanel.add(wrapAround, BorderLayout.SOUTH);
        jfrm.add(checkBoxPanel, BorderLayout.WEST);

        //create panel for radio buttons
        JPanel radioButtonPanel = new JPanel();
            ButtonGroup group = new ButtonGroup();

            JRadioButton up = new JRadioButton("Up");
            up.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) 
                {
                    searchUp = true;
                }
            });

            JRadioButton down = new JRadioButton("Down", true);
            down.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) 
                {
                    searchUp = false;
                }
            });
            group.add(up);
            group.add(down);
            radioButtonPanel.add(up);
            radioButtonPanel.add(down);
        jfrm.add(radioButtonPanel, BorderLayout.SOUTH);

        dialog.add(jfrm);
        
        return dialog;
    }

    private static void find(String toFind, JTextArea textArea)
    {
        if(toFind == null || toFind.length() <= 0)
            return;

        int searchStart = -1;
        int found = -1;
        String toSearch = textArea.getText();

        if(!matchCase)
        {
            toFind.toUpperCase();
            toSearch.toUpperCase();
        }

        System.out.println("toFind: |" + toFind + "|");
        if(!searchUp) //down
        {
            searchStart = textArea.getSelectionEnd();
            found = toSearch.indexOf(toFind, searchStart);

            if(wrap && found == -1)
            {
                toSearch = textArea.getText().substring(0, searchStart);
                found = toSearch.indexOf(toFind);
            }

            System.out.println("toSearch: " + toSearch);
            System.out.println("Start of found string: " + found + "\nEnd of found string: " + (found+toFind.length()) + "\n");
        } 
        else { //up
            searchStart = textArea.getSelectionStart();
            toSearch = toSearch.substring(0, searchStart);
            found = toSearch.lastIndexOf(toFind);

            if(wrap && found == -1)
            {
                toSearch = textArea.getText();
                found = toSearch.lastIndexOf(toFind);
            }

            System.out.println("toSearch: " + toSearch);
            System.out.println("Start of found string: " + found + "\nEnd of found string: " + (found+toFind.length()) + "\n");
        }

        if(found != -1)
            textArea.select(found, found + toFind.length());
        else
            JOptionPane.showConfirmDialog(null, "Cannot find \"" + toFind + "\"", "Notepad", JOptionPane.PLAIN_MESSAGE);
        
        
        //if nothing is selected, start and end are at index of cursor
        //textArea.select(0, 1);
        System.out.println("Start: " + textArea.getSelectionStart() + "\nEnd:" + textArea.getSelectionEnd());
    }
}

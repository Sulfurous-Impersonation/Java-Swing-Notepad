//
// Name:       Watson, Juniper 
// Homework:   #2 
// Due:        date 
// Course:     cs-2450-01-f21 
// 
// Description: 
// Implementation of GoTo function for notepad.
//

import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;

public class GotoDlg extends JDialog
{
    private static int myLine = -1;

    GotoDlg()
    {
        super();
    }

    public static int showDialog(JFrame parent)
    {
        JDialog dialog = createDialog(parent);

        dialog.setVisible(true);
        dialog.dispose();
        dialog = null;
        return myLine;
    }

    public static JDialog createDialog(JFrame parent)
    {
        //create panel for Goto Dialog
        JPanel jfrm = new JPanel();
        jfrm.setLayout(new BorderLayout());
        jfrm.add(new JLabel("Line Number:"), BorderLayout.NORTH);

        //create number formatter for text area
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);

        JFormattedTextField field = new JFormattedTextField(formatter);
        field.setColumns(8);
        jfrm.add(field, BorderLayout.SOUTH);

        //create dialog
        JDialog dialog = new JDialog(parent, "Go To Line", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(200, 100);

        //create panel for Go to/Cancel
        JPanel dialogButtonPanel = new JPanel();
        dialogButtonPanel.setLayout(new BorderLayout());
        JButton okButton = new JButton("Go to");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //get currently selected font
                //set myLine to current value in text box
                myLine = Integer.parseInt(field.getText());
                dialog.dispose();
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //DON'T mess with the font
                //Exit panel
                dialog.dispose();
            }
        });
        
        dialogButtonPanel.add(okButton, BorderLayout.WEST);
        dialogButtonPanel.add(cancelButton, BorderLayout.EAST);

        dialog.add(jfrm, BorderLayout.NORTH);
        dialog.add(dialogButtonPanel, BorderLayout.SOUTH);
        
        return dialog;
    }
}

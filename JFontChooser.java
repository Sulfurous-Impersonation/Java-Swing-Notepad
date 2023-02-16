// 
// Name:      Watson, Juniper
// Project:   #4
// Due:       10/29/2021 
// Course:    CS-2450-01-f21 
// 
// Description: 
// A program to showcase different kinds of dialog boxes 
//

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JFontChooser
{
    private static Font defaultFont = Font.getFont("Arial");
    JFontChooser()
    {
        super();
    }

    JFontChooser(Font default_font)
    {
        super();
        defaultFont = default_font;
    }

    public static Font showDialog(JFrame parent, Font defFont)
    {
        if (Font.getFont(defFont.getName()) != null)
            return null;
        defaultFont = defFont;
        JDialog dialog = createDialog(parent);

        dialog.setVisible(true);
        dialog.dispose();
        dialog = null;
        return defaultFont;
    }

    public static JDialog createDialog(JFrame parent)
    {
        JPanel jfrm = new JPanel();
        jfrm.setLayout(new BorderLayout());

        //Create label to display currently selected font
        JLabel jlab = new JLabel("the quick brown fox jumps over the lazy dog 0123456789");
        jlab.setFont(defaultFont);
        jlab.setBorder(BorderFactory.createEtchedBorder());

        //Makes a JList and a JScrollPane to display system's fonts
        JList fontList = new JList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        JScrollPane listScroll = new JScrollPane();
        listScroll.setViewportView(fontList);
        fontList.setLayoutOrientation(JList.VERTICAL);
        fontList.setSelectedValue(defaultFont.getName(), true);
        JLabel fontLabel = new JLabel("Fonts:");
        fontLabel.setDisplayedMnemonic('F');
        listScroll.setColumnHeaderView(fontLabel);
        listScroll.setBorder(BorderFactory.createEtchedBorder());
        //sets fontList's selectionListener
        fontList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                    jlab.setFont(new Font(fontList.getSelectedValue().toString(), jlab.getFont().getStyle(), jlab.getFont().getSize()));
                }
            }
        });

        //Makes a JSlider to adjust font size
        JPanel slidePanel = new JPanel(new BorderLayout());
        JSlider size = new JSlider(JSlider.HORIZONTAL, 8, 20, jlab.getFont().getSize());
        JLabel sizeLabel = new JLabel("Size:");
        sizeLabel.setDisplayedMnemonic('S');
        slidePanel.add(sizeLabel, BorderLayout.WEST);
        slidePanel.add(size, BorderLayout.SOUTH);
        //sets size's changeListener
        size.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                        jlab.setFont(new Font(jlab.getFont().getFontName(), jlab.getFont().getStyle(), size.getValue()));
            }
        });

        //style, regular/italic/bold radio buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JRadioButton reg = new JRadioButton("Regular");
        JRadioButton italic = new JRadioButton("Italic");
        JRadioButton bold = new JRadioButton("Bold");

        //set regular button actionListener
        reg.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                jlab.setFont(jlab.getFont().deriveFont(Font.CENTER_BASELINE));
                jlab.setFont(new Font(jlab.getFont().getFontName(), Font.PLAIN, jlab.getFont().getSize()));
            }
        });
        reg.setMnemonic('R');

        //set italic button actionListener
        italic.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                jlab.setFont(jlab.getFont().deriveFont(Font.ITALIC));
            }
        });
        italic.setMnemonic('I');

        //set bold button actionListener
        bold.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                jlab.setFont(jlab.getFont().deriveFont(Font.BOLD));
            }
        });
        bold.setMnemonic('B');
        
        ButtonGroup style = new ButtonGroup();

        style.add(reg);
        style.add(italic);
        style.add(bold);

        buttonPanel.add(new JLabel("Style:"));
        buttonPanel.add(reg);
        buttonPanel.add(italic);
        buttonPanel.add(bold);

        // Add the labels to the content pane.
        jfrm.add(slidePanel, BorderLayout.NORTH);
        jfrm.add(listScroll, BorderLayout.WEST);
        jfrm.add(jlab, BorderLayout.SOUTH);
        jfrm.add(buttonPanel, BorderLayout.CENTER);

        JDialog dialog = new JDialog(parent, "Choose a font", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(380, 300);

        JPanel dialogButtonPanel = new JPanel();
        dialogButtonPanel.setLayout(new BorderLayout());
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //get currently selected font
                defaultFont = jlab.getFont();
                System.out.println(defaultFont.getName());
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

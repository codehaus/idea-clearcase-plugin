// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   EditableRemovableTextField.java

package org.intellij.plugins.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

// Referenced classes of package org.intellij.plugins.ui:
//            FieldButton

public class EditableRemovableTextField extends JPanel {

    private String initialBrowsingPath;
    private JTextField textField;
    private FieldButton editButton;
    private FieldButton deleteButton;
    private JPanel buttonPanel;

    public EditableRemovableTextField() {
        super(new GridBagLayout());
        initialBrowsingPath = "";
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        textField = new JTextField();
        textField.setEditable(false);
        textField.setBorder(null);
        buttonPanel = new JPanel(new BorderLayout());
        editButton = new FieldButton(textField);
        editButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                askUserToBrowseAndSelectFilePath();
            }

        });
        deleteButton = new FieldButton(textField);
        deleteButton.setIcon(null);
        deleteButton.setText("x");
        deleteButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                getTextField().setText("");
            }

        });
        buttonPanel.add(deleteButton, "West");
        buttonPanel.add(editButton, "East");
        add(textField, new GridBagConstraints(0, 0, 1, 1, 1.0D, 0.0D, 17, 2, new Insets(0, 0, 0, 0), 0, 0));
        add(buttonPanel, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
    }

    public void askUserToBrowseAndSelectFilePath() {
        JFileChooser fileChooser = new JFileChooser(initialBrowsingPath);
        fileChooser.setFileSelectionMode(2);
        if (fileChooser.showOpenDialog(this) == 0) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            getTextField().setText(filePath);
        }
    }

    public FieldButton getEditButton() {
        return editButton;
    }

    public FieldButton getDeleteButton() {
        return deleteButton;
    }

    public JTextField getTextField() {
        return textField;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }

    public boolean requestDefaultFocus() {
        editButton.requestFocus();
        return true;
    }

    public static void main(String args[]) {
        final EditableRemovableTextField f = new EditableRemovableTextField();
        JFrame jFrame = new JFrame("Test");
        jFrame.setSize(800, 700);
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add(f, "South");
        final JCheckBox checkBox = new JCheckBox("enable");
        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                f.setEnabled(checkBox.isSelected());
            }
        });
        jPanel.add(checkBox, "North");
        jFrame.getContentPane().add(jPanel);
        f.getTextField().setText("C:/test");
        jFrame.setVisible(true);
    }
}

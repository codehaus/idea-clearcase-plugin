// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   RelativeFilePathField.java

package org.intellij.plugins.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class RelativeFilePathField extends JPanel {

    private JTextField field;
    private JButton button;
    private String initialBrowsingPath;

    public RelativeFilePathField(String initialBrowsingPath) {
        this(new JTextField(), initialBrowsingPath);
    }

    public RelativeFilePathField(JTextField field, String initialBrowsingPath) {
        super(new GridBagLayout());
        this.initialBrowsingPath = ".";
        this.field = field;
        button = new JButton("...");
        this.initialBrowsingPath = initialBrowsingPath;
        initUI();
    }

    private void initUI() {
        initButton();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.weightx = 1.0D;
        constraints.fill = 2;
        add(field, constraints);
        constraints.insets = new Insets(0, 2, 0, 0);
        constraints.gridx = 1;
        constraints.weightx = 0.0D;
        constraints.fill = 0;
        add(button, constraints);
    }

    private void initButton() {
        button.setMargin(new Insets(0, 3, 1, 3));
        button.setPreferredSize(getButtonDimension());
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                askUserToBrowseAndSelectFilePath();
            }

        });
    }

    public void askUserToBrowseAndSelectFilePath() {
        JFileChooser fileChooser = new JFileChooser(initialBrowsingPath);
        fileChooser.setFileSelectionMode(0);
        if (fileChooser.showOpenDialog(button) == 0) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (filePath.startsWith(initialBrowsingPath)) {
                filePath = filePath.substring(initialBrowsingPath.length() + 1);
            }
            field.setText(filePath);
        }
    }

    public void setEnabled(boolean enabled) {
        field.setEnabled(enabled);
        field.setEditable(enabled);
        button.setEnabled(enabled);
        fixTextFieldHeightChangingWhenDisable();
    }

    private void fixTextFieldHeightChangingWhenDisable() {
        button.setSize(getButtonDimension());
    }

    private Dimension getButtonDimension() {
        int height = field.getPreferredSize().height;
        if (!button.isEnabled()) {
            height++;
        }
        return new Dimension(button.getPreferredSize().width, height);
    }

    public boolean isEnabled() {
        return field.isEnabled();
    }

    public JTextField getField() {
        return field;
    }

    public JButton getButton() {
        return button;
    }

    public void setText(String name) {
        field.setText(name);
    }

    public String getText() {
        return field.getText();
    }

    public static void main(String args[]) {
        JTextField field = new JTextField("TestCase.java");
        field.setColumns(80);
        final RelativeFilePathField f = new RelativeFilePathField(field, ".");
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
        jFrame.setVisible(true);
    }
}

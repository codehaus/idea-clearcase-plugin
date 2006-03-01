// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   CheckBoxTableCellRenderer.java

package org.intellij.plugins.ui.common;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class CheckBoxTableCellRenderer extends JCheckBox
    implements TableCellRenderer {

    private JPanel panel;

    public CheckBoxTableCellRenderer() {
        panel = new JPanel();
        setHorizontalAlignment(0);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value == null) {
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            return panel;
        }
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setSelected(((Boolean)value).booleanValue());
        return this;
    }
}

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   PathListElementTableCellRenderer.java

package org.intellij.plugins.ui.pathlisteditor;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import org.intellij.plugins.ui.common.ElipsisLabelUI;

// Referenced classes of package org.intellij.plugins.ui.pathlisteditor:
//            PathListElement

public class PathListElementTableCellRenderer extends DefaultTableCellRenderer {

    public PathListElementTableCellRenderer() {
        setUI(new ElipsisLabelUI());
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof PathListElement) {
            PathListElement elt = (PathListElement)value;
            setText(elt.getPresentableUrl());
            if (!elt.isValid()) {
                setForeground(Color.RED);
            }
        }
        if (!isSelected) {
            setBackground(table.getBackground());
        }
        return component;
    }
}

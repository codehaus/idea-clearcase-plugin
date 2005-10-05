// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   PathListTableModel.java

package org.intellij.plugins.ui.pathlisteditor;

import javax.swing.table.AbstractTableModel;

// Referenced classes of package org.intellij.plugins.ui.pathlisteditor:
//            PathListElement, PathListEditorPanel

public class PathListTableModel extends AbstractTableModel {

    private final String columnNames[];
    private final PathListEditorPanel panel;

    PathListTableModel(PathListEditorPanel panel, String columnNames[]) {
        this.panel = panel;
        this.columnNames = columnNames;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return panel.getPathListElements().size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        PathListElement elt = (PathListElement)panel.getPathListElements().get(rowIndex);
        if (columnIndex == 0) {
            return elt;
        }
        if (columnIndex == 1) {
            if (!elt.isFile()) {
                return elt.isIncludeSubDirectories() ? Boolean.TRUE : Boolean.FALSE;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return java.lang.Object.class;
        }
        if (columnIndex == 1) {
            return java.lang.Boolean.class;
        } else {
            return null;
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            PathListElement elt = (PathListElement)panel.getPathListElements().get(rowIndex);
            return !elt.isFile();
        } else {
            return false;
        }
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        PathListElement elt = (PathListElement)panel.getPathListElements().get(rowIndex);
        elt.setIncludeSubDirectories(aValue.equals(Boolean.TRUE));
    }
}

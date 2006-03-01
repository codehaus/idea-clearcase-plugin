// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   SingleRowSelectionTable.java

package org.intellij.plugins.ui.common;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class SingleRowSelectionTable extends JTable {

    public SingleRowSelectionTable() {
        this(((TableModel) (new DefaultTableModel())));
    }

    public SingleRowSelectionTable(TableModel tablemodel) {
        super(tablemodel);
        addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent event) {
                SingleRowSelectionTable.this.mousePressed(event);
            }

        });
    }

    private void mousePressed(MouseEvent event) {
        if (SwingUtilities.isRightMouseButton(event)) {
            int selectedRows[] = getSelectedRows();
            if (selectedRows.length < 2) {
                int i = rowAtPoint(event.getPoint());
                if (i != -1) {
                    getSelectionModel().setSelectionInterval(i, i);
                }
            }
        }
    }

}

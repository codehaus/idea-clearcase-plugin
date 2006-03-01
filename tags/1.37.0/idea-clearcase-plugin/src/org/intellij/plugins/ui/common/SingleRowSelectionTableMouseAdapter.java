// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   SingleRowSelectionTableMouseAdapter.java

package org.intellij.plugins.ui.common;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

// Referenced classes of package org.intellij.plugins.ui.common:
//            SingleRowSelectionTable

final class SingleRowSelectionTableMouseAdapter extends MouseAdapter {

    private final SingleRowSelectionTable table;

    SingleRowSelectionTableMouseAdapter(SingleRowSelectionTable table) {
        this.table = table;
    }

    public void mousePressed(MouseEvent event) {
        if (SwingUtilities.isRightMouseButton(event)) {
            int selectedRows[] = table.getSelectedRows();
            if (selectedRows.length < 2) {
                int i = table.rowAtPoint(event.getPoint());
                if (i != -1) {
                    table.getSelectionModel().setSelectionInterval(i, i);
                }
            }
        }
    }
}

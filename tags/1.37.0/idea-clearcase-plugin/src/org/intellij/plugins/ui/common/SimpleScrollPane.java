// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   SimpleScrollPane.java

package org.intellij.plugins.ui.common;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.ScrollPaneUI;

public class SimpleScrollPane extends JScrollPane {

    public SimpleScrollPane(JComponent component) {
        super(component);
    }

    public SimpleScrollPane() {
    }

    public void setUI(ScrollPaneUI ui) {
        super.setUI(ui);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Component component = getViewport().getView();
                if (component != null) {
                    getViewport().setBackground(component.getBackground());
                }
            }

        });
    }
}

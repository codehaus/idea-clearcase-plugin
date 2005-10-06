// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   JPanelBuilder.java

package org.intellij.plugins.util;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import com.intellij.openapi.util.Comparing;

// Referenced classes of package org.intellij.plugins.util:
//            BorderUtil

public class JPanelBuilder {
    public static class Pair {

        public Object first;
        public Object second;

        public final boolean equals(Object obj) {
            return (obj instanceof Pair) && Comparing.equal(first, ((Pair)obj).first) && Comparing.equal(second, ((Pair)obj).second);
        }

        public final int hashCode() {
            int i = 0;
            if (first != null) {
                i += first.hashCode();
            }
            if (second != null) {
                i += second.hashCode();
            }
            return i;
        }

        public Pair(Object obj, Object obj1) {
            first = obj;
            second = obj1;
        }
    }


    private String title;
    private ArrayList items;
    private ArrayList isOffset;

    public JPanelBuilder(String s) {
        title = s;
        items = new ArrayList();
        isOffset = new ArrayList();
    }

    public JPanelBuilder() {
        this(null);
    }

    public void add(JComponent component) {
        add(component, false);
    }

    public void add(JComponent component, boolean isOffset) {
        items.add(component);
        this.isOffset.add(new Boolean(isOffset));
    }

    public void add(JComponent leftComponent, JComponent rightComponent) {
        add(leftComponent, rightComponent, false);
    }

    public void add(JComponent leftComponent, JComponent rightComponent, boolean isOffset) {
        items.add(new Pair(leftComponent, rightComponent));
        this.isOffset.add(new Boolean(isOffset));
    }

    public JPanel getTitledPanel(String title) {
        this.title = title;
        return getPanel();
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel();
        if (title != null) {
            panel.setBorder(BorderUtil.newTitledBorder(title));
        }
        panel.setLayout(new GridBagLayout());
        for (int i = 0; i < items.size(); i++) {
            int weighty = 0;
            int offset = Boolean.TRUE.equals(isOffset.get(i)) ? 15 : 5;
            if (title == null) {
                offset -= 4;
            }
            Object obj = items.get(i);
            if (obj instanceof JComponent) {
                JComponent c = (JComponent)obj;
                byte vspace = (byte)((c instanceof JLabel) || (c instanceof JTextField) ? 2 : 0);
                panel.add(c, new GridBagConstraints(0, i, 2, 1, 1.0D, weighty, 18, 2, new Insets(vspace, offset, vspace, 5), 0, 0));
            } else {
                Pair pair = (Pair)obj;
                JComponent c1 = (JComponent)pair.first;
                c1.setPreferredSize(c1.getPreferredSize());
                JComponent c2 = (JComponent)pair.second;
                byte vspace = (byte)((c1 instanceof JLabel) || (c1 instanceof JTextField) ? 2 : 0);
                panel.add(c1, new GridBagConstraints(0, i, 1, 1, 0.0D, weighty, 17, 2, new Insets(vspace, offset, vspace, 5), 0, 0));
                vspace = (byte)((c2 instanceof JLabel) || (c2 instanceof JTextField) ? 2 : 0);
                panel.add(c2, new GridBagConstraints(1, i, 1, 1, 0.0D, weighty, 17, 2, new Insets(vspace, 5, vspace, 5), 0, 0));
            }
        }

        JPanel jpanel = new JPanel();
        jpanel.setPreferredSize(new Dimension(0, 0));
        panel.add(jpanel, new GridBagConstraints(0, items.size(), 2, 1, 1.0D, 1.0D, 11, 2, new Insets(0, 0, 0, 0), 0, 0));
        return panel;
    }
}

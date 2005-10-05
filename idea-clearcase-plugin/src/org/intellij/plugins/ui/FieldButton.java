// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   FieldButton.java

package org.intellij.plugins.ui;

import java.awt.*;

import javax.swing.*;

// Referenced classes of package org.intellij.plugins.ui:
//            IconUtil

public class FieldButton extends JButton {

    private final int size;
    private final JComponent component;

    private FieldButton(int size, JComponent component) {
        super(IconUtil.getIcon("/icons/ellipsis.png"));
        this.size = size;
        this.component = component;
        setMargin(new Insets(0, 0, 0, 0));
        setDefaultCapable(false);
        setFocusable(false);
    }

    public FieldButton(int i) {
        this(i, null);
        if (i <= 0) {
            throw new IllegalArgumentException("wrong size: " + i);
        } else {
            return;
        }
    }

    public FieldButton(JComponent component) {
        this(-1, component);
        if (component == null) {
            throw new IllegalArgumentException("component cannot be null");
        } else {
            return;
        }
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    public Dimension getPreferredSize() {
        if (component != null) {
            int h = component.getPreferredSize().height;
            return new Dimension(h, h);
        }
        if (size != -1) {
            return new Dimension(size, size);
        } else {
            throw new IllegalStateException("component==null and size==-1");
        }
    }
}

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   ScrollPaneBackgroundSetter.java

package org.intellij.plugins.ui.common;

import java.awt.*;

// Referenced classes of package org.intellij.plugins.ui.common:
//            SimpleScrollPane

class ScrollPaneBackgroundSetter
    implements Runnable {

    private final SimpleScrollPane scrollPane;

    ScrollPaneBackgroundSetter(SimpleScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public void run() {
        Component component = scrollPane.getViewport().getView();
        if (component != null) {
            scrollPane.getViewport().setBackground(component.getBackground());
        }
    }
}

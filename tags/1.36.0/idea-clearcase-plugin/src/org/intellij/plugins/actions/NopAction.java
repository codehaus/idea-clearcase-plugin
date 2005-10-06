// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   NopAction.java

package org.intellij.plugins.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class NopAction extends AnAction {

    private static NopAction instance = new NopAction();

    public NopAction() {
    }

    public void update(AnActionEvent e) {
        e.getPresentation().setVisible(false);
    }

    public void actionPerformed(AnActionEvent anactionevent) {
    }

    public static NopAction getInstance() {
        return instance;
    }

}

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   AboutAction.java

package net.sourceforge.transparent.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import net.sourceforge.transparent.Version;
import org.intellij.openapi.DataContextUtil;

public class AboutAction extends AnAction {

    public AboutAction() {
    }

    public void actionPerformed(AnActionEvent e) {
        Messages.showMessageDialog(DataContextUtil.getProject(e), "Clearcase plugin version " + (new Version()).getVersion(), "Clearcase Plugin", Messages.getInformationIcon());
    }
}

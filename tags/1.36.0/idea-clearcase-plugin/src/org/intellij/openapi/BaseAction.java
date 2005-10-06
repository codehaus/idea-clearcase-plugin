// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   BaseAction.java

package org.intellij.openapi;

import javax.swing.*;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

// Referenced classes of package org.intellij.openapi:
//            DataContextUtil

public abstract class BaseAction extends AnAction {

    public BaseAction() {
    }

    public BaseAction(String text) {
        super(text);
    }

    public BaseAction(String text, String description, Icon icon) {
        super(text, description, icon);
    }

    protected final Project getProject(AnActionEvent event) {
        return DataContextUtil.getProject(event);
    }
}

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   DifferencesAction.java

package net.sourceforge.transparent.actions;

import com.intellij.openapi.vfs.VirtualFile;

// Referenced classes of package net.sourceforge.transparent.actions:
//            AsynchronousAction, DiffDialog, ActionContext

public class DifferencesAction extends AsynchronousAction {

    public DifferencesAction() {
    }

    public void perform(VirtualFile file, ActionContext context) {
        DiffDialog diffDialog = new DiffDialog(context.project, file);
        diffDialog.show();
    }

    protected String getActionName(ActionContext context) {
        return "Differences";
    }
}

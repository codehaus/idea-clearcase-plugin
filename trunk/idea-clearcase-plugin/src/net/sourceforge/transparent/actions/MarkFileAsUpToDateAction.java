// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   MarkFileAsUpToDateAction.java

package net.sourceforge.transparent.actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcsHelper;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;

// Referenced classes of package net.sourceforge.transparent.actions:
//            SynchronousAction, ActionContext

public class MarkFileAsUpToDateAction extends SynchronousAction {

    public MarkFileAsUpToDateAction() {
    }

    protected boolean isEnabled(VirtualFile file, ActionContext context) {
        return true;
    }

    protected void perform(VirtualFile file, ActionContext context) throws VcsException {
        refreshFile(file, context);
    }

    protected void changeFileStatus(Project project, VirtualFile file) {
        AbstractVcsHelper.getInstance(project).markFileAsUpToDate(file);
    }

    protected String getActionName(ActionContext context) {
        return "Mark File as Up-to-date";
    }
}

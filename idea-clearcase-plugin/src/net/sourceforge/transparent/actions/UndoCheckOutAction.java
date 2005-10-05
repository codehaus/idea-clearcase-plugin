// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   UndoCheckOutAction.java

package net.sourceforge.transparent.actions;

import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.openapi.OpenApiFacade;

// Referenced classes of package net.sourceforge.transparent.actions:
//            SynchronousAction, ActionContext

public class UndoCheckOutAction extends SynchronousAction {

    public UndoCheckOutAction() {
    }

    protected boolean isEnabled(VirtualFile file, ActionContext context) throws VcsException {
        return OpenApiFacade.getFileStatusManager(context.project).getStatus(file) == FileStatus.MODIFIED;
    }

    protected void perform(VirtualFile file, ActionContext context) throws VcsException {
        context.vcs.undoCheckoutFile(file.getPath());
    }

    protected String getActionName(ActionContext context) {
        return "Undo Checkout";
    }
}

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   HijackAction.java

package net.sourceforge.transparent.actions;

import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import net.sourceforge.transparent.CheckOutHelper;

// Referenced classes of package net.sourceforge.transparent.actions:
//            SynchronousAction, ActionContext

public class HijackAction extends SynchronousAction {

    public HijackAction() {
    }

    protected boolean isEnabled(VirtualFile file, ActionContext context) throws VcsException {
        return !file.isWritable();
    }

    protected void perform(VirtualFile file, ActionContext context) throws VcsException {
        (new CheckOutHelper(context.vcs)).hijackFile(file);
    }

    protected String getActionName(ActionContext context) {
        return "Hijack File";
    }
}

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   AddAction.java

package net.sourceforge.transparent.actions;

import java.io.File;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcsHelper;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import net.sourceforge.transparent.TransparentVcs;
import net.sourceforge.transparent.actions.checkin.CheckInOptionsHandler;

// Referenced classes of package net.sourceforge.transparent.actions:
//            SynchronousAction, ActionContext

public class AddAction extends SynchronousAction {

    private CheckInOptionsHandler ciHandler;

    public AddAction() {
        ciHandler = new CheckInOptionsHandler();
    }

    protected boolean isEnabled(VirtualFile file, ActionContext context) {
        return true;
    }

    protected void perform(VirtualFile file, ActionContext context) throws VcsException {
        if (ciHandler.canCheckIn(file, context)) {
            doAdd(context.vcs, file.getParent().getPath().replace('/', File.separatorChar), file.getName(), file.isDirectory(), ciHandler.getComment());
            refreshFile(file, context);
        }
    }

    protected void changeFileStatus(Project project, VirtualFile file) {
        AbstractVcsHelper.getInstance(project).markFileAsUpToDate(file);
    }

    private void doAdd(TransparentVcs vcs, String parentPath, String fileName, boolean isDirectory, String comment) throws VcsException {
        if (isDirectory) {
            vcs.addDirectory(parentPath, fileName, comment);
        } else {
            vcs.addFile(parentPath, fileName, comment);
        }
    }

    protected void resetTransactionIndicators(ActionContext context) {
        ciHandler.reset();
    }

    protected String getActionName(ActionContext context) {
        return "Add File";
    }
}

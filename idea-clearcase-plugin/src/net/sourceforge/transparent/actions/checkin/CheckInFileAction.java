// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   CheckInFileAction.java

package net.sourceforge.transparent.actions.checkin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcsHelper;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import net.sourceforge.transparent.actions.ActionContext;
import net.sourceforge.transparent.actions.SynchronousAction;
import org.intellij.openapi.OpenApiFacade;

// Referenced classes of package net.sourceforge.transparent.actions.checkin:
//            CheckInOptionsHandler

public class CheckInFileAction extends SynchronousAction {

    private CheckInOptionsHandler ciHandler;

    public CheckInFileAction() {
        ciHandler = new CheckInOptionsHandler();
    }

    protected boolean isEnabled(VirtualFile file, ActionContext context) throws VcsException {
        return OpenApiFacade.getFileStatusManager(context.project).getStatus(file) == FileStatus.MODIFIED;
    }

    protected void perform(VirtualFile file, ActionContext context) throws VcsException {
        if (ciHandler.canCheckIn(file, context)) {
            AbstractVcsHelper.getInstance(context.project).doCheckinFiles(new VirtualFile[] {file},
                                                                          ciHandler.getComment());
            refreshFile(file, context);
        }
    }

    protected void changeFileStatus(Project project, VirtualFile file) {
        AbstractVcsHelper.getInstance(project).markFileAsUpToDate(file);
    }

    protected void resetTransactionIndicators(ActionContext context) {
        ciHandler.reset();
    }

    protected String getActionName(ActionContext context) {
        return "Check In...";
    }
}

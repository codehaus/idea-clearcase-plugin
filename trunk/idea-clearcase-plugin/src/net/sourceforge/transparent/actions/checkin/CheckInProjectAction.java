// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   CheckInProjectAction.java

package net.sourceforge.transparent.actions.checkin;

import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.VcsException;
import net.sourceforge.transparent.actions.ActionContext;
import net.sourceforge.transparent.actions.VcsAction;

import java.util.ArrayList;
import java.util.List;

public class CheckInProjectAction extends VcsAction {

    public CheckInProjectAction() {
    }

    protected List execute(ActionContext context) {
        Project project = context.project;
        Module module   = context.module;
        FileDocumentManager.getInstance().saveAllDocuments();
        CheckInConfig config = CheckInConfig.getInstance(project);
        CheckInProjectDialog dialog = new CheckInProjectDialog(module, config.getCheckInEnvironment());
        ArrayList expections = new ArrayList();
        try {
        dialog.analyzeChanges(false, null);
            if (!dialog.hasDiffs()) {
                showUserNothingToCheckInWarning(project, dialog);
            } else {
                askUserAndCheckIn(dialog, config, project, expections);
            }
        } catch (VcsException e) {
            expections.add(e);
        }
        return expections;
    }

    private void askUserAndCheckIn(CheckInProjectDialog dialog, CheckInConfig config, Project project, ArrayList expections) {
//        try {
            throw new UnsupportedOperationException("Code not finished yet.");
//            if (askForCheckInConfirmation(dialog, config)) {
//                AbstractVcsHelper.getInstance(project).doCheckinProject((CheckinProjectPanel) dialog.getCheckinProjectPanel(), dialog.getPreparedComment(config.getCheckInEnvironment()));
//            }
//        }
//        catch (VcsException e) {
//            expections.add(e);
//        }
    }

    private void showUserNothingToCheckInWarning(Project project, CheckInProjectDialog dialog) {
        Messages.showMessageDialog(project, "Nothing to check in", "Nothing Found", Messages.getInformationIcon());
        dialog.dispose();
    }

    private boolean askForCheckInConfirmation(CheckInProjectDialog dialog, CheckInConfig config) throws VcsException {
        config.copyToDialog(dialog);
        dialog.show();
        boolean ok = dialog.isOK();
        if (ok) {
            config.copyFromDialog(dialog);
        }
        return ok;
    }

    protected String getActionName(ActionContext context) {
        return "Check In Project";
    }
}

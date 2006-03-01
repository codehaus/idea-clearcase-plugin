// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   CheckInOptionsHandler.java

package net.sourceforge.transparent.actions.checkin;

import java.io.File;

import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import net.sourceforge.transparent.ClearCase;
import net.sourceforge.transparent.TransparentVcs;
import net.sourceforge.transparent.actions.ActionContext;

// Referenced classes of package net.sourceforge.transparent.actions.checkin:
//            CheckInConfig, CheckInFileDialog, CheckInEnvironment

public class CheckInOptionsHandler {

    private boolean forAllChosen;
    private String comment;

    public CheckInOptionsHandler() {
        forAllChosen = false;
    }

    public boolean canCheckIn(VirtualFile file, ActionContext context) throws VcsException {
        CheckInConfig config = CheckInConfig.getInstance(context.project);
        TransparentVcs vcs = context.vcs;
        ClearCase cc = vcs.getClearCase();
        String comment = cc.getCheckoutComment(new File(file.getPresentableUrl()));
        config.getCheckInEnvironment().setComment(comment);
        config.setComment(comment);

        CheckInFileDialog d = new CheckInFileDialog(context.project,
                                                    new VirtualFile[] {file},
                                                    config.getCheckInEnvironment(),
                                                    context.event.getInputEvent().getModifiers());

        return askForCheckInConfirmation(d, config);
    }

    public boolean askForCheckInConfirmation(CheckInFileDialog dialog, CheckInConfig config) throws VcsException {
        if (forAllChosen) {
            return true;
        }
        config.copyToDialog(dialog);
        dialog.show();
        int dialogResult = dialog.getExitCode();
        if (dialogResult == 1) {
            return false;
        }
        if (dialogResult == 2) {
            forAllChosen = true;
        }
        config.copyFromDialog(dialog);
        comment = dialog.getComment();
        return true;
    }

    public void reset() {
        forAllChosen = false;
    }

    public String getComment() {
        return comment;
    }

    public boolean isForAllChosen() {
        return forAllChosen;
    }
}

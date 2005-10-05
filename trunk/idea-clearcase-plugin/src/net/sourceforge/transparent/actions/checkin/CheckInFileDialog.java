// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   CheckInFileDialog.java

package net.sourceforge.transparent.actions.checkin;

import java.awt.event.ActionEvent;

import javax.swing.*;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.ui.CheckinFileDialog;
import com.intellij.openapi.vfs.VirtualFile;

// Referenced classes of package net.sourceforge.transparent.actions.checkin:
//            CheckInFields, CheckInOptionsPanel, CheckInEnvironment

public class CheckInFileDialog extends CheckinFileDialog
    implements CheckInFields {

    public static final int OK_ALL_EXIT_CODE = 2;
    private Action actionButtons[];
    private CheckInEnvironment env;

    public void setScr(String scr) {
        getOptionsPanel().setScr(scr);
    }

    public String getScr() {
        return getOptionsPanel().getScr();
    }

    public void setShowScrField(boolean showField) {
        getOptionsPanel().setShowScrField(showField);
    }

    public boolean isShowScrField() {
        return getOptionsPanel().isShowScrField();
    }

    public CheckInFileDialog(Project project, VirtualFile files[], CheckInEnvironment env, int modifiers) {
        super(project, env, FilePathAdapter.adapt(files));
        this.env = env;
        if (files.length > 0) {
            setTitle("Check In " + files[0]);
        }
    }

    private Action[] getActionButtons() {
        Action okAll = new AbstractAction("OK to All") {
            public void actionPerformed(ActionEvent e) {
                if (isEnabled()) {
                    close(OK_ALL_EXIT_CODE);
                }
            }
        };
        return (new Action[] {
            getOKAction(), okAll, getCancelAction()
        });
    }

    protected Action[] createActions() {
        if (actionButtons == null) {
            actionButtons = getActionButtons();
        }
        return actionButtons;
    }

    private CheckInOptionsPanel getOptionsPanel() {
        return env.getAdditionalOptionsPanel();
    }
}

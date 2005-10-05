// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   CheckInProjectDialog.java

package net.sourceforge.transparent.actions.checkin;

import java.util.Arrays;

import javax.swing.*;

import com.intellij.openapi.localVcs.LocalVcs;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vcs.CheckinProjectDialog;
import com.intellij.openapi.project.Project;

// Referenced classes of package net.sourceforge.transparent.actions.checkin:
//            CheckInFields, CheckInOptionsPanel, CheckInEnvironment

public class CheckInProjectDialog extends CheckinProjectDialog
    implements CheckInFields {

    private CheckInEnvironment env;

    public CheckInProjectDialog(Module module, CheckInEnvironment checkInEnvironment) {
        super(module, "Check In Project", true, checkInEnvironment, Arrays.asList(LocalVcs.getInstance(module.getProject()).getRootPaths()));
        env = checkInEnvironment;
    }

    public JComponent getPreferredFocusedComponent() {
        return getOptionsPanel().getScrField();
    }

    private CheckInOptionsPanel getOptionsPanel() {
        return env.getAdditionalOptionsPanel();
    }

    public void setScr(String scr) {
        getOptionsPanel().setScr(scr);
    }

    public String getScr() {
        return getOptionsPanel().getScr();
    }

    public void setShowScrField(boolean showField) {
        getOptionsPanel().setShowScrField(showField);
    }
}

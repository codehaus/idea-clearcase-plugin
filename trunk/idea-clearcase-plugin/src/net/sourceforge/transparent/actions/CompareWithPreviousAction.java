package net.sourceforge.transparent.actions;

import com.intellij.openapi.vfs.VirtualFile;

public class CompareWithPreviousAction extends AsynchronousAction {

    public void perform(VirtualFile file, ActionContext context) {
        cleartool("diff", "-g", "-predecessor", getVersionExtendedPathName(file, context));
    }

    protected String getActionName(ActionContext context) {
        return "Compare with Previous Version";
    }
}

package net.sourceforge.transparent.actions;

import com.intellij.openapi.vfs.VirtualFile;
import net.sourceforge.transparent.ClearcaseUtils;

public class CompareWithPreviousAction extends AsynchronousAction {

    public void perform(VirtualFile file, ActionContext context) {
        String versionExtendedPathName = getVersionExtendedPathName(file, context);
        ClearcaseUtils.cleartool("diff", "-g", "-predecessor", versionExtendedPathName);
    }

    protected String getActionName(ActionContext context) {
        return "Compare with Previous Version";
    }
}

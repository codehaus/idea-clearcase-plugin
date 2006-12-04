package net.sourceforge.transparent.actions;

import com.intellij.openapi.vfs.VirtualFile;
import net.sourceforge.transparent.ClearcaseUtils;

public class VersionTreeAction extends AsynchronousAction {

    public void perform(VirtualFile file, ActionContext context) {
        ClearcaseUtils.cleartool("lsvtree", "-g", getVersionExtendedPathName(file, context));
    }

    protected String getActionName(ActionContext context) {
        return "Version Tree";
    }
}

package net.sourceforge.transparent.actions;

import com.intellij.openapi.vfs.VirtualFile;
import net.sourceforge.transparent.ClearcaseUtils;

public class UpdateProjectAction extends AsynchronousAction {

    public void perform(VirtualFile file, ActionContext context) {
        ClearcaseUtils.cleartool("update", "-graphical", context.vcs.getTransparentConfig().clearcaseRoot);
    }

    protected String getActionName(ActionContext context) {
        return "Update Project";
    }
}

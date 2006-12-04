package net.sourceforge.transparent.actions;

import com.intellij.openapi.vfs.VirtualFile;
import net.sourceforge.transparent.ClearcaseUtils;

public class HistoryAction extends AsynchronousAction {

    public void perform(VirtualFile file, ActionContext context) {
        ClearcaseUtils.cleartool("lsh", "-g", getVersionExtendedPathName(file, context));
    }

    protected String getActionName(ActionContext context) {
        return "History";
    }
}

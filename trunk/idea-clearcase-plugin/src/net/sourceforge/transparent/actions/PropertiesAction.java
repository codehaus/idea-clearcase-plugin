package net.sourceforge.transparent.actions;

import com.intellij.openapi.vfs.VirtualFile;
import net.sourceforge.transparent.ClearcaseUtils;

public class PropertiesAction extends AsynchronousAction {

    public void perform(VirtualFile file, ActionContext context) {
        ClearcaseUtils.cleartool("describe", "-g", file.getPath());
    }

    protected String getActionName(ActionContext context) {
        return "Properties";
    }
}

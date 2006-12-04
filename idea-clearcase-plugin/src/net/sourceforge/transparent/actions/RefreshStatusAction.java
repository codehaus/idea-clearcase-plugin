package net.sourceforge.transparent.actions;

import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import net.sourceforge.transparent.*;

import java.io.File;

/**
 * @author Gilles Philippart
 */
public class RefreshStatusAction extends SynchronousAction {

    protected void perform(VirtualFile virtualfile, ActionContext actionContext) throws VcsException {
        TransparentVcs vcs = actionContext.vcs;
        ClearCase clearCase = vcs.getClearCase();
        if (clearCase instanceof ClearCaseDecorator) {
            clearCase = ((ClearCaseDecorator) clearCase).getClearCase();
        }
        if (clearCase instanceof NewNativeClearCase) {
            NewNativeClearCase cc = (NewNativeClearCase) clearCase;
            String path = virtualfile.getPath();
            Status status = cc.getStatus(new File(path));
            System.out.println(status + "\t" + ", path=" + path);
        }
    }

    protected String getActionName(ActionContext actioncontext) {
        return "Refresh status";
    }
}

package net.sourceforge.transparent.actions;

import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import net.sourceforge.transparent.ClearCase;
import net.sourceforge.transparent.ClearCaseDecorator;
import net.sourceforge.transparent.NewNativeClearCase;
import net.sourceforge.transparent.TransparentVcs;
import net.sourceforge.transparent.activity.ActivitySelectionDialog;

/**
 * @author Gilles Philippart
 */
public class ActivitiesAction extends SynchronousAction {

    protected void perform(VirtualFile virtualfile, ActionContext context) throws VcsException {
        System.out.println("ActivitiesAction.perform");
        TransparentVcs vcs = context.vcs;
        ClearCase clearCase = vcs.getClearCase();
        if (clearCase instanceof ClearCaseDecorator) {
            clearCase = ((ClearCaseDecorator) clearCase).getClearCase();
        }
        if (clearCase instanceof NewNativeClearCase) {
            NewNativeClearCase cc = (NewNativeClearCase) clearCase;
            String path = virtualfile.getPath();
            System.out.println("File path = " + path);
            ActivitySelectionDialog dialog = new ActivitySelectionDialog(context.project, cc, path);
            dialog.show();
        }
    }

    protected String getActionName(ActionContext actioncontext) {
        return "Select an activity";
    }

    public boolean isEnabled(ActionContext context) {
        TransparentVcs vcs = context.vcs;
        ClearCase clearCase = vcs.getClearCase();
        if (clearCase instanceof ClearCaseDecorator) {
            clearCase = ((ClearCaseDecorator) clearCase).getClearCase();
        }
        System.out.println("clearCase = " + clearCase);
        return clearCase instanceof NewNativeClearCase;
    }
}

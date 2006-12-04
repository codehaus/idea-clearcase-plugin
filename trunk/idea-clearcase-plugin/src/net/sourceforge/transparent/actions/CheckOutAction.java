// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   CheckOutAction.java

package net.sourceforge.transparent.actions;

import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import net.sourceforge.transparent.Status;
import org.intellij.plugins.util.CommandUtil;

// Referenced classes of package net.sourceforge.transparent.actions:
//            SynchronousAction, ActionContext

public class CheckOutAction
    extends SynchronousAction {

    public CheckOutAction() {
    }

    public CheckOutAction(CommandUtil commandUtil) {
        super(commandUtil);
    }

    protected void perform(VirtualFile file, ActionContext context)
        throws VcsException {
        boolean keepHijack = false;
        Status fileStatus = context.vcs.getFileStatus(file);
        if (Status.HIJACKED.equals(fileStatus)) {
            String message = "The file " + file.getPresentableUrl() + " has been hijacked. \n" +
                "Would you like to use it as the checked-out file?\n" +
                "  If not it will be lost.";
            int answer = Messages.showYesNoDialog(context.vcs.getProject(), message, "Checkout hijacked file",
                Messages.getQuestionIcon());
            keepHijack = answer == 0;
        } else if (Status.NOT_AN_ELEMENT.equals(fileStatus)) {
            throw new VcsException("CheckOut : File is not an element.");
        }
        context.vcs.checkoutFile(file.getPath(), keepHijack);
        refreshFile(file, context);
    }

    protected String getActionName(ActionContext context) {
        return "Check Out...";
    }
}

package net.sourceforge.transparent.actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.TransactionRunnable;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.openapi.OpenApiFacade;
import org.intellij.plugins.util.CommandUtil;

import java.util.List;

public abstract class SynchronousAction extends FileAction {

    protected boolean isCancelled;
    private CommandUtil commandUtil;

    public SynchronousAction(CommandUtil commandUtil) {
        this.commandUtil = commandUtil;
    }

    public SynchronousAction() {
        this(new CommandUtil());
    }

    protected List<VcsException> runAction(ActionContext context) {
        context.isActionRecursive = isActionRecursive(context);
        isCancelled = false;
        return super.runAction(context);
    }

    protected boolean isActionRecursive(ActionContext context) {
        for (VirtualFile file : context.files) {
            if (file.isDirectory()) {
                return askIfShouldRecurse(context);
            }
        }

        return false;
    }

    protected boolean askIfShouldRecurse(ActionContext context) {
        int r = Messages.showYesNoDialog(context.vcs.getProject(), "Should the action be recursive", "Recursive Action Question", Messages.getQuestionIcon());
        return r == 0;
    }

    protected List<VcsException> execute(final ActionContext context) {
        List<VcsException> exceptions = context.vcsHelper.runTransactionRunnable(context.vcs, new TransactionRunnable() {
            public void run(List exceptions) {
                execute(exceptions, context);
            }
        }, "");

        return exceptions;
    }

    protected void execute(List exceptions, ActionContext context) {
        for (VirtualFile file : context.files) {
            execute(file, context, exceptions);
        }

        resetTransactionIndicators(context);
    }

    private void execute(VirtualFile file, ActionContext context, List exceptions) {
        if (isCancelled) {
            return;
        }
        try {
            performAndRefreshStatus(file, context);
        }
        catch (VcsException ex) {
            ex.setVirtualFile(file);
            exceptions.add(ex);
        }
        catch (RuntimeException ex) {
            VcsException e = new VcsException(ex);
            e.setVirtualFile(file);
            exceptions.add(e);
        }
        handleRecursiveExecute(file, context, exceptions);
    }

    private void handleRecursiveExecute(VirtualFile file, ActionContext context, List exceptions) {
        if (isCancelled) {
            return;
        }
        if (file.isDirectory() && context.isActionRecursive) {
            for (VirtualFile child : file.getChildren()) {
                execute(child, context, exceptions);
            }
        }
    }

    private void performAndRefreshStatus(VirtualFile file, ActionContext context) throws VcsException {
        if (!isEnabled(file, context)) {
            return;
        } else {
            perform(file, context);
            return;
        }
    }

    protected abstract void perform(VirtualFile virtualfile, ActionContext actioncontext) throws VcsException;

    protected void refreshFile(final VirtualFile file, final ActionContext context) {
        commandUtil.runWriteActionWithoutException(new CommandUtil.Command() {
            public Object run() {
                file.refresh(false, true, new Runnable() {

                    public void run() {
                        changeFileStatus(context.project, file);
                        notifyFileStatusChanged(context.project, file);
                    }
                });
                return null;
            }
        });
    }

    private void notifyFileStatusChanged(Project project, VirtualFile file) {
        OpenApiFacade.getFileStatusManager(project).fileStatusChanged(file);
    }

    protected void resetTransactionIndicators(ActionContext actioncontext) {
    }

    protected void changeFileStatus(Project project1, VirtualFile virtualfile) {
    }
}

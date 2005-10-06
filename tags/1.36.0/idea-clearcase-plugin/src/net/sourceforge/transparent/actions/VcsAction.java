package net.sourceforge.transparent.actions;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.localVcs.LvcsAction;
import com.intellij.openapi.vcs.VcsException;
import org.intellij.openapi.OpenApiFacade;
import org.intellij.plugins.util.LogUtil;

import java.util.List;

public abstract class VcsAction extends AnAction {

    public static final Logger LOG = LogUtil.getLogger();

    public void update(AnActionEvent e) {
        ActionContext context = getActionContext(e);
        logAction("update", context);
        Presentation presentation = e.getPresentation();
        if (isVisible(context)) {
            presentation.setVisible(true);
            presentation.setEnabled(isEnabled(context));
            presentation.setText(getActionName(context));
        } else {
            presentation.setVisible(false);
        }
    }

    protected boolean isVisible(ActionContext c) {
        return c.isVcsActive();
    }

    protected boolean isEnabled(ActionContext c) {
        return true;
    }

    protected ActionContext getActionContext(AnActionEvent e) {
        return new ActionContext(e);
    }

    protected void logAction(String m, ActionContext c) {
        debug("enter: " + m + "(" + getActionName(c) + ", id='" + getId() + "')");
        debug(c.toString());
    }

    private String getId() {
        ActionManager manager = OpenApiFacade.getActionManager();
        if (manager == null) {
            return "<unknown>";
        }
        return manager.getId(this);
    }

    protected abstract String getActionName(ActionContext actioncontext);

    protected void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    public void actionPerformed(AnActionEvent event) {
        ActionContext c = getActionContext(event);
        logAction("actionPerformed", c);
        if (!isEnabled(c)) {
            return;
        }
        OpenApiFacade.getFileDocumentManager().saveAllDocuments();
        showExceptions(runAction(c), c);
    }

    protected List<VcsException> runAction(ActionContext context) {
        String actionName = getActionName(context);
        LvcsAction lvcsAction = null;
        if (actionName != null) {
            lvcsAction = context.vcsHelper.startVcsAction(actionName);
        }
        List<VcsException> list;
        try {
            list = execute(context);
        }
        finally {
            if (actionName != null) {
                context.vcsHelper.finishVcsAction(lvcsAction);
            }
        }
        return list;
    }

    protected List<VcsException> execute(ActionContext context) {
        return null;
    }

    protected void showExceptions(List<VcsException> exceptions, ActionContext context) {
        if (exceptions != null && !exceptions.isEmpty()) {
            String actionName = getActionName(context);
            context.vcsHelper.showErrors(exceptions, actionName == null ? context.vcs.getName() : actionName);
        }
    }
}

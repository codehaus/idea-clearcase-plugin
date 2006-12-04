// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   FileAction.java

package net.sourceforge.transparent.actions;

import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.openapi.OpenApiFacade;

public abstract class FileAction extends VcsAction {

    protected boolean hasFileTarget(ActionContext c) {
        return c.files != null && c.files.length != 0;
    }

    public boolean isEnabled(ActionContext context) {
        if (!hasFileTarget(context)) {
            debug(
                "Action " + getActionName(context) + " disable: " + (context.files != null ? "0 files" : "files=null"));
            return false;
        }
        boolean enabled = false;
        for (VirtualFile file : context.files) {
            try {
                if (isEnabled(file, context)) {
                    enabled = true;
                }
            } catch (VcsException e1) {
            }
        }

        logActionState(enabled, context);
        return enabled;
    }

    private void logActionState(boolean enabled, ActionContext context) {
        if (!enabled) {
            debug("Action " + getActionName(context) + " disable: all files are ADDED (not commited to CC yet)");
        } else {
            debug("Action " + getActionName(context) + " enable");
        }
    }

    protected boolean isEnabled(VirtualFile file, ActionContext context) throws VcsException {
        debug("project=" + context.project + ", file=" + file);
        return OpenApiFacade.getFileStatusManager(context.project).getStatus(file) != FileStatus.ADDED;
    }

}

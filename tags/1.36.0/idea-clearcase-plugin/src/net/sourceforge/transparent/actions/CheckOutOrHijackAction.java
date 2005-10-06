// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   CheckOutOrHijackAction.java

package net.sourceforge.transparent.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.intellij.openapi.OpenApiFacade;
import org.intellij.plugins.actions.NopAction;

// Referenced classes of package net.sourceforge.transparent.actions:
//            ActionContext

public class CheckOutOrHijackAction extends AnAction {

    public CheckOutOrHijackAction() {
    }

    public void update(AnActionEvent e) {
        getDelegatedAction(e).update(e);
    }

    private AnAction getDelegatedAction(AnActionEvent e) {
        ActionContext actionContext = new ActionContext(e);
        AnAction action;
        if (!actionContext.isVcsActive()) {
            action = NopAction.getInstance();
        } else
        if (actionContext.vcs.getTransparentConfig().offline) {
            action = OpenApiFacade.getActionManager().getAction("ClearCase.Hijack");
        } else {
            action = OpenApiFacade.getActionManager().getAction("ClearCase.CheckOut");
        }
        return action;
    }

    public void actionPerformed(AnActionEvent e) {
        getDelegatedAction(e).actionPerformed(e);
    }
}

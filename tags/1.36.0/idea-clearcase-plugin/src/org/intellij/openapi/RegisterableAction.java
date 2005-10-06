// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   RegisterableAction.java

package org.intellij.openapi;

import javax.swing.*;

import com.intellij.openapi.actionSystem.ActionManager;

// Referenced classes of package org.intellij.openapi:
//            BaseAction

public abstract class RegisterableAction extends BaseAction {

    public RegisterableAction() {
    }

    public RegisterableAction(String text) {
        super(text);
    }

    public RegisterableAction(String text, String description, Icon icon) {
        super(text, description, icon);
    }

    public final void register() {
        ActionManager actionManager = ActionManager.getInstance();
        String actionId = getActionRegistrationId();
        if (actionManager.getAction(actionId) == null) {
            actionManager.registerAction(actionId, this);
        }
    }

    public final void unregister() {
        ActionManager actionManager = ActionManager.getInstance();
        String actionId = getActionRegistrationId();
        if (actionManager.getAction(actionId) != null) {
            actionManager.unregisterAction(actionId);
        }
    }

    protected String getActionRegistrationId() {
        return getClass().getName();
    }
}

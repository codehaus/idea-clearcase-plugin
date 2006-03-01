// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   MergeProjectAction.java

package net.sourceforge.transparent.actions;

import java.util.List;

import net.sourceforge.transparent.Runner;

// Referenced classes of package net.sourceforge.transparent.actions:
//            VcsAction, ActionContext

public class MergeProjectAction extends VcsAction {

    public MergeProjectAction() {
    }

    protected List execute(ActionContext ct) {
        (new Runner()).run("clearmrgman");
        return null;
    }

    protected String getActionName(ActionContext context) {
        return "Merge Project";
    }
}

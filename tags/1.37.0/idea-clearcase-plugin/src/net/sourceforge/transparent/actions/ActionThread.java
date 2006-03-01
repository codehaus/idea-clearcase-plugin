// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   ActionThread.java

package net.sourceforge.transparent.actions;

import com.intellij.openapi.vfs.VirtualFile;
import net.sourceforge.transparent.ClearCaseFile;

// Referenced classes of package net.sourceforge.transparent.actions:
//            AsynchronousAction, ActionContext

class ActionThread extends Thread {

    private final VirtualFile virtualFile;
    private final ActionContext context;
    private final ClearCaseFile file;
    private AsynchronousAction asynchronousAction;

    public ActionThread(AsynchronousAction asynchronousAction, ClearCaseFile file, VirtualFile vFile, ActionContext context) {
        super(asynchronousAction.getActionName(context));
        this.asynchronousAction = asynchronousAction;
        virtualFile = vFile;
        this.context = context;
        this.file = file;
    }
}

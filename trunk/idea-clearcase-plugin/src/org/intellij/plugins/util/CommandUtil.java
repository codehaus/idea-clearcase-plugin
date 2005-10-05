// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   CommandUtil.java

package org.intellij.plugins.util;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;

public class CommandUtil {
    public static interface Command {

        public abstract Object run() throws Exception;
    }


    static final int READ = 0;
    static final int WRITE = 1;
    Object result;
    Exception exception;

    public CommandUtil() {
    }

    public Object executeWriteCommand(final Project project, final String name, final Command cmd) throws Exception {
        if (EventQueue.isDispatchThread()) {
            executeCommand(project, name, cmd, 1);
        } else {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    executeCommand(project, name, cmd, 1);
                }

            });
        }
        return getResult();
    }

    public Object executeWriteCommandWithoutException(Project project, String name, Command cmd) {
        try {
            return executeWriteCommand(project, name, cmd);
        } catch (Exception e) {
            throw new RuntimeException("Should not have thrown an exception", e);

        }
    }

    public Object executeReadCommand(Project project, String name, Command cmd) throws Exception {
        executeCommand(project, name, cmd, 0);
        return getResult();
    }

    public Object executeReadCommandWithoutException(Project project, String name, Command cmd) {
        try {
            return executeReadCommand(project, name, cmd);
        } catch (Exception e) {
            throw new RuntimeException("Should not have thrown an exception", e);
        }
    }

    public Object runWriteAction(final Command cmd) throws Exception {
        if (EventQueue.isDispatchThread()) {
            runAction(cmd, 1);
        } else {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    runAction(cmd, 1);
                }

            });
        }
        return getResult();
    }

    public void runWriteActionWithoutException(Command command) {
        try {
            runWriteAction(command);
        }
        catch (Exception e) {
            throw new RuntimeException("Should not have thrown an exception", e);
        }
    }

    public Object runReadAction(Command cmd) throws Exception {
        runAction(cmd, 0);
        return getResult();
    }

    public void runReadActionWithoutException(Command command) {
        try {
            runReadAction(command);
        }
        catch (Exception e) {
            throw new RuntimeException("Should not have thrown an exception", e);
        }
    }

    private Object getResult() throws Exception {
        if (exception != null) {
            throw exception;
        } else {
            return result;
        }
    }

    private void executeCommand(Project project, String name, final Command cmd, final int type) {
        Runnable runnable = new Runnable() {

            public void run() {
                runAction(cmd, type);
            }

        };
        CommandProcessor.getInstance().executeCommand(project, runnable, name, null);
    }

    private void runAction(Command cmd, int type) {
        Runnable runnable = getRunnable(cmd);
        if (type == 1) {
            ApplicationManager.getApplication().runWriteAction(runnable);
        } else {
            ApplicationManager.getApplication().runReadAction(runnable);
        }
    }

    private Runnable getRunnable(final Command cmd) {
        Runnable runnable = new Runnable() {

            public void run() {
                try {
                    result = cmd.run();
                }
                catch (Exception e) {
                    exception = e;
                }
            }

        };
        return runnable;
    }

    public void runOnMainThreadAsynchonously(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }

    public void runOnMainThreadSynchonously(Runnable runnable) {
        try {
            SwingUtilities.invokeAndWait(runnable);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Object runOnMainThreadSynchonously(Command command) throws Exception {
        runOnMainThreadSynchonously(getRunnable(command));
        return getResult();
    }


}

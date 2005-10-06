// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   OpenApiFacade.java

package org.intellij.openapi;

import java.lang.reflect.Field;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcsHelper;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiManager;
//import com.intellij.refactoring.RefactoringSettings;
import com.intellij.refactoring.listeners.RefactoringListenerManager;

public class OpenApiFacade {


    public OpenApiFacade() {
    }

    public static FileStatusManager getFileStatusManager(Project project) {
            return FileStatusManager.getInstance(project);
    }

    private static void checkProject(Project project) {
        if (project == null) {
            throw new NullPointerException("project null");
        } else {
            return;
        }
    }

    public static ActionManager getActionManager() {
        try {
            return ActionManager.getInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalFileSystem getLocalFileSystem() {
            return LocalFileSystem.getInstance();
    }

    public static PsiManager getPsiManager(Project project) {
            return PsiManager.getInstance(project);
    }

    public static VirtualFileManager getVirtualFileManager() {
            return VirtualFileManager.getInstance();
    }

    public static RefactoringListenerManager getRefactoringListenerManager(Project project) {
            return RefactoringListenerManager.getInstance(project);
    }

//    public static RefactoringSettings getRefactoringSettings() {
//            return RefactoringSettings.getInstance();
//    }

    public static FileEditorManager getFileEditorManager(Project project) {
        return FileEditorManager.getInstance(project);
    }

    public static PsiDocumentManager getPsiDocumentManager(Project project) {
            return PsiDocumentManager.getInstance(project);
    }

    public static AbstractVcsHelper getAbstractVcsHelper(Project project) {
            return AbstractVcsHelper.getInstance(project);
    }

    public static void reset() {
        Field fields[] = (org.intellij.openapi.OpenApiFacade.class).getFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            try {
                f.set(null, null);
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    public static FileDocumentManager getFileDocumentManager() {
        return FileDocumentManager.getInstance();
    }
}

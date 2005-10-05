// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   DataContextUtil.java

package org.intellij.openapi;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

public class DataContextUtil {

    public static final String PSI_ELEMENT = "psi.Element";
    public static final String PSI_FILE = "psi.File";

    public DataContextUtil() {
    }

    public static Project getProject(AnActionEvent event) {
        return (Project)event.getDataContext().getData("project");
    }

    public static Module getModule(AnActionEvent event) {
        return (Module) event.getDataContext().getData("module");
    }

    public static PsiElement getPsiElement(AnActionEvent event) {
        return (PsiElement)event.getDataContext().getData("psi.Element");
    }

    public static Editor getEditor(AnActionEvent event) {
        return (Editor)event.getDataContext().getData("editor");
    }

    public static PsiFile getPsiFile(AnActionEvent event) {
        return (PsiFile)event.getDataContext().getData("psi.File");
    }

    public static VirtualFile getVirtualFile(AnActionEvent event) {
        return (VirtualFile)event.getDataContext().getData("virtualFile");
    }

    public static VirtualFile[] getVirtualFileArray(AnActionEvent event) {
        VirtualFile virtualFiles[] = (VirtualFile[])event.getDataContext().getData("virtualFileArray");
        if (virtualFiles == null || virtualFiles.length == 0) {
            VirtualFile virtualFile = getVirtualFile(event);
            if (virtualFile != null) {
                virtualFiles = (new VirtualFile[] {
                    virtualFile
                });
            }
        }
        return virtualFiles;
    }
}

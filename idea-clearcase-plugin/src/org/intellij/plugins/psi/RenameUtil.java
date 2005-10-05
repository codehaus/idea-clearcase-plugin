// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   RenameUtil.java

package org.intellij.plugins.psi;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.util.IncorrectOperationException;

public class RenameUtil {

    public RenameUtil() {
    }

    public static void renameClass(PsiClass psiClass, String newName) throws IncorrectOperationException {
        psiClass.setName(newName);
    }

    public static void renameClassManually(PsiClass psiClass, String newName) throws IncorrectOperationException {
        PsiElementFactory factory = psiClass.getManager().getElementFactory();
        PsiIdentifier id = factory.createIdentifier(newName);
        String oldName = psiClass.getName();
        boolean renameFile = isRenameFileOnClassRenaming(psiClass);
        psiClass.getNameIdentifier().replace(id);
        if (renameFile) {
            PsiFile file = (PsiFile)psiClass.getParent();
            String fileName = file.getName();
            int dotIndex = fileName.lastIndexOf('.');
            file.setName(dotIndex >= 0 ? newName + "." + fileName.substring(dotIndex + 1) : newName);
        }
        PsiMethod methods[] = psiClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            PsiMethod method = methods[i];
            if (method.isConstructor() && method.getName().equals(oldName)) {
                method.getNameIdentifier().replace(id);
            }
        }

    }

    public static boolean isRenameFileOnClassRenaming(PsiClass psiClass) {
        if (psiClass.getParent() instanceof PsiFile) {
            PsiFile file = (PsiFile)psiClass.getParent();
            String fileName = file.getName();
            int dotIndex = fileName.lastIndexOf('.');
            String fileNameWithoutExtension = dotIndex >= 0 ? fileName.substring(0, dotIndex) : fileName;
            String className = psiClass.getName();
            return className.equals(fileNameWithoutExtension);
        } else {
            return false;
        }
    }
}

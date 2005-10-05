// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   Refactorer.java

package org.intellij.plugins.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.refactoring.MoveClassesOrPackagesRefactoring;
import com.intellij.refactoring.PackageWrapper;
import com.intellij.refactoring.RefactoringFactory;
//import com.intellij.refactoring.RefactoringSettings;
import com.intellij.refactoring.RenameRefactoring;
//import com.intellij.refactoring.move.moveClassesOrPackages.AutocreatingSingleSourceRootMoveDestination;
import org.intellij.openapi.OpenApiFacade;

// Referenced classes of package org.intellij.plugins.util:
//            PluginPsiUtil

public class Refactorer {

    private Project project;
    private PluginPsiUtil pluginPsiUtil;
    private static final Logger LOG = Logger.getInstance("org.intellij.plugins.util.Refactorer");

    public Refactorer(Project project) {
        pluginPsiUtil = new PluginPsiUtil();
        this.project = project;
    }

    public PluginPsiUtil getPluginPsiUtil() {
        return pluginPsiUtil;
    }

    public void setPluginPsiUtil(PluginPsiUtil pluginPsiUtil) {
        this.pluginPsiUtil = pluginPsiUtil;
    }

    public void rename(PsiNamedElement elementToBeRenamed, String newName) {
        debug("renaming " + elementToBeRenamed.getName() + " to " + newName);
        if (!isContainingFileWritable(elementToBeRenamed)) {
            return;
        } else {
            throw new UnsupportedOperationException("Operation removed due to API changes.");
//            RefactoringSettings settings = OpenApiFacade.getRefactoringSettings();
//            boolean searchInComments = settings.isToSearchInCommentsForRename(elementToBeRenamed);
//            boolean searchInNoneJavaFiles = settings.isToSearchInNonJavaFilesForRename(elementToBeRenamed);
//            boolean preview = settings.MOVE_PREVIEW_USAGES;
//            RenameRefactoring rename = RefactoringFactory.getInstance(project).createRename(elementToBeRenamed, newName);
//            rename.run();
//            return;
        }
    }

    public void moveClass(PsiClass classToBeMoved, PsiDirectory newParent) {
        debug("moving " + classToBeMoved.getQualifiedName() + " to " + newParent.getVirtualFile().getPath());
        if (!isContainingFileWritable(classToBeMoved)) {
            return;
        } else {
            throw new UnsupportedOperationException("Operation removed due to API changes.");
//            AutocreatingSingleSourceRootMoveDestination destination = new AutocreatingSingleSourceRootMoveDestination(new PackageWrapper(newParent.getPackage()), newParent.getVirtualFile());
//            MoveClassesOrPackagesRefactoring mover = RefactoringFactory.getInstance(project).createMoveClassesOrPackages(new PsiClass[] {
//                classToBeMoved
//            }, destination);
//            mover.run();
//            return;
        }
    }

    protected boolean isContainingFileWritable(PsiElement element) {
        return pluginPsiUtil.isFileWritable(element);
    }

    private void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

}

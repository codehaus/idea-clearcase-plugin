// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   PluginPsiUtil.java

package org.intellij.plugins.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiAnonymousClass;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.openapi.DataContextUtil;
import org.intellij.openapi.OpenApiFacade;

// Referenced classes of package org.intellij.plugins.util:
//            DocumentUtil, PsiClassManager

public class PluginPsiUtil {

    private static final Logger LOG = Logger.getInstance("org.intellij.plugins.util.PluginPsiUtil");
    private PsiClassManager psiClassManager;

    public PluginPsiUtil() {
    }

    public PluginPsiUtil(PsiClassManager psiClassManager) {
        this.psiClassManager = psiClassManager;
    }

    public PsiClass findTopLevelClassOfEnclosingFile(PsiElement element) {
        PsiClass psiClass = findTopLevelEnclosingClass(element);
        if (psiClass == null) {
            psiClassManager.findPrimaryClass(element.getContainingFile().getVirtualFile().getPath());
        }
        return psiClass;
    }

    public static void logPsiElementHierarchy(String context, PsiElement element) {
        if (element == null) {
            debug("element=null");
            return;
        } else {
            PsiFile containingFile = element.getContainingFile();
            debug(context + ": elt=" + element + (containingFile != null ? " containingFile=" + containingFile : ""));
            context.replaceAll(".", " ");
            logPsiElementHierarchy(context, element.getParent());
            return;
        }
    }

    public static PsiClass findTopLevelEnclosingClass(PsiElement element) {
        PsiClass psiClass;
        if (element instanceof PsiClass) {
            psiClass = (PsiClass)element;
        } else {
            psiClass = (PsiClass)PsiTreeUtil.getParentOfType(element, com.intellij.psi.PsiClass.class);
        }
        if (psiClass instanceof PsiAnonymousClass) {
            psiClass = findTopLevelEnclosingClass(psiClass.getParent());
        }
        return psiClass;
    }

    public static PsiMethod findTopLevelClassMethod(PsiElement element) {
        PsiMethod method = (element instanceof PsiMethod) ? (PsiMethod)element : (PsiMethod)PsiTreeUtil.getParentOfType(element, com.intellij.psi.PsiMethod.class);
        if (method != null && (method.getContainingClass() instanceof PsiAnonymousClass)) {
            return findTopLevelClassMethod(method.getParent());
        } else {
            return method;
        }
    }

    public static PsiElement getCurrentElement(AnActionEvent event) {
        Editor editor = DataContextUtil.getEditor(event);
        PsiFile psiFile = DataContextUtil.getPsiFile(event);
        if (psiFile != null && editor != null) {
            return getElementAtCaret(psiFile, editor);
        } else {
            return DataContextUtil.getPsiElement(event);
        }
    }

    public static PsiElement getElementAtCaret(Project project) {
        Editor selectedEditor = OpenApiFacade.getFileEditorManager(project).getSelectedTextEditor();
        PsiFile psiFile = getSelectedPsiFile(project);
        return getElementAtCaret(psiFile, selectedEditor);
    }

    public static PsiFile getSelectedPsiFile(Project project) {
        Document selectedDocument = getSelectedDocument(project);
        if (selectedDocument == null) {
            return null;
        } else {
            return (new DocumentUtil(project)).getPsiFile(selectedDocument);
        }
    }

    private static Document getSelectedDocument(Project project) {
        return OpenApiFacade.getFileEditorManager(project).getSelectedTextEditor().getDocument();
    }

    public static PsiElement getElementAtCaret(PsiFile psiFile, Editor editor) {
        return psiFile.findElementAt(editor.getCaretModel().getOffset());
    }

    public static String getFilePath(PsiClass c) {
        PsiFile classFile = (PsiFile)findTopLevelEnclosingClass(c).getParent();
        return classFile.getVirtualFile().getPath();
    }

    public static boolean isAncestor(PsiElement element, PsiElement ancestor) {
        return PsiTreeUtil.isAncestor(ancestor, element, false);
    }

    public boolean isFileWritable(PsiElement element) {
        VirtualFile file = element.getContainingFile().getVirtualFile();
        if (!file.isWritable()) {
            OpenApiFacade.getVirtualFileManager().fireReadOnlyModificationAttempt(new VirtualFile[] {
                file
            });
            if (!file.isWritable()) {
                Messages.showMessageDialog("Cannot modify a read-only file " + file.getPath(), "File Read-only", Messages.getErrorIcon());
            }
            return false;
        } else {
            return true;
        }
    }

    public static PsiDirectory getClassDirectory(PsiClass theClass) {
        return theClass.getContainingFile().getContainingDirectory();
    }

    private static void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    public static boolean isInheritor(PsiClass childClass, PsiClass parentClass) {
        PsiClass objectClass = childClass.getManager().findClass("java.lang.Object", childClass.getResolveScope());
        if (!childClass.equals(objectClass)) {
            if (childClass.getSuperClass() == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(new Exception("isInheritor(" + childClass.getName() + ") superclass is null"));
                }
                return false;
            }
            if (childClass.getSuperClass().equals(parentClass)) {
                return true;
            } else {
                childClass = childClass.getSuperClass();
                return isInheritor(childClass, parentClass);
            }
        } else {
            return false;
        }
    }

    public static String getTestFormattedMethodName(String methodName) {
        return "test" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
    }

}

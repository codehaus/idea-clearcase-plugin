// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   PsiClassManager.java

package org.intellij.plugins.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.util.IncorrectOperationException;
import org.apache.velocity.VelocityContext;
import org.intellij.openapi.OpenApiFacade;

// Referenced classes of package org.intellij.plugins.util:
//            Refactorer, PsiDirectoryUtil, FilePathUtil, TemplateEngine

public class PsiClassManager extends Refactorer {

    Project project;
    PsiDirectoryUtil psiDirectoryUtil;
    TemplateEngine templateEngine;
    PsiManager psiManager;
    public static final String CLASS_NAME = "NAME";
    public static final String PACKAGE_NAME = "PACKAGE_NAME";
    private static final Logger LOG = Logger.getInstance("org.intellij.plugins.util.PsiClassManager");

    public PsiClassManager(Project project, TemplateEngine templateEngine) {
        this(project, templateEngine, OpenApiFacade.getPsiManager(project), new PsiDirectoryUtil(ProjectRootManager.getInstance(project), OpenApiFacade.getPsiManager(project)));
    }

    protected PsiClassManager(Project project, TemplateEngine templateEngine, PsiManager psiManager, PsiDirectoryUtil psiDirectoryUtil) {
        super(project);
        this.project = project;
        this.templateEngine = templateEngine;
        this.psiManager = psiManager;
        this.psiDirectoryUtil = psiDirectoryUtil;
    }

    public PsiClass findPrimaryClass(String filePath) {
        debug("findPrimaryClass filePath=" + filePath);
        if (filePath == null) {
            return null;
        }
        VirtualFile vFile = OpenApiFacade.getLocalFileSystem().findFileByPath(filePath);
        if (vFile == null) {
            return null;
        } else {
            PsiFile psiFile = psiManager.findFile(vFile);
            return findPrimaryClass(psiFile);
        }
    }

    public PsiClass findPrimaryClass(PsiFile psiFile) {
        if (psiFile == null || !(psiFile instanceof PsiJavaFile)) {
            return null;
        }
        PsiJavaFile javaFile = (PsiJavaFile)psiFile;
        PsiClass classes[] = javaFile.getClasses();
        String filePrimaryClassName = getPrimaryClassNameFromJavaFileName(javaFile.getName());
        for (int i = 0; i < classes.length; i++) {
            PsiClass aClass = classes[i];
            if (aClass.getName().equals(filePrimaryClassName)) {
                return aClass;
            }
        }

        return null;
    }

    public String getPrimaryClassNameFromJavaFileName(String name) {
        return name.substring(0, name.length() - ".java".length());
    }

    public PsiClass createClassFromTemplate(String filePath, String templateName, VelocityContext context) throws Exception {
        PsiDirectory parentDir = findOrCreateParentDir(filePath);
        if (parentDir == null) {
            return null;
        } else {
            String fileName = FilePathUtil.extractNameFromPath(filePath);
            String primaryClassName = getPrimaryClassNameFromJavaFileName(fileName);
            String packagePath = parentDir.getPackage().getQualifiedName();
            context.put("NAME", primaryClassName);
            context.put("PACKAGE_NAME", packagePath);
            debug("executeTemplate(" + parentDir.getVirtualFile().getPath());
            String classResult = templateEngine.processTemplate(templateName, context);
            return createClass(parentDir, classResult, true);
        }
    }

    public PsiClass createClass(String parentPath, String classSource, boolean reformat) throws IncorrectOperationException {
        PsiDirectory parentDir = psiDirectoryUtil.findOrCreateDirectory(parentPath);
        return createClass(parentDir, classSource, reformat);
    }

    public PsiClass createClass(PsiDirectory parentDir, String classSource, boolean reformat) throws IncorrectOperationException {
        String JAVA_FILE_EXTENSION = "java";
        PsiJavaFile file = (PsiJavaFile)PsiManager.getInstance(project).getElementFactory().createFileFromText("myclass." + JAVA_FILE_EXTENSION, classSource);
        PsiClass classes[] = file.getClasses();
        if (classes == null || classes.length == 0) {
            throw new IncorrectOperationException("This source did not produce Java class nor interface!");
        }
        PsiClass psiClass = classes[0];
        if (reformat) {
            CodeStyleManager.getInstance(project).reformat(file);
        }
        String className = psiClass.getName();
        String fileName = className + "." + JAVA_FILE_EXTENSION;
        if (psiClass.isInterface()) {
            parentDir.checkCreateInterface(className);
        } else {
            parentDir.checkCreateClass(className);
        }
        file = (PsiJavaFile)file.setName(fileName);
        file = (PsiJavaFile)parentDir.add(file);
        return file.getClasses()[0];
    }

    public void commitDocument(Document newDocument) {
        PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
        if (documentManager.hasUncommitedDocuments()) {
            documentManager.commitDocument(newDocument);
        }
    }

    private PsiDirectory findOrCreateParentDir(String filePath) throws IncorrectOperationException {
        String packagePath = FilePathUtil.extractParentPathFromPath(filePath);
        PsiDirectory parentDir = psiDirectoryUtil.findOrCreateDirectory(packagePath);
        return parentDir;
    }

    private void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    public PsiManager getPsiManager() {
        return psiManager;
    }

}

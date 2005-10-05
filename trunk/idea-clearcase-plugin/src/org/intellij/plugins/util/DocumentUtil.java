// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   DocumentUtil.java

package org.intellij.plugins.util;

import java.util.ArrayList;
import java.util.List;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.intellij.openapi.OpenApiFacade;

// Referenced classes of package org.intellij.plugins.util:
//            CollectionsUtil

public class DocumentUtil {

    Project project;

    public DocumentUtil(Project project) {
        this.project = project;
    }

    Document getDocument(Editor editor) {
        return editor.getDocument();
    }

    public Document getDocument(VirtualFile file) {
        return FileDocumentManager.getInstance().getDocument(file);
    }

    public Document getDocument(PsiFile file) {
        return PsiDocumentManager.getInstance(project).getDocument(file);
    }

    public PsiFile getPsiFile(Document document) {
        return PsiDocumentManager.getInstance(project).getPsiFile(document);
    }

    public VirtualFile getVirtualFile(Document document) {
        return FileDocumentManager.getInstance().getFile(document);
    }

    public Editor[] getEditors(Document document) {
        Editor projectEditors[] = getProjectEditors();
        Editor documentEditors[] = EditorFactory.getInstance().getEditors(document);
        List editors = CollectionsUtil.intersection(documentEditors, projectEditors);
        return (Editor[])editors.toArray(new Editor[editors.size()]);
    }

    private Editor[] getProjectEditors() {
        com.intellij.openapi.fileEditor.FileEditor allEditors[] = OpenApiFacade.getFileEditorManager(project).getAllEditors();
        ArrayList editors = new ArrayList();
        for (int i = 0; i < allEditors.length; i++) {
            com.intellij.openapi.fileEditor.FileEditor editor = allEditors[i];
            if (editor instanceof TextEditor) {
                editors.add(((TextEditor)editor).getEditor());
            }
        }

        return (Editor[])editors.toArray();
    }
}

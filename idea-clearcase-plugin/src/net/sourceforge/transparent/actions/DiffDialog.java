// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   DiffDialog.java

package net.sourceforge.transparent.actions;

import com.intellij.openapi.diff.DiffManager;
import com.intellij.openapi.diff.DiffPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class DiffDialog extends DialogWrapper {

    private Project myProject;
    private DiffPanel myDiffPanel;
    protected JTextArea myCommentArea;

    public DiffDialog(Project project, VirtualFile file) {
        super(project, true);
        myCommentArea = new JTextArea();
        try {
            Messages.showMessageDialog(project, "Inside File : " + file.getName() + " -- Project : " + new String(file.contentsToCharArray()), "Inside", Messages.getErrorIcon());
            Messages.showMessageDialog(project, "Inside File : " + file.getName() + " -- Project : " + new String(VfsUtil.getCommonAncestor(file, file).contentsToCharArray()), "Inside", Messages.getErrorIcon());
            VirtualFile childrenFiles[] = file.getChildren();
            if (childrenFiles == null) {
                Messages.showMessageDialog(project, "Children Files are null", "Inside", Messages.getErrorIcon());
            } else {
                for (int i = 0; i < childrenFiles.length; i++) {
                    Messages.showMessageDialog(project, "Child : " + childrenFiles[i].getName(), "Inside", Messages.getErrorIcon());
                }

            }
            if (file.getParent() == null) {
                Messages.showMessageDialog(project, "Parent is null", "Inside", Messages.getErrorIcon());
            } else {
                Messages.showMessageDialog(project, "Inside Parent File : " + file.getParent().getName(), "Inside", Messages.getErrorIcon());
            }
            myProject = project;
            setTitle("Differences");
            myDiffPanel = DiffManager.getInstance().createDiffPanel(WindowManager.getInstance().suggestParentWindow(project), project);
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
        init();
    }

    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(myDiffPanel.getComponent(), "Center");
        return panel;
    }

    protected void dispose() {
        super.dispose();
    }

    public JComponent getPreferredFocusedComponent() {
        return myCommentArea;
    }

    public DiffPanel getDiffPanel() {
        return myDiffPanel;
    }

    public String getComment() {
        return myCommentArea.getText();
    }

    protected String getDimensionServiceKey() {
        return "ClearCase.CheckinProject";
    }

    public JPanel createCommentPanel() {
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.add(new JLabel("Comment"), "North");
        return panel1;
    }
}

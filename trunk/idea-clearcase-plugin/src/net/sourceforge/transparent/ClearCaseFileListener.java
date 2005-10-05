// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   ClearCaseFileListener.java

package net.sourceforge.transparent;

import javax.swing.*;

import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileMoveEvent;
import com.intellij.openapi.vfs.VirtualFilePropertyEvent;
import org.intellij.plugins.ExcludedFileFilter;
import org.intellij.plugins.util.FileFilter;

// Referenced classes of package net.sourceforge.transparent:
//            CommandLineClearCase, ClearCaseFile, ClearCase

public class ClearCaseFileListener extends VirtualFileAdapter {

    private ClearCase clearcase;
    private FileFilter filter;

    public ClearCaseFileListener() {
        clearcase = new CommandLineClearCase();
        filter = new ExcludedFileFilter();
    }

    public FileFilter getFilter() {
        return filter;
    }

    public void fileCreated(VirtualFileEvent event) {
        if (event.getRequestor() == null || !filter.accept(event.getParent())) {
            return;
        }
        try {
            ClearCaseFile file = new ClearCaseFile(event.getFile(), clearcase);
            if (!file.isElement() && file.getParent().isElement() && JOptionPane.showConfirmDialog(null, "add " + file.getName() + " to source code control?") == 0) {
                file.getParent().checkOut(false, false);
                file.add("", false);
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void fileDeleted(VirtualFileEvent event) {
        if (event.getRequestor() == null || !filter.accept(event.getParent())) {
            return;
        }
        try {
            ClearCaseFile file = new ClearCaseFile(event.getParent(), event.getFile().getName(), clearcase);
            if (file.isElement()) {
                file.undoCheckOut();
                file.getParent().checkOut(false, false);
                file.delete("Deleted " + file.getFile().getName(), false);
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void propertyChanged(VirtualFilePropertyEvent event) {
        if (!filter.accept(event.getFile()) || !event.getPropertyName().equals("name")) {
            return;
        } else {
            ClearCaseFile oldFile = new ClearCaseFile(event.getParent(), (String)event.getOldValue(), clearcase);
            ClearCaseFile newFile = new ClearCaseFile(event.getParent(), (String)event.getNewValue(), clearcase);
            move(oldFile, newFile);
            return;
        }
    }

    public void fileMoved(VirtualFileMoveEvent event) {
        if (!filter.accept(event.getFile())) {
            return;
        } else {
            ClearCaseFile oldFile = new ClearCaseFile(event.getOldParent(), event.getFile().getName(), clearcase);
            ClearCaseFile newFile = new ClearCaseFile(event.getNewParent(), event.getFile().getName(), clearcase);
            move(oldFile, newFile);
            return;
        }
    }

    private void move(ClearCaseFile oldFile, ClearCaseFile newFile) {
        try {
            if (oldFile.isElement()) {
                moveFileBack(newFile, oldFile);
                oldFile.checkIn("");
                oldFile.move(newFile, "", false);
                newFile.checkOut(false, false);
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void moveFileBack(ClearCaseFile newFile, ClearCaseFile oldFile) {
        newFile.getFile().renameTo(oldFile.getFile());
    }
}

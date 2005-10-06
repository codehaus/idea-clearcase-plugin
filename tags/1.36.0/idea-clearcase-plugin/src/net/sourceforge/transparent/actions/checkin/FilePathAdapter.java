/**
 * Copyright (c) 2004 Tripos, Inc. All rights reserved.
 *
 * User: Vincent Mallet (vmallet@gmail.com)
 * Date: Dec 8, 2004 at 2:47:03 PM
 */
package net.sourceforge.transparent.actions.checkin;

import java.io.File;
import java.nio.charset.Charset;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vfs.VirtualFile;

public class FilePathAdapter implements FilePath {
    private VirtualFile virtualFile;

    public FilePathAdapter(VirtualFile file) {
        if (file == null) {
            throw new NullPointerException();
        }
        virtualFile = file;
    }

    public VirtualFile getVirtualFile() {
        return virtualFile;
    }

    public VirtualFile getVirtualFileParent() {
        return virtualFile.getParent();
    }

    public File getIOFile() {
        return new File(virtualFile.getPath());
    }

    public String getName() {
        return virtualFile.getName();
    }

    public String getPresentableUrl() {
        return virtualFile.getPresentableUrl();
    }

    public Document getDocument() {
        throw new UnsupportedOperationException("No adaptation");
    }

    public Charset getCharset() {
        return virtualFile.getCharset();
    }

    public FileType getFileType() {
        return virtualFile.getFileType();
    }

    public void refresh() {
        throw new UnsupportedOperationException();
    }

    public String getPath() {
        return virtualFile.getPath();
    }

    public boolean isDirectory() {
        return virtualFile.isDirectory();
    }

    public static FilePath[] adapt(VirtualFile[] files) {
        FilePath[] paths = new FilePath[files.length];
        for (int i = 0; i < files.length; i++) {
            paths[i] = new FilePathAdapter(files[i]);
        }

        return paths;
    }
}
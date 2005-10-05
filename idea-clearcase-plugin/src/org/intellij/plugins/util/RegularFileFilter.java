// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   RegularFileFilter.java

package org.intellij.plugins.util;

import java.io.File;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

// Referenced classes of package org.intellij.plugins.util:
//            FileUtil, FileFilter

public class RegularFileFilter
    implements FileFilter {

    private Project project;
    private FileUtil fileUtil;

    public RegularFileFilter() {
        fileUtil = new FileUtil();
    }

    public boolean accept(String path) {
        return accept(new File(path));
    }

    public boolean accept(File file) {
        return accept(fileUtil.ioFileToVirtualFile(file));
    }

    public boolean accept(VirtualFile file) {
        return file.getFileSystem().getProtocol().equals("file");
    }
}

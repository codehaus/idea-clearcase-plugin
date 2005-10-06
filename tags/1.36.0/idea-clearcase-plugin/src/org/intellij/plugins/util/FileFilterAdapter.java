// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   FileFilterAdapter.java

package org.intellij.plugins.util;

import java.io.File;

import com.intellij.openapi.vfs.VirtualFile;

// Referenced classes of package org.intellij.plugins.util:
//            FileFilter

public abstract class FileFilterAdapter
    implements FileFilter {

    public FileFilterAdapter() {
    }

    public boolean accept(String file) {
        return accept(new File(file));
    }

    public boolean accept(VirtualFile file) {
        return accept(new File(file.getPresentableUrl()));
    }
}

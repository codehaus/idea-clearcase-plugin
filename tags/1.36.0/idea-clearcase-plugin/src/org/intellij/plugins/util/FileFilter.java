// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   FileFilter.java

package org.intellij.plugins.util;

import java.io.File;

import com.intellij.openapi.vfs.VirtualFile;

public interface FileFilter
    extends java.io.FileFilter {

    public abstract boolean accept(String s);

    public abstract boolean accept(VirtualFile virtualfile);

    public abstract boolean accept(File file);
}

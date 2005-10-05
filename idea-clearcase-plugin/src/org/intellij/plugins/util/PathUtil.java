// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   PathUtil.java

package org.intellij.plugins.util;


public class PathUtil {

    private char separator;

    public PathUtil(char separator) {
        this.separator = separator;
    }

    public String getNameFromPath(String path) {
        if (path == null || path.equals("")) {
            return "";
        }
        int lastSlashIndex = path.lastIndexOf(separator);
        if (lastSlashIndex == path.length() - 1) {
            return getNameFromPath(path.substring(0, path.length() - 1));
        } else {
            return path.substring(lastSlashIndex + 1);
        }
    }

    public String getParentPathFromPath(String path) {
        if (path == null || path.equals("")) {
            return "";
        }
        int lastSlashIndex = path.lastIndexOf(separator);
        if (lastSlashIndex == -1) {
            return "";
        }
        if (lastSlashIndex == path.length() - 1) {
            return getParentPathFromPath(path.substring(0, path.length() - 1));
        } else {
            return path.substring(0, lastSlashIndex);
        }
    }

    public String getFirstElementOfPath(String path) {
        if (path == null || path.equals("")) {
            return "";
        } else {
            int slashIndex = path.indexOf(separator);
            String name = slashIndex <= 0 ? path : path.substring(0, slashIndex);
            return name;
        }
    }

    public String getSubPathPastFirstElement(String path) {
        if (path == null || path.equals("")) {
            return "";
        } else {
            int slashIndex = path.indexOf(separator);
            String subPackageName = slashIndex <= 0 ? "" : path.substring(path.indexOf(separator) + 1);
            return subPackageName;
        }
    }
}

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   FilePathUtil.java

package org.intellij.plugins.util;


// Referenced classes of package org.intellij.plugins.util:
//            PathUtil

public class FilePathUtil {

    static PathUtil util = new PathUtil('/');

    public FilePathUtil() {
    }

    public static String extractNameFromPath(String path) {
        return util.getNameFromPath(path);
    }

    public static String extractParentPathFromPath(String path) {
        return util.getParentPathFromPath(path);
    }

    public static String extractFirstPathElement(String path) {
        return util.getFirstElementOfPath(path);
    }

    public static String extractSubPathPastFirstElement(String path) {
        return util.getSubPathPastFirstElement(path);
    }

    public static String removeExtension(String path) {
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex != -1) {
            int slashIdex = path.lastIndexOf('/');
            if (dotIndex > slashIdex) {
                return path.substring(0, dotIndex);
            }
        }
        return path;
    }

    public static String replaceFileSeparator(String filePath) {
        return filePath.replace('/', '.');
    }

}

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   PsiDirectoryUtil.java

package org.intellij.plugins.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.util.IncorrectOperationException;

// Referenced classes of package org.intellij.plugins.util:
//            LogUtil

public class PsiDirectoryUtil {

    private static final Logger LOG;
    private ProjectRootManager rootManager;
    private PsiManager manager;
    static final boolean $assertionsDisabled; /* synthetic field */

    public PsiDirectoryUtil(ProjectRootManager projectRootManager, PsiManager manager) {
        rootManager = projectRootManager;
        this.manager = manager;
    }

    public PsiDirectory findOrCreateDirectory(PsiDirectory dir, String path) throws IncorrectOperationException {
        for (StringTokenizer tokens = new StringTokenizer(path, "/\\."); tokens.hasMoreTokens();) {
            String name = tokens.nextToken();
            dir = findOrCreateSubDirectory(dir, name);
        }

        return dir;
    }

    private PsiDirectory findOrCreateSubDirectory(PsiDirectory dir, String name) throws IncorrectOperationException {
        if (!$assertionsDisabled && dir == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && name == null) {
            throw new AssertionError();
        }
        PsiDirectory subDir = null;
        PsiDirectory subdirectories[] = dir.getSubdirectories();
        int i = 0;
        do {
            if (i >= subdirectories.length) {
                break;
            }
            if (subdirectories[i].getName().equals(name)) {
                subDir = subdirectories[i];
                debug("found " + subDir);
                break;
            }
            i++;
        } while (true);
        if (subDir == null) {
            debug("creating " + name);
            subDir = dir.createSubdirectory(name);
        }
        return subDir;
    }

    private static void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    public PsiDirectory findOrCreateDirectory(String path) throws IncorrectOperationException {
        debug("findOrCreateDirectory(" + path + ")");
        PsiDirectory rootsThatContainsPath[] = findRootsThatContainsPath(path);
        if (rootsThatContainsPath == null || rootsThatContainsPath.length == 0) {
            throw new IllegalArgumentException(path + " is not within the project source roots");
        }
        sortDirsLongestNameFirst(rootsThatContainsPath);
        PsiDirectory root = rootsThatContainsPath[0];
        String rootPath = root.getVirtualFile().getPath();
        if (path.length() == rootPath.length()) {
            return root;
        } else {
            String remainerPath = path.substring(rootPath.length() + 1);
            return findOrCreateDirectory(root, remainerPath);
        }
    }

    private PsiDirectory[] findRootsThatContainsPath(String path) {
        String pathWithTrailingSlash = path.charAt(path.length() - 1) != '/' ? path + '/' : path;
        ArrayList matchingRoots = new ArrayList();
        VirtualFile rootDirectories[] = rootManager.getContentSourceRoots();
        for (int i = 0; i < rootDirectories.length; i++) {
            VirtualFile rootDirectory = rootDirectories[i];
            String rootPath = rootDirectory.getPath();
            if (pathWithTrailingSlash.startsWith(rootPath + "/")) {
                matchingRoots.add(manager.findDirectory(rootDirectory));
            }
        }

        return (PsiDirectory[])matchingRoots.toArray(new PsiDirectory[0]);
    }

    private void sortDirsLongestNameFirst(PsiDirectory dirs[]) {
        Arrays.sort(dirs, new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((PsiDirectory)o2).getVirtualFile().getPath().length() - ((PsiDirectory)o1).getVirtualFile().getPath().length();
            }

        });
    }

    static Class _mthclass$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException e) {
            throw (new NoClassDefFoundError()).initCause(e);
        }
    }

    static  {
        $assertionsDisabled = !(org.intellij.plugins.util.PsiDirectoryUtil.class).desiredAssertionStatus();
        LOG = LogUtil.getLogger(org.intellij.plugins.util.PsiDirectoryUtil.class);
    }
}

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   ProjectSourceFileFilter.java

package net.sourceforge.transparent;

import java.io.File;

import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.plugins.util.FileFilter;
import org.intellij.plugins.util.FileUtil;

public class ProjectSourceFileFilter
    implements FileFilter {

    private Project project;
    private FileUtil fileUtil;

    public ProjectSourceFileFilter(Project project) {
        this.project = project;
        fileUtil = new FileUtil();
    }

    public boolean accept(String path) {
        return accept(new File(path));
    }

    public boolean accept(File file) {
        return accept(fileUtil.ioFileToVirtualFile(file));
    }

    public boolean accept(VirtualFile file) {
        ModuleManager moduleManager = ModuleManager.getInstance(project);
        com.intellij.openapi.module.Module modules[] = moduleManager.getModules();
        for (int i = 0; i < modules.length; i++) {
            com.intellij.openapi.module.Module module = modules[i];
            ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
            if (!moduleRootManager.getFileIndex().isInSourceContent(file));
        }

        return false;
    }
}

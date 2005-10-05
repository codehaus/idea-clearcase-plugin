// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   ExcludedPathsFromVcsConfiguration.java

package org.intellij.plugins;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.plugins.ui.pathlisteditor.PathListElement;
import org.jdom.Element;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package org.intellij.plugins:
//            ListenerNotifier

public class ExcludedPathsFromVcsConfiguration
    implements ListenerNotifier, JDOMExternalizable, ProjectComponent {

    public static final String EXCLUDE_FROM_VCS = "excludeFromVCS";
    public static final String FILE = "file";
    public static final String DIRECTORY = "directory";
    public static final String URL = "url";
    public static final String INCLUDE_SUBDIRECTORIES = "includeSubdirectories";
    private PropertyChangeSupport listenerSupport;
    public static final int DEPENDENCY_FORMAT_VERSION = 18;
    private List<PathListElement> excludedPathsFromVcs;
    private Project project;

    public ExcludedPathsFromVcsConfiguration(Project project) {
        listenerSupport = new PropertyChangeSupport(this);
        excludedPathsFromVcs = new ArrayList<PathListElement>();
        this.project = project;
    }

    public void disposeComponent() {
    }

    public void initComponent() {
    }

    public void projectClosed() {
    }

    public void projectOpened() {
    }

    public List<PathListElement> getExcludedPaths() {
        return excludedPathsFromVcs;
    }

    public void addExcludedPath(PathListElement element) {
        excludedPathsFromVcs.add(element);
    }

    public void resetExcludedPaths(List<PathListElement> excludedPaths) {
        excludedPathsFromVcs = new ArrayList<PathListElement>(excludedPaths);
        notifyListenersOfChange();
    }

    public void readExternal(Element element) throws InvalidDataException {
        DefaultJDOMExternalizer.readExternal(this, element);
        Element element1 = element.getChild("excludeFromVCS");
        if (element1 != null) {
            Iterator iterator = element1.getChildren().iterator();
            do {
                if (!iterator.hasNext()) {
                    break;
                }
                Element element2 = (Element)iterator.next();
                String s1 = element2.getAttributeValue("url");
                if (s1 != null) {
                    if ("file".equals(element2.getName())) {
                        PathListElement cf1 = new PathListElement(s1, false, true);
                        excludedPathsFromVcs.add(cf1);
                    }
                    if ("directory".equals(element2.getName())) {
                        boolean flag = true;
                        if ("false".equals(element2.getAttributeValue("includeSubdirectories"))) {
                            flag = false;
                        }
                        PathListElement cf2 = new PathListElement(s1, flag, false);
                        excludedPathsFromVcs.add(cf2);
                    }
                }
            } while (true);
        }
    }

    public void writeExternal(Element element) throws WriteExternalException {
        DefaultJDOMExternalizer.writeExternal(this, element);
        if (excludedPathsFromVcs.size() > 0) {
            Element element1 = new Element("excludeFromVCS");
            for (PathListElement excludedElement : excludedPathsFromVcs) {
                if (excludedElement.isFile()) {
                    Element element4 = new Element("file");
                    element4.setAttribute("url", excludedElement.getUrl());
                    element1.addContent(element4);
                } else {
                    Element element5 = new Element("directory");
                    element5.setAttribute("url", excludedElement.getUrl());
                    element5.setAttribute("includeSubdirectories", excludedElement.isIncludeSubDirectories() ? "true" : "false");
                    element1.addContent(element5);
                }
            }

            element.addContent(element1);
        }
    }

    public static ExcludedPathsFromVcsConfiguration getInstance(Project project) {
        ExcludedPathsFromVcsConfiguration config =
                project.getComponent(ExcludedPathsFromVcsConfiguration.class);

        if (config == null) {
            throw new RuntimeException(
                    ExcludedPathsFromVcsConfiguration.class.getName()
                            + " not configured as a component");
        }

        return config;
    }

    public boolean reject(VirtualFile file, boolean flag) {
        for (PathListElement excludedPath : excludedPathsFromVcs) {
           VirtualFile excludedFile = excludedPath.getVirtualFile();

            if (excludedFile != null) {
                if (excludedPath.isFile()) {
                    if (excludedFile.equals(file)) {
                        return true;
                    }
                } else if (excludedPath.isIncludeSubDirectories()) {
                    if (VfsUtil.isAncestor(excludedFile, file, false)) {
                        return true;
                    }
                } else if (!file.isDirectory() && excludedFile.equals(file.getParent())) {
                    return true;
                }
            }
        }

        if (flag) {
            List<VirtualFile> excludedRoots = getAllExcludedRoots();
            for (VirtualFile excludedRoot : excludedRoots) {
                if (VfsUtil.isAncestor(excludedRoot, file, false))
                    return true;
            }
        }
        return false;
    }

    private List<VirtualFile> getAllExcludedRoots() {
        ModuleManager moduleManager = ModuleManager.getInstance(project);
        Module[] modules = moduleManager.getSortedModules();

        List<VirtualFile> excludedRoots = new ArrayList<VirtualFile>();

        for (Module module : modules) {
            ModuleRootManager mgr2 = ModuleRootManager.getInstance(module);
            VirtualFile[] moduleExcludedRoots = mgr2.getExcludeRoots();
            excludedRoots.addAll(Arrays.asList(moduleExcludedRoots));
        }

        return excludedRoots;
    }

    public String getComponentName() {
        return getClass().getName();
    }

    static Project a(ExcludedPathsFromVcsConfiguration v1) {
        return v1.project;
    }

    public PropertyChangeListener[] getListeners() {
        return listenerSupport.getPropertyChangeListeners();
    }

    public void addListener(PropertyChangeListener listener) {
        listenerSupport.addPropertyChangeListener(listener);
    }

    public void notifyListenersOfChange() {
        listenerSupport.firePropertyChange("configuration", null, this);
    }

    public void removeListener(PropertyChangeListener listener) {
        listenerSupport.removePropertyChangeListener(listener);
    }
}

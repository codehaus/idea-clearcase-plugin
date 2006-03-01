// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   Config.java

package org.intellij.plugins.config;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import org.intellij.plugins.util.LogUtil;
import org.jdom.Element;

public abstract class Config
    implements JDOMExternalizable, ProjectComponent {

    private static final Logger LOG = LogUtil.getLogger();
    protected Project project;
    private StartupManager startupManager;

    public Config(Project project) {
        this.project = project;
    }

    public void projectClosed() {
    }

    public void projectOpened() {
        debug(LogUtil.getCallerMethod() + " for " + (project != null ? "" + project.getProjectFile() : "null"));
        initOnProjectOpened();
        getStartupManager().registerPostStartupActivity(new Runnable() {

            public void run() {
                initConfig();
            }

        });
    }

    private StartupManager getStartupManager() {
        if (startupManager == null) {
            startupManager = StartupManager.getInstance(project);
        }
        return startupManager;
    }

    public void setStartupManager(StartupManager startupManager) {
        this.startupManager = startupManager;
    }

    private void initConfig() {
        try {
            configChanged();
        }
        catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void configChanged() throws ConfigurationException {
        if (isDefaultProject()) {
            return;
        } else {
            updateInternalState();
            return;
        }
    }

    protected abstract void initOnProjectOpened();

    protected abstract void updateInternalState() throws ConfigurationException;

    public void disposeComponent() {
    }

    public void initComponent() {
        debug(LogUtil.getCallerMethod() + " for " + (project != null ? "" + project.getProjectFile() : "null"));
    }

    public void readExternal(Element element) throws InvalidDataException {
        DefaultJDOMExternalizer.readExternal(this, element);
    }

    public void writeExternal(Element element) throws WriteExternalException {
        DefaultJDOMExternalizer.writeExternal(this, element);
    }

    protected void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    public boolean isDefaultProject() {
        return project == null || project.getProjectFile() == null;
    }

}

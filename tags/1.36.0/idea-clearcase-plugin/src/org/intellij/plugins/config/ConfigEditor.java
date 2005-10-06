// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   ConfigEditor.java

package org.intellij.plugins.config;

import java.awt.*;

import javax.swing.*;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.intellij.plugins.util.BorderUtil;
import org.intellij.plugins.util.LogUtil;

// Referenced classes of package org.intellij.plugins.config:
//            Config

public abstract class ConfigEditor
    implements ProjectComponent, Configurable {

    private static final Logger LOG = Logger.getInstance("org.intellij.plugins.config.ConfigEditor");
    protected Project project;
    private Class configurationClass;
    protected JComponent component;
    private Config configuration;

    public ConfigEditor(Project project, Class configurationClass) {
        this.project = project;
        this.configurationClass = configurationClass;
    }

    public void disposeUIResources() {
        component = null;
    }

    public JComponent createComponent() {
        logMethod();
        if (component == null) {
            component = createEditorPanel();
        }
        return component;
    }

    public abstract JComponent createEditorPanel();

    public JLabel createHtmlLabel(String title, String htmlHelp) {
        JLabel help = new JLabel("<html>" + htmlHelp + "</html>");
        Font font = help.getFont();
        help.setFont(font.deriveFont(0));
        help.setBorder(BorderUtil.newTitledBorder(title));
        return help;
    }

    public String getHelpTopic() {
        return null;
    }

    public Icon getIcon() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public void projectOpened() {
        logMethod();
    }

    private void logMethod() {
        debug(LogUtil.getCallerMethod() + " for project " + getProjectName());
    }

    public void projectClosed() {
        logMethod();
    }

    public void initComponent() {
        logMethod();
    }

    public void reset() {
        copyFromConfig();
    }

    public void apply() throws ConfigurationException {
        copyToConfig();
        getConfiguration().configChanged();
    }

    protected abstract void copyFromConfig();

    protected abstract void copyToConfig();

    protected String getProjectName() {
        String projectName;
        if (isDefaultProject()) {
            projectName = "<default>";
        } else {
            projectName = project.getProjectFile().getName();
        }
        return projectName;
    }

    private boolean isDefaultProject() {
        return project == null || project.getProjectFile() == null;
    }

    public void disposeComponent() {
    }

    public void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    public Config getConfiguration() {
        if (configuration == null) {
            configuration = (Config)project.getComponent(configurationClass);
        }
        return configuration;
    }

}

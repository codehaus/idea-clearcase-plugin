// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   MainConfigEditor.java

package org.intellij.plugins.config;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.intellij.openapi.project.Project;
import org.intellij.plugins.util.PathUtil;

// Referenced classes of package org.intellij.plugins.config:
//            ConfigEditor, PluginVersion

public abstract class MainConfigEditor extends ConfigEditor {

    private Class versionClass;

    public MainConfigEditor(Project project, Class configurationClass, Class versionClass) {
        super(project, configurationClass);
        this.versionClass = versionClass;
    }

    public JComponent createEditorPanel() {
        component = new JPanel(new BorderLayout());
        component.add(createMainEditorPanel(), "North");
        JPanel versionPanel = createVersionPanel();
        component.add(versionPanel, "South");
        return component;
    }

    protected abstract JComponent createMainEditorPanel();

    private JPanel createVersionPanel() {
        JPanel versionPanel = new JPanel(new BorderLayout());
        versionPanel.add(new JLabel("Version " + getPluginVersion()), "East");
        versionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        return versionPanel;
    }

    private String getPluginVersion() {
        Package thisPackage = Package.getPackage((new PathUtil('.')).getParentPathFromPath(getClass().getName()));
        if (thisPackage == null || thisPackage.getSpecificationVersion() == null || thisPackage.getSpecificationVersion().equals("")) {
            return getVersion();
        } else {
            return thisPackage.getSpecificationVersion();
        }
    }

    private String getVersion() {
        try {
            return ((PluginVersion)versionClass.newInstance()).getVersion();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return "Unknown";
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }
}

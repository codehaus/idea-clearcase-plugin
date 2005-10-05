// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   PathManager.java

package org.intellij.openapi.application;


public class PathManager {

    private String homePath;
    private String libPath;
    private String systemPath;
    private String configPath;
    private String helpPath;
    private String binPath;
    private String optionsPath;
    private String pluginsPath;

    public PathManager() {
        this(com.intellij.openapi.application.PathManager.getHomePath(), com.intellij.openapi.application.PathManager.getLibPath(), com.intellij.openapi.application.PathManager.getSystemPath(), com.intellij.openapi.application.PathManager.getConfigPath(), com.intellij.openapi.application.PathManager.getHelpURL(), com.intellij.openapi.application.PathManager.getBinPath(), com.intellij.openapi.application.PathManager.getOptionsPath(), com.intellij.openapi.application.PathManager.getPluginsPath());
    }

    private PathManager(String homePath, String libPath, String systemPath, String configPath, String helpPath, String binPath, String optionsPath, 
            String pluginsPath) {
        this.homePath = homePath;
        this.libPath = libPath;
        this.systemPath = systemPath;
        this.configPath = configPath;
        this.helpPath = helpPath;
        this.binPath = binPath;
        this.optionsPath = optionsPath;
        this.pluginsPath = pluginsPath;
    }

    public static PathManager newInstanceForTest(String home) {
        return new PathManager(home, home + "/lib", home + "/system", home + "/config", home + "/help/idea", home + "/bin", home + "/config/options", home + "/plugins");
    }

    public String getHomePath() {
        return homePath;
    }

    public String getLibPath() {
        return libPath;
    }

    public String getSystemPath() {
        return systemPath;
    }

    public String getConfigPath() {
        return configPath;
    }

    public String getHelpPath() {
        return helpPath;
    }

    public String getBinPath() {
        return binPath;
    }

    public String getOptionsPath() {
        return optionsPath;
    }

    public String getPluginsPath() {
        return pluginsPath;
    }
}

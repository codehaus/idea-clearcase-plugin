// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   TemplateEngine.java

package org.intellij.plugins.util;

import java.io.File;
import java.io.StringWriter;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogSystem;

// Referenced classes of package org.intellij.plugins.util:
//            LogUtil

public class TemplateEngine {
    public static final class VelocityLogWrapper
        implements LogSystem {

        public void init(RuntimeServices runtimeservices) throws Exception {
        }

        public void logVelocityMessage(int i, String string) {
            TemplateEngine.debug(string);
        }

        public VelocityLogWrapper() {
        }
    }


    private boolean inited;
    private VelocityEngine velocity;
    private VelocityContext context;
    private static final Logger LOG;
    public static final String IDEA_FILE_TEMPLATE_DIR = "fileTemplates";
    public static final String IDEA_TEMPLATE_INCLUDES_DIR = "includes";

    public TemplateEngine() {
        inited = false;
        velocity = new VelocityEngine();
    }

    private void initVelocityProcess() {
        if (inited) {
            return;
        }
        try {
            ExtendedProperties extendedProperties = new ExtendedProperties();
            extendedProperties.addProperty("resource.loader", "file,class");
            extendedProperties.addProperty("file.resource.loader.class", (org.apache.velocity.runtime.resource.loader.FileResourceLoader.class).getName());
            extendedProperties.addProperty("input.encoding", "UTF-8");
            extendedProperties.addProperty("file.resource.loader.path", getFileTemplatePath());
            extendedProperties.addProperty("file.resource.loader.cache", "true");
            extendedProperties.addProperty("file.resource.loader.modificationCheckInterval", "2");
            extendedProperties.addProperty("class.resource.loader.class", (org.intellij.plugins.util.TemplatesClasspathLoader.class).getName());
//            extendedProperties.addProperty("runtime.log.logsystem.class", (org.intellij.plugins.util.TemplateEngine$VelocityLogWrapper.class).getName());
            velocity.setExtendedProperties(extendedProperties);
            velocity.init();
            inited = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getFileTemplatesDir() {
        return new File(PathManager.getConfigPath(), "fileTemplates");
    }

    private String getFileTemplatePath() {
        File templatesDir = getFileTemplatesDir();
        File includesDir = new File(templatesDir, "includes");
        return templatesDir.getAbsolutePath() + "," + includesDir.getAbsolutePath();
    }

    public String processTemplate(String templateName, VelocityContext context) throws Exception, VelocityException {
        initVelocityProcess();
        StringWriter writer = new StringWriter();
        velocity.mergeTemplate(templateName, context, writer);
        return writer.toString();
    }

    public VelocityContext getContext() {
        return context;
    }

    public void setContext(VelocityContext context) {
        this.context = context;
    }

    private static void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    static  {
        LOG = LogUtil.getLogger(org.intellij.plugins.util.TemplateEngine.class);
    }

}

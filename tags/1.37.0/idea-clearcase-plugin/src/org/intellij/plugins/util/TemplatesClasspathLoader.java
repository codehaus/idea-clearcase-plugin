// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   TemplatesClasspathLoader.java

package org.intellij.plugins.util;

import java.io.InputStream;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class TemplatesClasspathLoader extends ClasspathResourceLoader {

    public static final String TEMPLATE_SUBDIR = "fileTemplates/";
    public static final String TEMPLATE_INCLUDE_SUBDIR = "fileTemplates/includes/";

    public TemplatesClasspathLoader() {
    }

    public synchronized InputStream getResourceStream(String templateName) throws ResourceNotFoundException {
        InputStream inputStream = super.getResourceStream("fileTemplates/" + templateName + ".ft");
        if (inputStream == null) {
            inputStream = super.getResourceStream("fileTemplates/includes/" + templateName + ".ft");
        }
        return inputStream;
    }
}

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   PluginConfigParser.java

package org.intellij.openapi;

import java.io.InputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;

public class PluginConfigParser {

    private InputStream configStream;
    private Class projectComponentClasses[];
    private Class applicationComponentClasses[];

    public PluginConfigParser(InputStream pluginConfigStream) {
        configStream = pluginConfigStream;
        try {
            parse();
        }
        catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Document createDocument(InputStream pluginConfig) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        builder.setValidation(false);
        return builder.build(pluginConfig);
    }

    public Class[] getProjectComponentClasses() {
        return projectComponentClasses;
    }

    public Class[] getApplicationComponentClasses() {
        return applicationComponentClasses;
    }

    public void parse() throws JDOMException, IOException {
        Document document = createDocument(configStream);
        Element root = document.getRootElement();
        projectComponentClasses = createClassArray(parseComponentClasses(root, "project-components"));
        applicationComponentClasses = createClassArray(parseComponentClasses(root, "application-components"));
    }

    private static Class[] createClassArray(Collection classes) {
        return (Class[])classes.toArray(new Class[classes.size()]);
    }

    private Set<Class> parseComponentClasses(Element parentElement, String componentListElementName) {
        List projectComponentsElts = parentElement.getChildren(componentListElementName);
        Set<Class> componentClasses = new HashSet<Class>();

        label0:
        for (Object projectComponentsElt : projectComponentsElts) {
            Element element = (Element) projectComponentsElt;
            List componentElts = element.getChildren("component");
            Iterator iterator = componentElts.iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    continue label0;
                }
                Element componentElt = (Element) iterator.next();
                List implementations = componentElt.getChildren("implementation-class");
                if (implementations.size() > 0) {
                    Element implementation = (Element) implementations.get(0);
                    Class clazz = getClassForName(implementation.getTextTrim());
                    if (clazz != null) {
                        componentClasses.add(clazz);
                    }
                }
            }
        }

        return componentClasses;
    }

    private Class getClassForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}

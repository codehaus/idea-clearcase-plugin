// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   LogFactoryImpl.java

package org.apache.commons.logging.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

public class LogFactoryImpl extends LogFactory {

    public static final String LOG_PROPERTY = "org.apache.commons.logging.Log";
    protected static final String LOG_PROPERTY_OLD = "org.apache.commons.logging.log";
    protected Hashtable attributes;
    protected Hashtable instances;
    private String logClassName;
    protected Constructor logConstructor;
    protected Class logConstructorSignature[];
    protected Method logMethod;
    protected Class logMethodSignature[];

    public LogFactoryImpl() {
        attributes = new Hashtable();
        instances = new Hashtable();
        logClassName = (org.intellij.plugins.util.logging.Log4JLogger.class).getName();
        logConstructor = null;
        logConstructorSignature = (new Class[] {
            java.lang.String.class
        });
        logMethod = null;
        logMethodSignature = (new Class[] {
            org.apache.commons.logging.LogFactory.class
        });
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public String[] getAttributeNames() {
        Vector names = new Vector();
        for (Enumeration keys = attributes.keys(); keys.hasMoreElements(); names.addElement((String)keys.nextElement())) { }
        String results[] = new String[names.size()];
        for (int i = 0; i < results.length; i++) {
            results[i] = (String)names.elementAt(i);
        }

        return results;
    }

    public Log getInstance(Class clazz) throws LogConfigurationException {
        return getInstance(clazz.getName());
    }

    public Log getInstance(String name) throws LogConfigurationException {
        Log instance = (Log)instances.get(name);
        if (instance == null) {
            instance = newInstance(name);
            instances.put(name, instance);
        }
        return instance;
    }

    public void release() {
        instances.clear();
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public void setAttribute(String name, Object value) {
        if (value == null) {
            attributes.remove(name);
        } else {
            attributes.put(name, value);
        }
    }

    protected String getLogClassName() {
        if (logClassName != null) {
            return logClassName;
        }
        logClassName = (String)getAttribute("org.apache.commons.logging.Log");
        if (logClassName == null) {
            logClassName = (String)getAttribute("org.apache.commons.logging.log");
        }
        if (logClassName == null) {
            try {
                logClassName = System.getProperty("org.apache.commons.logging.Log");
            }
            catch (SecurityException e) { }
        }
        if (logClassName == null) {
            try {
                logClassName = System.getProperty("org.apache.commons.logging.log");
            }
            catch (SecurityException e) { }
        }
        if (logClassName == null && isLog4JAvailable()) {
            logClassName = "org.apache.commons.logging.impl.Log4JLogger";
        }
        if (logClassName == null && isJdk14Available()) {
            logClassName = "org.apache.commons.logging.impl.Jdk14Logger";
        }
        if (logClassName == null) {
            logClassName = "org.apache.commons.logging.impl.SimpleLog";
        }
        return logClassName;
    }

    protected Constructor getLogConstructor() throws LogConfigurationException {
        if (logConstructor != null) {
            return logConstructor;
        }
        String logClassName = getLogClassName();
        Class logClass = null;
        try {
            logClass = loadClass(logClassName);
            if (logClass == null) {
                throw new LogConfigurationException("No suitable Log implementation for " + logClassName);
            }
            if (!(org.apache.commons.logging.Log.class).isAssignableFrom(logClass)) {
                throw new LogConfigurationException("Class " + logClassName + " does not implement Log");
            }
        } catch (Throwable t) {
            throw new LogConfigurationException(t);
        }

        try {
            logMethod = logClass.getMethod("setLogFactory", logMethodSignature);
        } catch (Throwable t) {
            logMethod = null;
            try {
                logConstructor = logClass.getConstructor(logConstructorSignature);
            } catch (Throwable t2) {
                throw new LogConfigurationException("No suitable Log constructor " + logConstructorSignature + " for " + logClassName, t2);
            }
        }
        return logConstructor;
    }

    private static Class loadClass(final String name) throws ClassNotFoundException {
        Object result = AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                ClassLoader threadCL = LogFactoryImpl.getContextClassLoader();
                try {
                    if (threadCL == null) {
                        return Class.forName(name);
                    }
                    return threadCL.loadClass(name);
                } catch (ClassNotFoundException e) {
                    return e;
                }
            }
        });

        if (result instanceof Class) {
            return (Class)result;
        } else {
            throw (ClassNotFoundException)result;
        }
    }

    protected boolean isJdk14Available() {
        try {
            loadClass("java.util.logging.Logger");
            loadClass("org.apache.commons.logging.impl.Jdk14Logger");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    protected boolean isLog4JAvailable() {
        try {
            loadClass("org.apache.log4j.Logger");
            loadClass("org.apache.commons.logging.impl.Log4JLogger");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    protected Log newInstance(String name) throws LogConfigurationException {
        Log instance = null;
        Object params[] = new Object[1];
        params[0] = name;
        try {
            instance = (Log)getLogConstructor().newInstance(params);
            if (logMethod != null) {
                params[0] = this;
                logMethod.invoke(instance, params);
            }
            return instance;
        } catch (Throwable t) {
            throw new LogConfigurationException(t);
        }
    }
}

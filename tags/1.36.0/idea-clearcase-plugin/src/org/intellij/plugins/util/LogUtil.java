// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   LogUtil.java

package org.intellij.plugins.util;

import com.intellij.openapi.diagnostic.Logger;

public class LogUtil {

    public LogUtil() {
    }

    public static String getCallerMethod() {
        String methodName = "";
        StackTraceElement stackTrace[] = (new Throwable()).getStackTrace();
        if (stackTrace != null) {
            methodName = stackTrace[2].getClassName() + "." + stackTrace[2].getMethodName();
        }
        return methodName;
    }

    public static String getCallerClass() {
        String className = "";
        StackTraceElement stackTrace[] = (new Throwable()).getStackTrace();
        if (stackTrace != null) {
            className = stackTrace[2].getClassName();
        }
        return className;
    }

    public static Logger getLogger(Class c) {
        return Logger.getInstance(c.getName());
    }

    public static Logger getLogger() {
        return Logger.getInstance(getCallerClass());
    }
}

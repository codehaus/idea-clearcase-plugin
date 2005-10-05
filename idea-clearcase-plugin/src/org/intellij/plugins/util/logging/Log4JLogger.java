// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   Log4JLogger.java

package org.intellij.plugins.util.logging;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public final class Log4JLogger
    implements Log {

    private static final String FQCN;
    private Logger logger;

    public Log4JLogger() {
        logger = null;
    }

    public Log4JLogger(String name) {
        logger = null;
        System.out.println("Log4JLogger");
        logger = Logger.getLogger(name);
    }

    public Log4JLogger(Logger logger) {
        this.logger = null;
        System.out.println("Log4JLogger");
        this.logger = logger;
    }

    public void trace(Object message) {
        logger.log(FQCN, Priority.DEBUG, message, null);
    }

    public void trace(Object message, Throwable t) {
        logger.log(FQCN, Priority.DEBUG, message, t);
    }

    public void debug(Object message) {
        logger.log(FQCN, Priority.DEBUG, message, null);
    }

    public void debug(Object message, Throwable t) {
        logger.log(FQCN, Priority.DEBUG, message, t);
    }

    public void info(Object message) {
        logger.log(FQCN, Priority.INFO, message, null);
    }

    public void info(Object message, Throwable t) {
        logger.log(FQCN, Priority.INFO, message, t);
    }

    public void warn(Object message) {
        logger.log(FQCN, Priority.WARN, message, null);
    }

    public void warn(Object message, Throwable t) {
        logger.log(FQCN, Priority.WARN, message, t);
    }

    public void error(Object message) {
        logger.log(FQCN, Priority.ERROR, message, null);
    }

    public void error(Object message, Throwable t) {
        logger.log(FQCN, Priority.ERROR, message, t);
    }

    public void fatal(Object message) {
        logger.log(FQCN, Priority.FATAL, message, null);
    }

    public void fatal(Object message, Throwable t) {
        logger.log(FQCN, Priority.FATAL, message, t);
    }

    public Logger getLogger() {
        return logger;
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public boolean isErrorEnabled() {
        return logger.isEnabledFor(Priority.ERROR);
    }

    public boolean isFatalEnabled() {
        return logger.isEnabledFor(Priority.FATAL);
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public boolean isTraceEnabled() {
        return logger.isDebugEnabled();
    }

    public boolean isWarnEnabled() {
        return logger.isEnabledFor(Priority.WARN);
    }

    static Class _mthclass$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw (new NoClassDefFoundError()).initCause(x1);
        }
    }

    static  {
        FQCN = (org.apache.commons.logging.impl.Log4JLogger.class).getName();
    }
}

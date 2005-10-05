// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   CallLog.java

package org.intellij.plugins.util.testing;

import java.util.ArrayList;
//import junitx.framework.ArrayAssert;

public class CallLog {

    ArrayList actualCalls;
    ArrayList expectedCalls;

    public CallLog() {
        actualCalls = new ArrayList();
        expectedCalls = new ArrayList();
    }

    public void addActualCallTo(String method) {
        actualCalls.add(method);
    }

    public void addActualCallToCurrentMethod() {
        addActualCallTo(getCallerMethod());
    }

    public void addActualCallToCurrentMethod(Object arg) {
        addActualCallTo(getCallerMethod() + "(" + arg + ")");
    }

    public void addExpectedCallTo(String methodName) {
        expectedCalls.add(methodName);
    }

    public void addExpectedCallTo(String methodName, Object arg) {
        expectedCalls.add(methodName + "(" + arg + ")");
    }

    private String getCallerMethod() {
        String methodName = "";
        StackTraceElement stackTrace[] = (new Throwable()).getStackTrace();
        if (stackTrace != null) {
            methodName = stackTrace[2].getMethodName();
        }
        return methodName;
    }

    public void verify(String expectedMethodCalls[]) {
        throw new UnsupportedOperationException("Couldn't Compile");
//        ArrayAssert.assertEquals("calls", expectedMethodCalls, actualCalls.toArray(new String[0]));
    }

    public void verifyNoCalls() {
        verify(new String[0]);
    }

    public void verify() {
        verify((String[])expectedCalls.toArray(new String[expectedCalls.size()]));
    }

    public void reset() {
        expectedCalls.clear();
        actualCalls.clear();
    }
}

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   DummySystemInfo.java

package org.intellij.plugins.util.testing;

import org.intellij.plugins.util.SystemInfo;

public class DummySystemInfo extends SystemInfo {

    public int currentTimeInMs;

    public DummySystemInfo() {
        currentTimeInMs = 50000;
    }

    public long getCurrentTimeInMs() {
        return (long)currentTimeInMs;
    }
}

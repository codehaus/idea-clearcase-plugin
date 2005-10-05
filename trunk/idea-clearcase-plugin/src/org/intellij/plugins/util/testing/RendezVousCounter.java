// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   RendezVousCounter.java

package org.intellij.plugins.util.testing;


public class RendezVousCounter {

    private int count;

    public RendezVousCounter() {
    }

    public synchronized void waitUntilCountReached(int callCount, int timeoutInMs) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        do {
            if (count >= callCount) {
                break;
            }
            long timeAlreadyWaited = System.currentTimeMillis() - startTime;
            if (timeAlreadyWaited >= (long)timeoutInMs) {
                break;
            }
            wait((long)timeoutInMs - timeAlreadyWaited);
        } while (true);
        count = 0;
    }

    public synchronized void increment() {
        count++;
        notifyAll();
    }
}

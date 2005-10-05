// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   ListenerNotifier.java

package org.intellij.plugins;

import java.beans.PropertyChangeListener;

public interface ListenerNotifier {

    public abstract PropertyChangeListener[] getListeners();

    public abstract void addListener(PropertyChangeListener propertychangelistener);

    public abstract void notifyListenersOfChange();

    public abstract void removeListener(PropertyChangeListener propertychangelistener);
}

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   ActionHandler.java

package org.intellij.plugins.actions;

import java.awt.event.InputEvent;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;

public abstract class ActionHandler {

    private AnActionEvent event;

    public ActionHandler() {
    }

    public abstract void update();

    public abstract void actionPerformed();

    public void setEvent(AnActionEvent event) {
        this.event = event;
    }

    public AnActionEvent getEvent() {
        return event;
    }

    public String getPlace() {
        return event.getPlace();
    }

    public InputEvent getInputEvent() {
        return event.getInputEvent();
    }

    public DataContext getDataContext() {
        return event.getDataContext();
    }

    public int getModifiers() {
        return event.getModifiers();
    }

    public Presentation getPresentation() {
        return event.getPresentation();
    }
}

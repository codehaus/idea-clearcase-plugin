// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   SimpleListModel.java

package org.intellij.plugins.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

public class SimpleListModel extends AbstractListModel {

    private final List list;

    public SimpleListModel() {
        this(((List) (new ArrayList())));
    }

    public SimpleListModel(Object list[]) {
        this.list = new ArrayList(Arrays.asList(list));
    }

    public SimpleListModel(List list) {
        this.list = list;
    }

    public int getSize() {
        return list.size();
    }

    public List getElements() {
        return list;
    }

    public Object getElementAt(int index) {
        return list.get(index);
    }

    public void addElement(Object elt) {
        list.add(elt);
    }

    public void addAllElement(Object elts[]) {
        int fromIndex = list.size();
        list.addAll(Arrays.asList(elts));
        int toIndex = list.size();
        fireIntervalAdded(this, fromIndex, toIndex);
    }

    public void removeAllElement(Object elts[]) {
        int toIndex = list.size();
        list.removeAll(Arrays.asList(elts));
        int fromIndex = list.size();
        fireIntervalRemoved(this, fromIndex, toIndex);
    }
}

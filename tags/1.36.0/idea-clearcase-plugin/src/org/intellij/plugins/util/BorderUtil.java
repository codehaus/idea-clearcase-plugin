// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   BorderUtil.java

package org.intellij.plugins.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class BorderUtil {

    public BorderUtil() {
    }

    public static TitledBorder newTitledBorder(String s) {
        return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), s);
    }

    public static Border newEtchedBorder() {
        return BorderFactory.createEtchedBorder();
    }
}

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   IconManager.java

package org.intellij.plugins.ui;

import java.util.HashMap;

import javax.swing.*;

// Referenced classes of package org.intellij.plugins.ui:
//            IconUtil

public class IconManager {

    HashMap iconPathsByName;
    HashMap iconByName;

    public IconManager() {
        iconPathsByName = new HashMap();
        iconByName = new HashMap();
    }

    public void setIconPathsByName(HashMap iconPathsByName) {
        this.iconPathsByName = iconPathsByName;
    }

    public HashMap getIconPathsByName() {
        return iconPathsByName;
    }

    public Icon getIcon(String name) {
        Icon icon = (Icon)iconByName.get(name);
        if (icon == null) {
            String path = (String)iconPathsByName.get(name);
            icon = fetchIcon(path);
            iconByName.put(name, icon);
        }
        return icon;
    }

    protected Icon fetchIcon(String path) {
        return IconUtil.getIcon(path);
    }
}

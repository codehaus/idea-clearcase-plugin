// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   CollectionsUtil.java

package org.intellij.plugins.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionsUtil {

    public CollectionsUtil() {
    }

    public static List intersection(Object array1[], Object array2[]) {
        ArrayList result = new ArrayList();
        if (array1 != null && array2 != null) {
            for (int i = 0; i < array1.length; i++) {
                Object o1 = array1[i];
                for (int j = 0; j < array2.length; j++) {
                    Object o2 = array2[j];
                    if (o2.equals(o1)) {
                        result.add(o1);
                    }
                }

            }

        }
        return result;
    }
}

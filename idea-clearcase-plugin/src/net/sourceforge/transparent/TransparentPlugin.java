// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   TransparentPlugin.java

package net.sourceforge.transparent;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.LocalFileSystem;
import org.jdom.Element;

// Referenced classes of package net.sourceforge.transparent:
//            ClearCaseFileListener

public class TransparentPlugin
    implements ApplicationComponent, JDOMExternalizable {

    private ClearCaseFileListener _listener;

    public TransparentPlugin() {
        _listener = new ClearCaseFileListener();
    }

    public String getComponentName() {
        return "Transparent ClearCase Integration";
    }

    public void initComponent() {
        LocalFileSystem.getInstance().addVirtualFileListener(_listener);
        System.out.println(getComponentName() + " loaded");
    }

    public void disposeComponent() {
        LocalFileSystem.getInstance().removeVirtualFileListener(_listener);
    }

    public void readExternal(Element element1) throws InvalidDataException {
    }

    public void writeExternal(Element element1) throws WriteExternalException {
    }
}

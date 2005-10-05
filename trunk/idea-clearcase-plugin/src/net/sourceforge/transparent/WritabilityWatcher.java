// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   WritabilityWatcher.java

package net.sourceforge.transparent;

import com.intellij.openapi.util.Key;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFilePropertyEvent;

// Referenced classes of package net.sourceforge.transparent:
//            TransparentFileStatusProvider

public class WritabilityWatcher {

    private TransparentFileStatusProvider statusProvider;
    private VirtualFileManager virtualFileMgr;
    public static final Key STATUS = new Key("Transparent.Status");

    public WritabilityWatcher(TransparentFileStatusProvider provider, VirtualFileManager virtualFileMgr) {
        statusProvider = provider;
        this.virtualFileMgr = VirtualFileManager.getInstance();
        virtualFileMgr.addVirtualFileListener(new VirtualFileAdapter() {

            public void propertyChanged(VirtualFilePropertyEvent event) {
                boolean isChangeOfWritability = event.getPropertyName().equals("writable");
                if (isChangeOfWritability && event.getNewValue().equals(Boolean.TRUE)) {
                    event.getFile().putUserData(WritabilityWatcher.STATUS, FileStatus.MODIFIED);
                }
            }

        });
    }

}

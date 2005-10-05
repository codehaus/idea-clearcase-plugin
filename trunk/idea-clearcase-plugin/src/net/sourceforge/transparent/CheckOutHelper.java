// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   CheckOutHelper.java

package net.sourceforge.transparent;

import java.util.Arrays;

import com.intellij.openapi.vcs.AbstractVcsHelper;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.plugins.util.FileUtil;

// Referenced classes of package net.sourceforge.transparent:
//            ClearCaseFile, TransparentVcs, TransparentConfiguration

public class CheckOutHelper {

    private TransparentVcs vcs;
    private AbstractVcsHelper vcsHelper;
    private TransparentConfiguration config;
    private FileUtil fileUtil;

    public CheckOutHelper(TransparentVcs transparentVcs) {
        this(transparentVcs, AbstractVcsHelper.getInstance(transparentVcs.getProject()), transparentVcs.getTransparentConfig(), new FileUtil());
    }

    public CheckOutHelper(TransparentVcs transparentVcs, AbstractVcsHelper vcsHelper, TransparentConfiguration transparentConfig, FileUtil fileUtil) {
        vcs = transparentVcs;
        this.vcsHelper = vcsHelper;
        config = transparentConfig;
        this.fileUtil = fileUtil;
    }

    public void checkOutOrHijackFile(VirtualFile file) {
        try {
            makeFileWritable(file);
        }
        catch (Throwable e) {
            vcsHelper.showErrors(Arrays.asList(new VcsException[] {
                new VcsException(e)
            }), "Exception while " + (shouldHijackFile(file) ? "hijacking " : "checking out ") + file.getPresentableUrl());
        }
    }

    public boolean shouldHijackFile(VirtualFile file) {
        return config.offline || !isElement(file);
    }

    private boolean isElement(VirtualFile file) {
        return (new ClearCaseFile(file, vcs.getClearCase())).isElement();
    }

    private void makeFileWritable(VirtualFile file) throws VcsException {
        if (shouldHijackFile(file)) {
            hijackFile(file);
        } else {
            vcs.checkoutFile(file.getPresentableUrl(), false);
        }
    }

    public void hijackFile(VirtualFile file) throws VcsException {
        try {
            fileUtil.setFileWritability(file, true);
        }
        catch (Exception e) {
            throw new VcsException(e);
        }
    }
}

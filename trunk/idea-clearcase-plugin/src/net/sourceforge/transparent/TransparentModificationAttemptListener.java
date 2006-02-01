// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   TransparentModificationAttemptListener.java

package net.sourceforge.transparent;

import javax.swing.*;

import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.EditFileProvider;
import com.intellij.openapi.vfs.ModificationAttemptEvent;
import com.intellij.openapi.vfs.ModificationAttemptListener;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.openapi.OpenApiFacade;
import org.intellij.plugins.util.RegularFileFilter;

import java.util.Arrays;

// Referenced classes of package net.sourceforge.transparent:
//            CheckOutHelper, TransparentVcs, TransparentConfiguration

public class TransparentModificationAttemptListener
    implements ModificationAttemptListener, EditFileProvider {

    private TransparentVcs transparentVcs;
    private CheckOutHelper checkOutHelper;
    private RegularFileFilter regularFileFilter;

    public TransparentModificationAttemptListener(TransparentVcs transparentVcs, CheckOutHelper checkOutHelper, RegularFileFilter regularFileFilter) {
        this.transparentVcs = transparentVcs;
        this.checkOutHelper = checkOutHelper;
        this.regularFileFilter = regularFileFilter;
    }

    public TransparentModificationAttemptListener(TransparentVcs vcs) {
        transparentVcs = vcs;
        regularFileFilter = new RegularFileFilter();
    }

    public CheckOutHelper getCheckOutHelper() {
        if (checkOutHelper == null) {
            checkOutHelper = new CheckOutHelper(transparentVcs);
        }
        return checkOutHelper;
    }

    public void readOnlyModificationAttempt(ModificationAttemptEvent event) {
        if (event.isConsumed()) {
            return;
        }
        VirtualFile[] files = event.getFiles();
        checkOutIfNecessary(files);

        event.consume();
    }

    private void checkOutIfNecessary(VirtualFile[] files)
    {
        for (VirtualFile file : files)
        {
           if (isUnderVcs(file))
              checkOutOrHijackFile(file);
        }
    }

    public boolean isUnderVcs(VirtualFile file) {
        return transparentVcs.getFileFilter().accept(file) && regularFileFilter.accept(file);
    }

    private void checkOutOrHijackFile(VirtualFile file) {
        getCheckOutHelper().checkOutOrHijackFile(file);
    }

    private boolean shouldCheckoutFile(VirtualFile file) {
        String path = file.getPresentableUrl();
        boolean shouldCheckoutFile = transparentVcs.getTransparentConfig().automaticCheckout;
        if (!shouldCheckoutFile) {
            String message = "The file " + path + " is readonly\n" + "Do you want to " + (getCheckOutHelper().shouldHijackFile(file) ? "hijack it" : "check it out") + "?";
            int answer = askUser(message);
            shouldCheckoutFile = answer == 0;
        }
        return shouldCheckoutFile;
    }

    protected int askUser(String message) {
        return JOptionPane.showConfirmDialog(null, message, "Readonly file", 0, 3, Messages.getQuestionIcon());
    }

    public void start() {
        OpenApiFacade.getVirtualFileManager().addModificationAttemptListener(this);
    }

    public void stop() {
        OpenApiFacade.getVirtualFileManager().removeModificationAttemptListener(this);
    }

    public void editFiles(VirtualFile files[]) {
//        System.err.println("Editing files: " + Arrays.asList(files));
        checkOutIfNecessary(files);
    }

    public String getRequestText() {
        return "World, edit?";
    }
}

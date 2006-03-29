// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   NativeClearCase.java

package net.sourceforge.transparent;

import net.sourceforge.eclipseccase.jni.Clearcase;
import org.intellij.plugins.util.FileUtil;

import java.io.File;

public class NativeClearCase
    implements ClearCase {

    public String getName() {
        return NativeClearCase.class.getName();
    }

    public NativeClearCase() {
        Clearcase.isElement("c:/");
    }

    private void checkStatus(Clearcase.Status status) {
        if (!status.status) {
            throw new ClearCaseException(status.message);
        }
    }

    public void undoCheckOut(File file) {
        checkStatus(Clearcase.uncheckout(file.getPath(), false));
    }

    public void checkIn(File file, String comment) {
        checkStatus(Clearcase.checkin(file.getPath(), comment, true));
    }

    public void checkOut(File file, boolean isReserved, String comment) {
        checkStatus(Clearcase.checkout(file.getPath(), comment, isReserved, true));
    }

    public void delete(File file, String comment) {
        checkStatus(Clearcase.delete(file.getPath(), comment));
    }

    public void add(File file, String comment) {
        if (file.isDirectory()) {
            doAddDir(file, comment);
        } else {
            doAdd(file, comment);
        }
    }

    private void doAdd(File file, String comment) {
        checkStatus(Clearcase.add(file.getPath(), comment, file.isDirectory()));
    }

    private void doAddDir(File dir, String comment) {
        File tmpDir = new File(dir.getParentFile(), dir.getName() + ".add");
        if (!dir.renameTo(tmpDir)) {
            throw new ClearCaseException("Could not rename " + dir.getPath() + " to " + tmpDir.getName());
        }
        try {
            checkStatus(Clearcase.add(dir.getPath(), comment, true));
        }
        finally {
            if (!FileUtil.moveDirWithContent(tmpDir, dir)) {
                throw new ClearCaseException(
                    "Could not move back the content of " + dir.getPath()
                        + " as part of adding it to Clearcase:\n"
                        + "Its old content is in " + tmpDir.getName()
                        + ". Please move it back manually");
            }
        }
    }

    public void move(File file, File target, String comment) {
        checkStatus(Clearcase.move(file.getPath(), target.getPath(), comment));
    }

    public Status getStatus(File file) {
        if (!isElement(file)) {
            return Status.NOT_AN_ELEMENT;
        }
        if (isCheckedOut(file)) {
            return Status.CHECKED_OUT;
        }
        if (Clearcase.isHijacked(file.getPath())) {
            return Status.HIJACKED;
        } else {
            return Status.CHECKED_IN;
        }
    }

    public boolean isElement(File file) {
        return Clearcase.isElement(file.getPath());
    }

    public boolean isCheckedOut(File file) {
        return Clearcase.isCheckedOut(file.getPath());
    }

    public void cleartool(String cmd) {
        checkStatus(Clearcase.cleartool(cmd));
    }

    public CheckedOutStatus getCheckedOutStatus(File file) {
        Clearcase.Status status = Clearcase.cleartool("lscheckout -fmt %Rf -directory " + file.getPath());
        if (status == null || status.message == null) {
            return CheckedOutStatus.NOT_CHECKED_OUT;
        }
        if (status.message.equalsIgnoreCase("reserved")) {
            return CheckedOutStatus.RESERVED;
        }
        if (status.message.equalsIgnoreCase("unreserved")) {
            return CheckedOutStatus.UNRESERVED;
        } else {
            return CheckedOutStatus.NOT_CHECKED_OUT;
        }
    }

    public String getCheckoutComment(File file) {
        Clearcase.Status status = Clearcase.cleartool("lscheckout -fmt %c -directory " + file.getPath());
        if (status == null || status.message == null) {
            return "";
        }
        return status.message;
    }

    /**
     * Don't know how to query with cleartool this information
     *
     * @param file
     * @return 
     */
    public boolean isLatestVersion(File file) {
        return true;
    }
}

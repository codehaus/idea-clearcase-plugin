// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   AbstractClearCase.java

package net.sourceforge.transparent;

import net.sourceforge.clearcase.simple.ClearcaseException;
import net.sourceforge.clearcase.simple.ClearcaseFactory;
import net.sourceforge.clearcase.simple.IClearcase;

import java.io.File;

public abstract class AbstractClearCase implements ClearCase {

    protected IClearcase cc;

    public AbstractClearCase(int type) throws ClearcaseException {
        cc = ClearcaseFactory.getInstance().createInstance(type);
    }

    public String getName() {
        return getClass().getName();
    }

    public void move(File file, File target, String comment) {
        checkStatus(cc.move(file.getPath(), target.getPath(), comment));
    }

    private void checkStatus(IClearcase.Status status) {
        if (!status.status) {
            throw new ClearCaseException(status.message);
        }
    }

    public void undoCheckOut(File file) {
        checkStatus(cc.uncheckout(file.getPath(), false));
    }

    public void checkIn(File file, String comment) {
        checkStatus(cc.checkin(file.getPath(), comment, true));
    }

    public void checkOut(File file, boolean isReserved, String comment) {
        checkStatus(cc.checkout(file.getPath(), "", isReserved, true));
    }

    public void delete(File file, String comment) {
        checkStatus(cc.delete(file.getPath(), ""));
    }

    public void add(File file, String comment) {
        checkStatus(cc.add(file.getPath(), "", file.isDirectory(), false)); //TODO: use makeMaster parameter properly
    }

    public Status getStatus(File file) {
        if (isHijacked(file)) {
            return Status.HIJACKED;
        }
        if (!isElement(file)) {
            return Status.NOT_AN_ELEMENT;
        }
        if (isCheckedOut(file)) {
            return Status.CHECKED_OUT;
        } else {
            return Status.CHECKED_IN;
        }
    }

    public boolean isHijacked(File file) {
        return cc.isHijacked(file.getPath(), false); //TODO: use isSymbolicLink parameter properly
    }

    public boolean isElement(File file) {
        return cc.isElement(file.getPath());
    }

    public boolean isCheckedOut(File file) {
        return cc.isCheckedOut(file.getPath(), false); //TODO: use isSymbolicLink parameter properly
    }

    public void cleartool(String cmd) {
        checkStatus(cc.cleartool(cmd));
    }

    public CheckedOutStatus getCheckedOutStatus(File file) {
        IClearcase.Status status = cc.cleartool("lscheckout -fmt %Rf -directory " + file.getPath());
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
        return "<undefined>";
    }

}

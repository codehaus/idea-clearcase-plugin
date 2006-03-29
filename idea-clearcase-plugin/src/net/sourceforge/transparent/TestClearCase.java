// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   TestClearCase.java

package net.sourceforge.transparent;

import java.awt.*;
import java.io.File;

import javax.swing.*;

import com.intellij.openapi.ui.DialogWrapper;

// Referenced classes of package net.sourceforge.transparent:
//            Status, ClearCase, CheckedOutStatus

public class TestClearCase
    implements ClearCase {
    class StatusDialog extends DialogWrapper {

        public JComboBox status;
        public String location;

        private void setLocation() {
            Exception e = new Exception();
            StackTraceElement stackTrace[] = e.getStackTrace();
            for (int i = 1; i < Math.min(stackTrace.length, 20); i++) {
                location += stackTrace[i] + "\n";
            }

        }

        protected JComponent createCenterPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            JTextArea loc = new JTextArea(location);
            panel.add(loc, "North");
            status = new JComboBox();
            status.addItem(Status.CHECKED_IN);
            status.addItem(Status.CHECKED_OUT);
            status.addItem(Status.HIJACKED);
            status.addItem(Status.NOT_AN_ELEMENT);
            panel.add(status, "Center");
            return panel;
        }

        public StatusDialog() {
            super(true);
            setTitle("Status returned");
            setLocation();
            init();
        }
    }


    public TestClearCase() {
    }

    public void add(File file, String comment) {
        System.out.println("ClearCase.add(" + file.getPath() + ")");
    }

    public String getName() {
        return (net.sourceforge.transparent.TestClearCase.class).getName();
    }

    public void checkIn(File file, String comment) {
        System.out.println("ClearCase.checkIn(" + file.getPath() + "," + comment + ")");
    }

    public void checkOut(File file, boolean isReserved, String comment) {
        System.out.println("ClearCase.checkOut(" + file.getPath() + "," + isReserved + ")");
    }

    public void delete(File file, String comment) {
        System.out.println("ClearCase.delete(" + file.getPath() + ")");
    }

    public Status getStatus(File file) {
        System.out.println("ClearCase.getStatus(" + file.getPath() + ")");
        StatusDialog d = new StatusDialog();
        d.setTitle("Status for " + file.getPath());
        showStatusDialog(d);
        Status status = (Status)d.status.getSelectedItem();
        System.out.println(" -> " + status);
        return status;
    }

    public boolean isElement(File file) {
        Status status = getStatus(file);
        return status == Status.CHECKED_IN || status == Status.CHECKED_OUT;
    }

    public boolean isCheckedOut(File file) {
        return getStatus(file) == Status.CHECKED_OUT;
    }

    private void showStatusDialog(final StatusDialog d) {
        if (EventQueue.isDispatchThread()) {
            d.show();
        } else {
            try {
                EventQueue.invokeAndWait(new Runnable() {

                    public void run() {
                        d.show();
                    }
                });
            }
            catch (Exception x) {
                throw new RuntimeException(x);
            }
        }
    }

    public void move(File file, File target, String comment) {
        System.out.println("ClearCase.move(" + file.getPath() + "," + target.getPath() + ")");
    }

    public void undoCheckOut(File file) {
        System.out.println("ClearCase.undoCheckoutFile(" + file.getPath() + ")");
    }

    public void cleartool(String cmd) {
        System.out.println("ClearCase.cleartool(" + cmd + ")");
    }

    public CheckedOutStatus getCheckedOutStatus(File file) {
        return null;
    }

    public String getCheckoutComment(File file) {
        return null;
    }

    public boolean isLatestVersion(File file) {
        System.out.println("ClearCase.isLatestVersion(" + file.getPath() + ")");
        return true;
    }


}

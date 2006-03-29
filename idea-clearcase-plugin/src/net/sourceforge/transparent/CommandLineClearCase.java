// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   CommandLineClearCase.java

package net.sourceforge.transparent;

import com.intellij.openapi.diagnostic.Logger;
import org.intellij.plugins.util.FileUtil;

import java.io.File;

// Referenced classes of package net.sourceforge.transparent:
//            ClearCaseException, Runner, ClearCase, Status, 
//            CheckedOutStatus

public class CommandLineClearCase
    implements ClearCase {

    private static final Logger LOG = Logger.getInstance("net.sourceforge.transparent.CommandLineClearCase");

    public CommandLineClearCase() {
    }

    public String getName() {
        return (net.sourceforge.transparent.CommandLineClearCase.class).getName();
    }

    public void undoCheckOut(File file) {
        cleartool(new String[]{
            "unco", "-rm", file.getAbsolutePath()
        });
    }

    public void checkIn(File file, String comment) {
        cleartool(new String[]{
            "ci", "-c", quote(comment), "-identical", file.getAbsolutePath()
        });
    }

    public void checkOut(File file, boolean isReserved, String comment) {
        boolean withComment = false;
        if (comment != null && comment.length() > 0) {
            withComment = true;
        }

        if (withComment) {
            cleartool(new String[]{
                "co", "-c", quote(comment), isReserved ? "-reserved" : "-unreserved", file.getAbsolutePath()
            });
        } else {
            cleartool(new String[]{
                "co", "-nc", isReserved ? "-reserved" : "-unreserved", file.getAbsolutePath()
            });
        }
    }

    public void delete(File file, String comment) {
        cleartool(new String[]{
            "rmname", "-force", "-c", quote(comment), file.getAbsolutePath()
        });
    }

    public void add(File file, String comment) {
        if (file.isDirectory()) {
            doAddDir(file, comment);
        } else {
            doAdd("mkelem", file.getAbsolutePath(), comment);
        }
    }

    private void doAddDir(File dir, String comment) {
        File tmpDir = new File(dir.getParentFile(), dir.getName() + ".add");
        if (!dir.renameTo(tmpDir)) {
            throw new ClearCaseException("Could not rename " + dir.getPath() + " to " + tmpDir.getName());
        }
        try {
            doAdd("mkdir", dir.getAbsolutePath(), comment);
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

    private void doAdd(String subcmd, String path, String comment) {
        cleartool(new String[]{
            subcmd, "-c", quote(comment), path
        });
    }

    public void move(File file, File target, String comment) {
        cleartool(new String[]{
            "mv", "-c", quote(comment), file.getAbsolutePath(), target.getAbsolutePath()
        });
    }

    public boolean isElement(File file) {
        return getStatus(file) != Status.NOT_AN_ELEMENT;
    }

    public boolean isCheckedOut(File file) {
        return getStatus(file) == Status.CHECKED_OUT;
    }

    public Status getStatus(File file) {
        Runner runner = cleartool(new String[]{
            "ls", "-directory", file.getAbsolutePath()
        }, true);
        if (!runner.isSuccessfull()) {
            return Status.NOT_AN_ELEMENT;
        }
        if (runner.getOutput().indexOf("@@") == -1) {
            return Status.NOT_AN_ELEMENT;
        }
        if (runner.getOutput().indexOf("[hijacked]") != -1) {
            return Status.HIJACKED;
        }
        if (runner.getOutput().indexOf("Rule: CHECKEDOUT") != -1) {
            return Status.CHECKED_OUT;
        } else {
            return Status.CHECKED_IN;
        }
    }

    public void cleartool(String subcmd) {
        String cmd = "cleartool " + subcmd;
        LOG.debug(cmd);
        Runner runner = new Runner();
        runner.run(cmd);
    }

    public void cleartool(String subcmd[]) {
        cleartool(subcmd, false);
    }

    private Runner cleartool(String subcmd[], boolean canFail) {
        String cmd[] = Runner.getCommand("cleartool", subcmd);
        LOG.debug(Runner.getCommandLine(cmd));
        Runner runner = new Runner();
        runner.run(cmd, canFail);
        return runner;
    }

    public CheckedOutStatus getCheckedOutStatus(File file) {
        Runner runner = cleartool(new String[]{
            "lscheckout", "-fmt", "%Rf", "-directory", file.getAbsolutePath()
        }, true);
        if (!runner.isSuccessfull()) {
            return CheckedOutStatus.NOT_CHECKED_OUT;
        }
        if (runner.getOutput().equalsIgnoreCase("reserved")) {
            return CheckedOutStatus.RESERVED;
        }
        if (runner.getOutput().equalsIgnoreCase("unreserved")) {
            return CheckedOutStatus.UNRESERVED;
        }
        if (runner.getOutput().equals("")) {
            return CheckedOutStatus.NOT_CHECKED_OUT;
        } else {
            return CheckedOutStatus.NOT_CHECKED_OUT;
        }
    }

    public String getCheckoutComment(File file) {
        return "<undefined>";
    }

    public static String quote(String str) {
        return "\"" + str.replaceAll("\"", "\\\"") + "\"";
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

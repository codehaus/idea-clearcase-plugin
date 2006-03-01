// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   FileUtil.java

package org.intellij.plugins.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

// Referenced classes of package org.intellij.plugins.util:
//            CommandUtil

public class FileUtil {

    private CommandUtil commandUtil;

    public FileUtil() {
        this(new CommandUtil());
    }

    public FileUtil(CommandUtil commandUtil) {
        this.commandUtil = commandUtil;
    }

    public VirtualFile ioFileToVirtualFile(File file) {
        return LocalFileSystem.getInstance().findFileByIoFile(file);
    }

    public void setFileWritability(final VirtualFile file, boolean writable) throws IOException {
        if (!(file.getFileSystem() instanceof LocalFileSystem)) {
            throw new IllegalArgumentException("Wrong file system: " + file.getFileSystem());
        }
        if (file.isWritable() != writable) {
            String s = file.getPresentableUrl();
            setFileWritability(s, writable);
            commandUtil.runWriteActionWithoutException(new CommandUtil.Command() {

                public Object run() throws Exception {
                    file.refresh(false, false);
                    return null;
                }

            });
        }
    }

    public void setFileWritability(String path, boolean writable) throws IOException {
        Process process;
        if (SystemInfo.isWindows) {
            process = Runtime.getRuntime().exec(new String[] {
                "attrib", writable ? "-r" : "+r", path
            });
        } else {
            process = Runtime.getRuntime().exec(new String[] {
                "chmod", writable ? "u+w" : "u-w", path
            });
        }
        try {
            process.waitFor();
        }
        catch (InterruptedException interruptedexception) { }
    }

    public static String readFileIntoString(File file) throws IOException {
        FileInputStream istream = new FileInputStream(file);
        byte bytes[] = new byte[istream.available()];
        istream.read(bytes);
        istream.close();
        return new String(bytes);
    }

    public static void writeStringIntoFile(String s, File file) throws IOException {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(s);
            writer.write("\n");
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }

    public static final void rename(File oldfile, File newfile) throws IOException {
        if (!oldfile.exists()) {
            throw new IOException("File " + oldfile + " does not exist ");
        }
        if (newfile.exists() && !newfile.delete() && newfile.exists()) {
            throw new IOException("Cannot delete file " + newfile);
        }
        if (!oldfile.renameTo(newfile) && !newfile.exists()) {
            System.gc();
            try {
                Thread.sleep(10L);
            }
            catch (InterruptedException e) { }
            if (!oldfile.renameTo(newfile) && !newfile.exists()) {
                String data = readFileIntoString(oldfile);
                writeStringIntoFile(data, newfile);
                oldfile.delete();
            }
        }
    }

    public static final void delete(File file) {
        if (!file.exists()) {
            return;
        }
        if (!file.delete()) {
            System.gc();
            try {
                Thread.sleep(10L);
            }
            catch (InterruptedException ex) { }
            if (!file.delete()) {
                file.deleteOnExit();
            }
        }
    }

    public static void copyFile(String from, String to) throws IOException {
        File inputFile = new File(from);
        File outputFile = new File(to);
        FileReader in = new FileReader(inputFile);
        FileWriter out = new FileWriter(outputFile);
        char buf[] = new char[2048];
        for (int count = -1; (count = in.read(buf)) != -1;) {
            out.write(buf, 0, count);
        }

        in.close();
        out.close();
    }

    public static boolean moveDirWithContent(File fromDir, File toDir) {
        if (!toDir.exists()) {
            return fromDir.renameTo(toDir);
        }

        File[] files = fromDir.listFiles();
        if (files == null) {
            return false;
        }

        boolean success = true;

        for (int i = 0; i < files.length; i++) {
            File fromFile = files[i];
            File toFile = new File(toDir, fromFile.getName());
            success = success && fromFile.renameTo(toFile);
        }

        fromDir.delete();

        return success;
    }
}

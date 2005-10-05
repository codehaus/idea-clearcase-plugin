// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   CheckInConfig.java

package net.sourceforge.transparent.actions.checkin;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.VcsException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


public class CheckInConfig
    implements ProjectComponent {

    public String lastScr;
    public String scrTextFileName;
    private Project project;
    private String comment = "";
    private CheckInEnvironment env;

    public CheckInConfig(Project project) {
        scrTextFileName = "";
        this.project = project;
        lastScr = "";
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void copyToDialog(CheckInFields fields) throws VcsException {
        fields.setShowScrField(isScrNeeded());
        fields.setScr(lastScr);
    }

    public void copyFromDialog(CheckInFields fields) throws VcsException {
        if (!isScrNeeded()) {
            return;
        } else {
            lastScr = fields.getScr();
            writeScr();
            return;
        }
    }

    public boolean isScrNeeded() {
        return getScrFileName().length() != 0;
    }

    private String getScrFileName() {
        return scrTextFileName;
    }

    public void writeScr() throws VcsException {
        if (!isScrNeeded()) {
            return;
        }
        Writer writer = null;
        try {
            writer = createWriter(getScrFileName());
            writer.write(lastScr);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new VcsException("Could not write to the scr file: " + e.getMessage());
        }
        finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected Writer createWriter(String scrTextFileName) throws IOException {
        FileWriter fileWriter = new FileWriter(scrTextFileName);
        return fileWriter;
    }

    public void projectOpened() {
//        PUT_FOCUS_INTO_COMMENT = true;
    }

    public void projectClosed() {
    }

    public String getComponentName() {
        return "TransparentCheckInConfiguration";
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    public static CheckInConfig getInstance(Project project) {
        return project.getComponent(CheckInConfig.class);
    }

    public CheckInEnvironment getCheckInEnvironment() {
        if (env == null) {
            env = new CheckInEnvironment(project, this);
        }
        return env;
    }
}

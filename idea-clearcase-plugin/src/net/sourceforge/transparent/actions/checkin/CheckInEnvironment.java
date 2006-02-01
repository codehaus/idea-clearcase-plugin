package net.sourceforge.transparent.actions.checkin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.localVcs.LocalVcsServices;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcsHelper;
import com.intellij.openapi.vcs.CheckinProjectDialogImplementer;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.RollbackProvider;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.checkin.CheckinEnvironment;
import com.intellij.openapi.vcs.checkin.DifferenceType;
import com.intellij.openapi.vcs.checkin.Revisions;
import com.intellij.openapi.vcs.checkin.RevisionsFactory;
import com.intellij.openapi.vcs.ui.Refreshable;
import com.intellij.openapi.vcs.ui.RefreshableOnComponent;
import com.intellij.openapi.vcs.versions.AbstractRevisions;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.ui.ColumnInfo;
import net.sourceforge.transparent.TransparentVcs;
import org.intellij.plugins.ExcludedFileFilter;

import java.util.ArrayList;
import java.util.List;

public class CheckInEnvironment
        implements CheckinEnvironment {

    public static final Logger LOG = Logger.getInstance("net.sourceforge.transparent.ClearCase");
    private CheckInConfig configuration;
    private CheckinEnvironment lvcsCheckinEnvironment;
    private CheckInOptionsPanel optionsPanel;
    private String comment = "";
    private TransparentVcs transparentVcs;
    private RevisionsFactory revisionsFactory = new FilteringRevisionsFactory();

    private class FilteringRevisionsFactory implements RevisionsFactory {
        public Revisions[] createOn(String[] paths, ProgressIndicator progress) throws VcsException {
            RevisionsFactory factory = lvcsCheckinEnvironment.getRevisionsFactory();
            Revisions[] revisions = factory.createOn(paths, progress);

            List<Revisions> filteredRevisions = new ArrayList<Revisions>(revisions.length);
            ExcludedFileFilter filter = transparentVcs.getFileFilter();

            for (Revisions revs : revisions) {
                if (filter.accept(revs.getVirtualFile())) {
                    filteredRevisions.add(revs);
                }
            }

            return filteredRevisions.toArray(new Revisions[filteredRevisions.size()]);
        }
    }

    public CheckInEnvironment(Project project, CheckInConfig configuration) {
        this.configuration = configuration;
        optionsPanel = new CheckInOptionsPanel();
        transparentVcs = TransparentVcs.getInstance(project);
        LocalVcsServices lvcsServices = LocalVcsServices.getInstance(project);
        if (lvcsServices != null) {
            lvcsCheckinEnvironment = lvcsServices.createCheckinEnvironment(null);
        }
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<VcsException> commit(CheckinProjectDialogImplementer dialog, Project project) {
        String checkinComment = dialog.getPreparedComment(this);
        writeScr();

        List<VcsException> exceptions =
                AbstractVcsHelper.getInstance(project).doCheckinProject(
                        dialog.getCheckinProjectPanel(),
                        checkinComment,
                        transparentVcs);

        return exceptions;
    }

    private void writeScr()
    {
        try
        {
            configuration.lastScr = optionsPanel.getScr();
            configuration.writeScr();
        }
        catch (Exception e)
        {
            LOG.error("Unable to write scr file", e);
        }
    }

    public List<VcsException> commit(FilePath[] roots, Project project,
                                     String preparedComment) {
        throw new UnsupportedOperationException();
    }

    public String getCheckinOperationName() {
        return "CHECKING_OPERATION_NAME";
    }

    public RevisionsFactory getRevisionsFactory() {
        return revisionsFactory;
    }

    public boolean showCheckinDialogInAnyCase() {
        return false;
    }

    public RollbackProvider createRollbackProviderOn(
            AbstractRevisions[] selectedRevisions,
            final boolean containsExcluded) {
        return lvcsCheckinEnvironment.createRollbackProviderOn(selectedRevisions, containsExcluded);
    }

    public DifferenceType[] getAdditionalDifferenceTypes() {
        return lvcsCheckinEnvironment.getAdditionalDifferenceTypes();
    }

    public ColumnInfo[] getAdditionalColumns(int index) {
        return lvcsCheckinEnvironment.getAdditionalColumns(index);
    }

    public RefreshableOnComponent createAdditionalOptionsPanel(Refreshable panel, boolean checkinProject) {
        return getAdditionalOptionsPanel();
    }

    public RefreshableOnComponent createAdditionalOptionsPanelForCheckinProject(Refreshable panel) {
        return null;
    }

    public RefreshableOnComponent createAdditionalOptionsPanelForCheckinFile(Refreshable panel) {
        return null;
    }

    public String getDefaultMessageFor(FilePath filesToCheckin[]) {
        return comment;
    }

    public CheckInOptionsPanel getAdditionalOptionsPanel() {
        return optionsPanel;
    }

    public void onRefreshFinished() {
    }

    public void onRefreshStarted() {
    }

    public AnAction[] getAdditionalActions(int index) {
        return new AnAction[0];
    }

    public String prepareCheckinMessage(String string) {
        return string;
    }

    public String getHelpId() {
        return lvcsCheckinEnvironment.getHelpId();
    }
}

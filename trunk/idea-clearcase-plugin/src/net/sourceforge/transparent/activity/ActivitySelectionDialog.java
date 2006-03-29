package net.sourceforge.transparent.activity;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.jacob.com.ComFailException;
import net.sourceforge.clearcase.comapi.ICCActivities;
import net.sourceforge.clearcase.comapi.ICCActivity;
import net.sourceforge.transparent.NewNativeClearCase;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * @author Gilles Philippart
 */
public class ActivitySelectionDialog extends DialogWrapper {

    private static final Logger LOG = Logger.getInstance("#net.sourceforge.transparent.activity.ActivitySelectionDialog");

    private ActivitySelectionForm form;
    private Project project;
    private NewNativeClearCase cc;
    private String viewRootPath;

    public ActivitySelectionDialog(Project project, NewNativeClearCase cc, String viewRootPath) {
        super(project, false);
        this.project = project;
        this.cc = cc;
        this.viewRootPath = viewRootPath;
        setTitle("Activity selection");
        init();
        doRefreshAction();
    }

    private class RefreshAction extends AbstractAction {

        public RefreshAction() {
            putValue(Action.NAME, "Refresh");
        }

        public void actionPerformed(ActionEvent e) {
            doRefreshAction();
        }
    }

    protected Action[] createLeftSideActions() {
        return new Action[]{new RefreshAction()};
    }

    public void doRefreshAction() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Listing activities...");
        }
        java.util.List<Activity> availableActivities = findActivities();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Done.");
        }
        form.setActivities(availableActivities);
        Activity selectedActivity = null;
        try {
            ICCActivity ccActivity = cc.getViewCurrentActivity(viewRootPath);
            Activity viewCurrentActivity = new Activity(ccActivity);
            int count = availableActivities.size();
            for (int i = 0; i < count; i++) {
                Activity activity = availableActivities.get(i);
                String name = activity.getHeadline();
                if (name.equals(viewCurrentActivity.getHeadline())) {
                    selectedActivity = activity;
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Found selected activity : " + name + ", index =" + selectedActivity);
                    }
                    break;
                }
            }
        } catch (ComFailException e) {
            // this is probably a 'This view has no current activity.' exception
        }
        // searching the current activity in the list
        form.setSelectedActivity(selectedActivity);
    }

    private java.util.List<Activity> findActivities() {
        java.util.List<Activity> newActivities = new ArrayList<Activity>();
        ICCActivities activities = cc.getActivities(this.viewRootPath);
        int count = activities.getCount();
        for (int i = 1; i <= count; i++) {
            ICCActivity item = activities.getItem(i);
            Activity activity = new Activity(item);
            newActivities.add(activity);
        }
        return newActivities;
    }

    protected JComponent createCenterPanel() {
        if (form == null) {
            form = new ActivitySelectionForm();
        }
        JPanel rootPanel = form.getRootPanel();
        rootPanel.setPreferredSize(new Dimension(800, 500));
        return rootPanel;
    }

    protected void doOKAction() {
        Activity activity = form.getSelectedActivity();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling CC with new index = " + activity + ", activity.headline()=" + activity.getHeadline());
        }
        try {
            cc.setViewCurrentActivity(viewRootPath, activity.getClearcaseActivity());
            super.doOKAction();
        } catch (ComFailException e) {
            String message = e.getMessage();
            String lookfor = "Description: ";
            int i = message.indexOf(lookfor) + lookfor.length();
            String desc = StringUtils.substring(message, i);
            showError(desc);
        }
        catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String desc) {
        Messages.showErrorDialog(project, desc, "Activity selection error");
    }

}

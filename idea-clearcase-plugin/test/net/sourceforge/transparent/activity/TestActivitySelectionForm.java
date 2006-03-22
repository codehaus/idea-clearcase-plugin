package net.sourceforge.transparent.activity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gilles Philippart
 */
public class TestActivitySelectionForm {

    public static void main(String[] args) {
        JFrame f = new JFrame();
        List<Activity> activities = findActivities();
        final ActivitySelectionForm activitySelectionForm = new ActivitySelectionForm();
        activitySelectionForm.setActivities(activities);
        f.setContentPane(new JPanel(new BorderLayout()));
        Container p = f.getContentPane();
        p.add(activitySelectionForm.getRootPanel(), BorderLayout.CENTER);
        JButton clearButton = new JButton(new AbstractAction("Clear") {
            public void actionPerformed(ActionEvent actionEvent) {
                activitySelectionForm.setActivities(new ArrayList<Activity>());
            }
        });
        JButton loadButton = new JButton(new AbstractAction("Load") {
            public void actionPerformed(ActionEvent actionEvent) {
                activitySelectionForm.setActivities(findActivities());
            }
        });
        JPanel controlPanel = new JPanel();
        controlPanel.add(clearButton);
        controlPanel.add(loadButton);
        p.add(controlPanel, BorderLayout.SOUTH);
        f.setVisible(true);
        f.setSize(800, 600);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static List<Activity> findActivities() {
        List<Activity> activities = new ArrayList<Activity>();
        activities.add(new FakeActivity("New tests must be created", "Gilles"));
        activities.add(new FakeActivity("Big overhaul", "Cedric"));
        activities.add(new FakeActivity("Performance improvements", "Cedric"));
        return activities;
    }

}

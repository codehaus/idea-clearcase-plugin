package net.sourceforge.transparent.activity;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.UniqueList;
import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventSelectionModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This {@link ca.odell.glazedlists.matchers.MatcherEditor} matches issues if their user is selected.
 *
 * @author <a href="mailto:jesse@odel.on.ca">Jesse Wilson</a>
 */
public class UsersSelect extends AbstractMatcherEditor implements ListSelectionListener {

    /**
     * a list of users
     */
    EventList usersEventList;
    EventList usersSelectedList;
    private JList usersJList;

    /**
     * Create a {@link net.sourceforge.transparent.activity.ActivitiesForUsersMatcher} editor that matches users from the
     * specified {@link ca.odell.glazedlists.EventList} of {@link net.sourceforge.transparent.activity.Activity}s.
     */
    public UsersSelect(EventList source, JList usersJList) {
        // derive the users list from the issues list
        EventList usersNonUnique = new ActivityToUserList(source);
        usersEventList = new UniqueList(usersNonUnique);

        // create a JList that contains users
        EventListModel usersListModel = new EventListModel(usersEventList);
        usersJList.setModel(usersListModel);

        // create an EventList containing the JList's selection
        EventSelectionModel userSelectionModel = new EventSelectionModel(usersEventList);
        usersJList.setSelectionModel(userSelectionModel);
        usersSelectedList = userSelectionModel.getSelected();

        // handle changes to the list's selection
        usersJList.addListSelectionListener(this);
        this.usersJList = usersJList;
    }

    /**
     * When the JList selection changes, create a new Matcher and fire
     * an event.
     */
    public void valueChanged(ListSelectionEvent e) {
        Matcher newMatcher = new ActivitiesForUsersMatcher(usersSelectedList);
        fireChanged(newMatcher);
    }

}

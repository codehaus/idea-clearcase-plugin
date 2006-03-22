package net.sourceforge.transparent.activity;

import ca.odell.glazedlists.matchers.Matcher;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Gilles Philippart
 */
public class ActivitiesForUsersMatcher implements Matcher {

    /**
     * the users to match
     */
    private Set users = new HashSet();

    /**
     * Create a new {@link net.sourceforge.transparent.activity.ActivitiesForUsersMatcher} that matches only
     * {@link net.sourceforge.transparent.activity.Activity}s that have one or more user in the specified list.
     */
    public ActivitiesForUsersMatcher(Collection users) {
        // make a defensive copy of the users
        this.users.addAll(users);
    }

    /**
     * Test whether to include or not include the specified issue based
     * on whether or not their user is selected.
     */
    public boolean matches(Object o) {
        if (o == null) {
            return false;
        }
        if (users.isEmpty()) {
            return true;
        }

        Activity issue = (Activity) o;
        String user = issue.getOwner();
        return users.contains(user);
    }
}

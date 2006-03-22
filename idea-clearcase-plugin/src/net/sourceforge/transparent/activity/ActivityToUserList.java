package net.sourceforge.transparent.activity;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.event.ListEvent;

/**
 * An IssuesToUserList is a list of users that is obtained by getting
 * the reporters from the issues list.
 *
 * @author <a href="mailto:jesse@odel.on.ca">Jesse Wilson</a>
 */
class ActivityToUserList extends TransformedList {

    /**
     * Construct an IssuesToUserList from an EventList that contains only
     * Issue objects.
     */
    public ActivityToUserList(EventList source) {
        super(source);
        source.addListEventListener(this);
    }

    /**
     * Gets the user at the specified index.
     */
    public Object get(int index) {
        Activity activity = (Activity) source.get(index);
        return activity.getOwner();
    }

    /**
     * When the source issues list changes, propogate the exact same changes
     * for the users list.
     */
    public void listChanged(ListEvent listChanges) {
        updates.forwardEvent(listChanges);
    }
}

package net.sourceforge.transparent.activity;

import ca.odell.glazedlists.TextFilterator;

import java.util.List;

/**
 * @author Gilles Philippart
 */
class ActivityTextFilterator implements TextFilterator {

    public void getFilterStrings(List baseList, Object element) {
        Activity activity = (Activity) element;
        baseList.add(activity.getHeadline());
    }
}

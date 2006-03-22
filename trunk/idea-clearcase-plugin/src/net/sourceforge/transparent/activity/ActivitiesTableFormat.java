package net.sourceforge.transparent.activity;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.AdvancedTableFormat;

import java.util.Comparator;

/**
 * @author Gilles Philippart
 */
class ActivitiesTableFormat implements AdvancedTableFormat {

    public Class getColumnClass(int column) {
        if (column == 0) {
            return String.class;
        } else if (column == 1) {
            return String.class;
        }
        throw new IllegalStateException();

    }

    public Comparator getColumnComparator(int column) {
        return GlazedLists.caseInsensitiveComparator();
    }

    public int getColumnCount() {
        return 2;
    }

    public String getColumnName(int column) {
        if (column == 0) {
            return "Headline";
        } else if (column == 1) {
            return "Owner";
        }
        throw new IllegalStateException();
    }

    public Object getColumnValue(Object baseObject, int column) {
        Activity activity = (Activity) baseObject;
        if (column == 0) {
            return activity.getHeadline();
        } else if (column == 1) {
            return activity.getOwner();
        }
        throw new IllegalStateException();

    }

}

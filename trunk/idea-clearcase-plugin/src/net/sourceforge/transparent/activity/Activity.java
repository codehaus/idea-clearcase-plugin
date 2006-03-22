package net.sourceforge.transparent.activity;

import net.sourceforge.clearcase.comapi.ICCActivity;

/**
 * @author Gilles Philippart
 */
public class Activity implements Comparable {

    private ICCActivity activity;
    private String oid;
    private String name;
    private String headline;
    private String owner;
    private String group;

    public Activity(ICCActivity activity) {
        this.activity = activity;
    }

    public String getOID() {
        if (oid == null) {
            oid = activity.getOID();
        }
        return oid;
    }

    public String getName() {
        if (name == null) {
            name = activity.getName();
        }
        return name;
    }

    public String getHeadline() {
        if (headline == null) {
            headline = activity.getHeadline();
        }
        return headline;
    }

    public String getOwner() {
        if (owner == null) {
            owner = activity.getOwner();
        }
        return owner;
    }

    public String getGroup() {
        if (group == null) {
            group = activity.getOwner();
        }
        return group;
    }

    public ICCActivity getClearcaseActivity() {
        return activity;
    }

    public int compareTo(Object o) {
        return getHeadline().compareTo(((Activity) o).getHeadline());
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Activity");
        sb.append("{oid='").append(getOID()).append('\'');
        sb.append(", name='").append(getName()).append('\'');
        sb.append(", headline='").append(getHeadline()).append('\'');
        sb.append(", owner='").append(getOwner()).append('\'');
        sb.append(", group='").append(getGroup()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

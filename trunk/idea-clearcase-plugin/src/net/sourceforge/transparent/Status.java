// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   Status.java

package net.sourceforge.transparent;

public class Status {

    public static final Status CHECKED_OUT = new Status("checked out");
    public static final Status CHECKED_IN = new Status("checked in");
    public static final Status NOT_AN_ELEMENT = new Status("not an element");
    public static final Status HIJACKED = new Status("hijacked");
    private String state;

    private Status(String state) {
        this.state = state;
    }

    public String toString() {
        return state;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Status status = (Status) o;
        if (!state.equals(status.state)) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        return state.hashCode();
    }

}

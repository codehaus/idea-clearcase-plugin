package net.sourceforge.transparent;

import java.io.File;

public interface ClearCase {

    String getName();

    void checkIn(File file, String s);

    void checkOut(File file, boolean isReserved, String comment);

    void undoCheckOut(File file);

    void add(File file, String s);

    void delete(File file, String s);

    void move(File file, File file1, String s);

    Status getStatus(File file);

    boolean isElement(File file);

    boolean isCheckedOut(File file);

    /**
     * Unused.
     *
     * @param file
     */
    CheckedOutStatus getCheckedOutStatus(File file);

    String getCheckoutComment(File file);

    /**
     * Return true if the loaded file is the last version.
     *
     * @param file
     * @return true if the loaded file is the last version
     */
    boolean isLatestVersion(File file);

}

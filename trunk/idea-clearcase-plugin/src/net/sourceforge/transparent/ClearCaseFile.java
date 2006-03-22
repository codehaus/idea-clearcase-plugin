// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   ClearCaseFile.java

package net.sourceforge.transparent;

import java.io.File;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.diagnostic.Logger;
import org.intellij.plugins.util.LogUtil;

// Referenced classes of package net.sourceforge.transparent:
//            ClearCaseException, Status, ClearCase

public class ClearCaseFile {

    public static final Logger LOG = LogUtil.getLogger(ClearCaseFile.class);
    /**
     * @label delegates to
     */
    private ClearCase _clearCase;
    private File _file;

    /**
     * @label is in state
     */
    private Status _status;

    /**
     * @label parent
     */
    private ClearCaseFile _parent;

    public ClearCaseFile(File file, ClearCase clearCase) {
        _clearCase = clearCase;
        _file = file;
        updateStatus();
    }

    public ClearCaseFile(VirtualFile file, ClearCase clearCase) {
        this(new File(file.getPath()), clearCase);
    }

    public ClearCaseFile(VirtualFile parent, String name, ClearCase clearCase) {
        this(new File(parent.getPath(), name), clearCase);
    }

    public File getFile() {
        return _file;
    }

    public String getName() {
        return _file.getName();
    }

    public String getPath() {
        return _file.getPath();
    }

    public ClearCaseFile getParent() {
        if (_parent == null && _file.getParentFile() != null) {
            _parent = new ClearCaseFile(_file.getParentFile(), _clearCase);
        }
        return _parent;
    }

    public boolean exists() {
        return _file.exists();
    }

    public String toString() {
        return _file.getPath();
    }

    public boolean isCheckedOut() {
        return _status == Status.CHECKED_OUT;
    }

    public boolean isCheckedIn() {
        return _status == Status.CHECKED_IN;
    }

    public boolean isElement() {
        return _status != Status.NOT_AN_ELEMENT;
    }

    public boolean isHijacked() {
        return _status == Status.HIJACKED;
    }

    private void updateStatus() {
        _status = _clearCase.getStatus(_file);
    }

    private void assertIsElement() {
        if (!isElement()) {
            throw new ClearCaseException(_file.getAbsoluteFile() + " is not an element");
        } else {
            return;
        }
    }

    private void assertIsCheckedOut() {
        assertIsElement();
        if (!isCheckedOut()) {
            throw new ClearCaseException(_file.getAbsoluteFile() + " is not checked out");
        } else {
            return;
        }
    }

    public void undoCheckOut() {
        assertIsCheckedOut();
        _clearCase.undoCheckOut(_file);
        updateStatus();
    }

    public void checkIn(String comment, boolean useHijacked) {
        if (isHijacked() && useHijacked) {
            checkOut(false, true);
        }
        checkIn(comment);
    }

    public void checkIn(String comment) {
        assertIsElement();
        if (isCheckedOut()) {
//            System.out.println("ClearCaseFile: checking in, comment: " + comment);
            _clearCase.checkIn(_file, comment);
            updateStatus();
        }
    }

    public void checkOut(boolean reserved, boolean useHijacked) {
//        System.out.println("NOOOOOOOOOOOOOOOOOOOOOO!");
        checkOut(reserved, useHijacked, "");
    }

    public void checkOut(boolean reserved, boolean useHijacked, String comment) {
        assertIsElement();
        if (!isCheckedOut()) {
            File newFile = null;
            if (useHijacked) {
                newFile = new File(_file.getParentFile().getAbsolutePath(), _file.getName() + ".hijacked");
                _file.renameTo(newFile);
            }
            _clearCase.checkOut(_file, reserved, comment);
            if (newFile != null) {
                _file.delete();
                newFile.renameTo(_file);
            }
            updateStatus();
        }
    }

    public void add(String comment, boolean reserved) {
        if (getParent() == null) {
            return;
        }

        String parentComment = addToComment(comment, "Added " + _file.getName());

        if ("".equals(comment)) {
            comment = "Initial Checkin";
        }

        ensureParentIsElement(comment);
        getParent().checkOut(reserved, false, "Adding " + _file.getName());
        _clearCase.add(_file, comment);
        _clearCase.checkIn(_file, comment);
        getParent().checkIn(parentComment);
        updateStatus();
    }

    public void delete(String comment, boolean reserved) {
        if (isCheckedOut()) {
            undoCheckOut();
        }
        if (isParentDeleted()) {
            return;
        }

        String deletedText = "Deleted " + _file.getName();
        String parentComment = addToComment(comment, deletedText);

        getParent().checkOut(reserved, false, "Deleting " + _file.getName());
        if ("".equals(comment)) {
            comment = deletedText;
        }
        _clearCase.delete(_file, comment);
        getParent().checkIn(parentComment);
        updateStatus();
    }

    private String addToComment(String comment, String additionalText) {
        String result = comment;
        if (result == null || result.length() == 0) {
            result = additionalText;
        } else {
            result += '\n' + additionalText;
        }

        return result;
    }

    private boolean isParentDeleted() {
        return !getParent().getFile().exists();
    }

    public void move(ClearCaseFile target, String comment, boolean reserved) {
        ensureParentIsElement(comment);
        getParent().checkOut(reserved, false);
        target.getParent().checkOut(reserved, false);
        if ("".equals(comment)) {
            comment = "Moved " + _file.getPath() + " to " + target.getFile().getPath();
        }
        _clearCase.move(_file, target.getFile(), comment);
        getParent().checkIn(comment);
        target.getParent().checkIn(comment);
        updateStatus();
        target.updateStatus();
    }

    public void rename(String newName, String comment, boolean reserved) {
        ensureParentIsElement(comment);
        getParent().checkOut(reserved, false);
        if ("".equals(comment)) {
            comment = "Renamed " + _file.getName() + " to " + newName;
        }
        _clearCase.move(_file, new File(_file.getParentFile(), newName), comment);
        getParent().checkIn(comment);
        updateStatus();
    }

    private void ensureParentIsElement(String comment) {
        if (!getParent().isElement()) {
            LOG.info("Trying to add parent : " + getParent());
            getParent().add(comment, false);
        }
        if (!getParent().isElement()) {
            throw new ClearCaseException("Could not add " + _file.getAbsoluteFile());
        } else {
            return;
        }
    }
}

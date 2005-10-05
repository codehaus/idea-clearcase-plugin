// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   EditorHandler.java

package org.intellij.plugins.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;
import java.util.EventObject;

// Referenced classes of package org.intellij.plugins.ui:
//            EditableRemovableCellEditor

public class EditorHandler
    implements ActionListener, ItemListener, Serializable {

    protected Object value;
    private final EditableRemovableCellEditor editor;

    protected EditorHandler(EditableRemovableCellEditor editor) {
        this.editor = editor;
    }

    public Object getCellEditorValue() {
        return value;
    }

    public void setEditorObjectValue(Object obj) {
        value = obj;
    }

    public boolean isCellEditable(EventObject eventobject) {
        return true;
    }

    public boolean shouldSelectCell(EventObject eventobject) {
        return true;
    }

    public boolean c(EventObject eventobject) {
        return true;
    }

    public boolean stopCellEditing() {
        EditableRemovableCellEditor.fireEditingStopped(editor);
        return true;
    }

    public void cancelCellEditing() {
        EditableRemovableCellEditor.fireEditingCanceled(editor);
    }

    public void actionPerformed(ActionEvent actionevent) {
        editor.stopCellEditing();
    }

    public void itemStateChanged(ItemEvent itemevent) {
        editor.stopCellEditing();
    }
}

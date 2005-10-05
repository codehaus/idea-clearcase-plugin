// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   EditableRemovableCellEditor.java

package org.intellij.plugins.ui;

import java.awt.*;
import java.util.EventObject;

import javax.swing.*;
import javax.swing.table.TableCellEditor;

// Referenced classes of package org.intellij.plugins.ui:
//            EditableRemovableTextField, StringEditorHandler, EditorHandler

public class EditableRemovableCellEditor extends AbstractCellEditor
    implements TableCellEditor {

    protected EditableRemovableTextField editableTextField;
    protected EditorHandler b;

    public EditableRemovableCellEditor() {
        editableTextField = new EditableRemovableTextField();
        b = new StringEditorHandler(this);
        editableTextField.getTextField().addActionListener(b);
    }

    public JButton getEditButton() {
        return editableTextField.getEditButton();
    }

    public JTextField getTextField() {
        return editableTextField.getTextField();
    }

    public Object getCellEditorValue() {
        return b.getCellEditorValue();
    }

    public boolean isCellEditable(EventObject eventobject) {
        return b.isCellEditable(eventobject);
    }

    public boolean shouldSelectCell(EventObject eventobject) {
        return b.shouldSelectCell(eventobject);
    }

    public boolean stopCellEditing() {
        return b.stopCellEditing();
    }

    public void cancelCellEditing() {
        b.cancelCellEditing();
    }

    public Component getTableCellEditorComponent(JTable jtable, Object obj, boolean flag, int i, int j) {
        b.setEditorObjectValue(obj);
        editableTextField.getTextField().setBackground(jtable.getSelectionBackground());
        editableTextField.getButtonPanel().setBackground(jtable.getSelectionBackground());
        return editableTextField;
    }

    static void fireEditingStopped(EditableRemovableCellEditor eb1) {
        eb1.fireEditingStopped();
    }

    static void fireEditingCanceled(EditableRemovableCellEditor eb1) {
        eb1.fireEditingCanceled();
    }
}

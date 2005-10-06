// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   StringEditorHandler.java

package org.intellij.plugins.ui;

// Referenced classes of package org.intellij.plugins.ui:
//            EditorHandler, EditableRemovableCellEditor, EditableRemovableTextField

class StringEditorHandler extends EditorHandler {

    private final EditableRemovableCellEditor editor;

    StringEditorHandler(EditableRemovableCellEditor editor) {
        super(editor);
        this.editor = editor;
    }

    public void setEditorObjectValue(Object obj) {
        editor.editableTextField.getTextField().setText(obj != null ? obj.toString() : "");
    }

    public Object getCellEditorValue() {
        return editor.editableTextField.getTextField().getText();
    }
}

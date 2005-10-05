// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   PathListEditorPanel.java

package org.intellij.plugins.ui.pathlisteditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.plugins.ExcludedPathsFromVcsConfiguration;
import org.intellij.plugins.ui.common.CheckBoxTableCellRenderer;
import org.intellij.plugins.ui.common.SimpleScrollPane;
import org.intellij.plugins.ui.common.SingleRowSelectionTable;
import org.intellij.plugins.util.FileUtil;

// Referenced classes of package org.intellij.plugins.ui.pathlisteditor:
//            EditableListPanel, PathListElement, PathListTableModel, PathListElementTableCellRenderer

public class PathListEditorPanel extends EditableListPanel {

    private ExcludedPathsFromVcsConfiguration configuration;
    private String pathWhereUserLeftOffLastTime;
    private SingleRowSelectionTable table;
    private ArrayList pathListElements;
    private JButton addFileButton;
    private JButton addDirButton;
    private JButton removeButton;
    private static final Logger LOG;
    private FileUtil fileUtil;

    public PathListEditorPanel(String initialSelection, ExcludedPathsFromVcsConfiguration configuration) {
        this(initialSelection, configuration, new FileUtil());
    }

    public PathListEditorPanel(String initialSelection, ExcludedPathsFromVcsConfiguration configuration, FileUtil fileUtil) {
        pathListElements = new ArrayList();
        this.configuration = configuration;
        this.fileUtil = fileUtil;
        pathWhereUserLeftOffLastTime = initialSelection;
        initUI();
    }

    protected String getTitle() {
        return null;
    }

    protected JButton[] getButtons() {
        addDirButton = new JButton("Add Directory...");
        addDirButton.setMnemonic('D');
        addDirButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                addDirectory();
            }

        });
        addFileButton = new JButton("Add File...");
        addFileButton.setMnemonic('F');
        addFileButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                addFile();
            }

        });
        removeButton = new JButton("Remove");
        removeButton.setMnemonic('R');
        removeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                removeSelection();
            }

        });
        return (new JButton[] {
            addDirButton, addFileButton, removeButton
        });
    }

    private void addFile() {
        addPath(true);
    }

    private void addDirectory() {
        addPath(false);
    }

    private void addPath(boolean isFile) {
        int selectedRow = table.getSelectedRow() + 1;
        if (selectedRow < 0) {
            selectedRow = pathListElements.size();
        }
        int savedSelectedRow = selectedRow;
        File selectedFiles[] = askForAdditionalPathsToExclude(isFile);
        VirtualFile chosenPaths[] = convertFilesToVirtualFiles(selectedFiles);
        for (int i = 0; i < chosenPaths.length; i++) {
            VirtualFile chosenPath = chosenPaths[i];
            debug("adding path = " + chosenPath);
            if (isAlreadyAddedToPaths(chosenPath)) {
                continue;
            }
            PathListElement newElt;
            if (isFile) {
                newElt = new PathListElement(chosenPath, false, true);
            } else {
                newElt = new PathListElement(chosenPath, true, false);
            }
            pathListElements.add(selectedRow, newElt);
            selectedRow++;
        }

        if (selectedRow > savedSelectedRow) {
            AbstractTableModel model = (AbstractTableModel)table.getModel();
            model.fireTableRowsInserted(savedSelectedRow, selectedRow - 1);
            table.setRowSelectionInterval(savedSelectedRow, selectedRow - 1);
        }
    }

    private VirtualFile[] convertFilesToVirtualFiles(File selectedFiles[]) {
        VirtualFile chosenPaths[] = new VirtualFile[selectedFiles.length];
        for (int i = 0; i < selectedFiles.length; i++) {
            File selectedFile = selectedFiles[i];
            chosenPaths[i] = fileUtil.ioFileToVirtualFile(selectedFile);
        }

        return chosenPaths;
    }

    private File[] askForAdditionalPathsToExclude(boolean isFile) {
        JFileChooser fileChooser = new JFileChooser(pathWhereUserLeftOffLastTime);
        fileChooser.setMultiSelectionEnabled(true);
        String approveButtonText;
        if (isFile) {
            approveButtonText = "Add File";
            fileChooser.setFileSelectionMode(0);
        } else {
            approveButtonText = "Add Directory";
            fileChooser.setFileSelectionMode(1);
        }
        int result = fileChooser.showDialog(this, approveButtonText);
        File selectedFiles[] = new File[0];
        if (result == 0) {
            selectedFiles = fileChooser.getSelectedFiles();
            if (selectedFiles.length > 0) {
                pathWhereUserLeftOffLastTime = selectedFiles[0].getParent();
            }
        }
        return selectedFiles;
    }

    private boolean isAlreadyAddedToPaths(VirtualFile virtualFile) {
        for (Iterator iterator = pathListElements.iterator(); iterator.hasNext();) {
            PathListElement elt = (PathListElement)iterator.next();
            VirtualFile virtualFileInPaths = elt.getVirtualFile();
            if (virtualFileInPaths != null && virtualFile.equals(virtualFileInPaths)) {
                return true;
            }
        }

        return false;
    }

    private void removeSelection() {
        int rowSelected = table.getSelectedRow();
        if (rowSelected < 0) {
            return;
        }
        if (table.isEditing()) {
            TableCellEditor cellEditor = table.getCellEditor();
            if (cellEditor != null) {
                cellEditor.stopCellEditing();
            }
        }
        pathListElements.remove(rowSelected);
        AbstractTableModel abstracttablemodel = (AbstractTableModel)table.getModel();
        abstracttablemodel.fireTableRowsDeleted(rowSelected, rowSelected);
        if (rowSelected >= pathListElements.size()) {
            rowSelected--;
        }
        if (rowSelected >= 0) {
            table.setRowSelectionInterval(rowSelected, rowSelected);
        }
    }

    protected JComponent getListPanel() {
        String columnLabels[] = {
            "Path", "Recursively"
        };
        PathListTableModel model = new PathListTableModel(this, columnLabels);
        table = new SingleRowSelectionTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(300, table.getRowHeight() * 6));
        table.setDefaultRenderer(java.lang.Boolean.class, new CheckBoxTableCellRenderer());
        table.setDefaultRenderer(java.lang.Object.class, new PathListElementTableCellRenderer());
        table.getColumn(columnLabels[0]).setPreferredWidth(350);
        table.getColumn(columnLabels[1]).setPreferredWidth(140);
        table.getSelectionModel().setSelectionMode(0);
        TableCellEditor editor = table.getDefaultEditor(java.lang.String.class);
        if (editor instanceof DefaultCellEditor) {
            ((DefaultCellEditor)editor).setClickCountToStart(1);
        }
        SimpleScrollPane scrollPane = new SimpleScrollPane(table);
        return scrollPane;
    }

    public void reset() {
        java.util.List paths = getConfiguration().getExcludedPaths();
        pathListElements.clear();
        PathListElement element;
        for (Iterator iterator = paths.iterator(); iterator.hasNext(); pathListElements.add(element.cloneMyself())) {
            element = (PathListElement)iterator.next();
        }

    }

    public void apply() {
        getConfiguration().resetExcludedPaths(pathListElements);
    }

    public boolean isModified() {
        return !pathListElements.equals(getConfiguration().getExcludedPaths());
    }

    private ExcludedPathsFromVcsConfiguration getConfiguration() {
        return configuration;
    }

    public ArrayList getPathListElements() {
        return pathListElements;
    }

    public static TitledBorder createEtchedTitleBorder(String s) {
        return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), s);
    }

    private void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    static  {
        LOG = Logger.getInstance((org.intellij.plugins.ui.pathlisteditor.PathListEditorPanel.class).getName());
    }



}

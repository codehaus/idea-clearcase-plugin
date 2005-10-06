// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   DualListPickList.java

package org.intellij.plugins.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import org.intellij.openapi.OpenApiFacade;

// Referenced classes of package org.intellij.plugins.ui:
//            SimpleListModel, IconUtil

public class DualListPickList extends JPanel {

    private JList leftList;
    private JList rightList;
    public static final Icon LEFT_ARROW = IconUtil.getIcon("/icons/leftArrow.png");
    public static final Icon RIGHT_ARROW = IconUtil.getIcon("/icons/rightArrow.png");

    public DualListPickList(String leftTitle, String rightTitle, java.util.List leftList, java.util.List rightList) {
        super(new GridBagLayout());
        initUI(leftTitle, rightTitle, leftList, rightList);
    }

    private void initUI(String leftTitle, String rightTitle, java.util.List leftList, java.util.List rightList) {
        this.leftList = createJList(leftList);
        this.rightList = createJList(rightList);
        removeElementsOfRightListOffOfLeftList();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.0D;
        c.anchor = 18;
        c.fill = 1;
        JScrollPane leftScrollList = createJScrollPanel(this.leftList);
        add(createTitledPanel(leftTitle, leftScrollList), c);
        c.gridx = 1;
        c.weightx = 0.0D;
        c.anchor = 10;
        add(createTitledPanel(" ", createButtonsPanel()), c);
        c.gridx = 2;
        c.weightx = 0.0D;
        c.anchor = 18;
        JScrollPane rightScrollList = new JScrollPane(this.rightList);
        add(createTitledPanel(rightTitle, rightScrollList), c);
        setupListWidth(leftScrollList, rightScrollList);
    }

    private JScrollPane createJScrollPanel(JComponent component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setHorizontalScrollBarPolicy(30);
        scrollPane.setVerticalScrollBarPolicy(20);
        return scrollPane;
    }

    private void setupListWidth(JScrollPane left, JScrollPane right) {
        int maxWidth = Math.max(right.getViewport().getView().getPreferredSize().width, left.getViewport().getView().getPreferredSize().width);
        maxWidth = Math.max(maxWidth, 120);
        maxWidth = Math.min(maxWidth, 120);
        setWidthOf(right, maxWidth);
        setWidthOf(left, maxWidth);
    }

    private void setWidthOf(JScrollPane scrollPane, int width) {
        Dimension preferredSize = scrollPane.getViewport().getSize();
        preferredSize.width = width;
        preferredSize.height = 40;
        scrollPane.getViewport().setPreferredSize(preferredSize);
        scrollPane.revalidate();
    }

    private JPanel createTitledPanel(String title, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(title), "North");
        panel.add(component, "Center");
        return panel;
    }

    private JList createJList(java.util.List list) {
        JList jList = new JList(new SimpleListModel(list));
        jList.setVisibleRowCount(5);
        return jList;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        JComponent moveLeftButton = createButton("Move selection to the left", LEFT_ARROW, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleMoveLeft();
            }

        });
        buttonPanel.add(moveLeftButton, c);
        JComponent moveRightButton = createButton("Move selection to the right", RIGHT_ARROW, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleMoveRight();
            }

        });
        c.gridy = 1;
        buttonPanel.add(moveRightButton, c);
        return buttonPanel;
    }

    private JComponent createButton(String text, Icon icon, ActionListener actionListener) {
        ActionManager actionManager = OpenApiFacade.getActionManager();
        if (actionManager == null) {
            return createRegularButton(icon, text, actionListener);
        } else {
            return createIDEAButton(text, icon, actionListener);
        }
    }

    private JComponent createRegularButton(final Icon icon, String text, final ActionListener actionListener) {
        JButton jButton = new JButton(icon);
        jButton.setAction(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                actionListener.actionPerformed(e);
            }

        });
        jButton.setToolTipText(text);
        return jButton;
    }

    private JComponent createIDEAButton(final String text, final Icon icon, final ActionListener actionListener) {
        ActionManager actionManager = OpenApiFacade.getActionManager();
        DefaultActionGroup group = new DefaultActionGroup();
        group.add(new AnAction() {

            public void actionPerformed(AnActionEvent e) {
                actionListener.actionPerformed(null);
            }

            public void update(AnActionEvent e) {
                e.getPresentation().setText(text);
                e.getPresentation().setVisible(true);
                e.getPresentation().setEnabled(true);
                e.getPresentation().setIcon(icon);
            }
        });
        return actionManager.createActionToolbar("JUnit Test Configuration", group, true).getComponent();
    }

    public void handleMoveLeft() {
        moveSelectedRows(getRightJList(), getLeftJList());
    }

    public void handleMoveRight() {
        moveSelectedRows(getLeftJList(), getRightJList());
    }

    private void moveSelectedRows(JList fromList, JList toList) {
        Object selectedRows[] = fromList.getSelectedValues();
        ((SimpleListModel)toList.getModel()).addAllElement(selectedRows);
        ((SimpleListModel)fromList.getModel()).removeAllElement(selectedRows);
    }

    public JList getLeftJList() {
        return leftList;
    }

    public JList getRightJList() {
        return rightList;
    }

    public java.util.List getRightList() {
        return ((SimpleListModel)rightList.getModel()).getElements();
    }

    public void setLeftList(java.util.List newList) {
        leftList.setModel(new SimpleListModel(newList));
        removeElementsOfRightListOffOfLeftList();
    }

    public void setRightList(java.util.List newList) {
        rightList.setModel(new SimpleListModel(newList));
        removeElementsOfRightListOffOfLeftList();
    }

    private void removeElementsOfRightListOffOfLeftList() {
        ((SimpleListModel)getLeftJList().getModel()).removeAllElement(getRightList().toArray(new String[0]));
    }

    public static void main(String args[]) {
        String lefts[] = {
            "file1", "file2sdfgflgk alskfjgdalsdkfj laskdjflaskdjflaskdjf ", "file3", "file1", "file2sdfgflgk alskfjgdalsdkfj laskdjflaskdjflaskdjf ", "file3", "file1", "file2sdfgflgk alskfjgdalsdkfj laskdjflaskdjflaskdjf ", "file3", "file1", 
            "file2sdfgflgk alskfjgdalsdkfj laskdjflaskdjflaskdjf ", "file3", "file1", "file2sdfgflgk alskfjgdalsdkfj laskdjflaskdjflaskdjf ", "file3"
        };
        String rights[] = {
            "file1", "file4"
        };
        DualListPickList c = new DualListPickList("Available", "Selected", new ArrayList(Arrays.asList(lefts)), new ArrayList(Arrays.asList(rights)));
        JFrame jFrame = new JFrame("Test");
        jFrame.setSize(800, 700);
        jFrame.getContentPane().add(c);
        jFrame.setVisible(true);
    }

}

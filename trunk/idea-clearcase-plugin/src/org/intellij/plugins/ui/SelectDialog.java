// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   SelectDialog.java

package org.intellij.plugins.ui;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.intellij.plugins.util.LogUtil;

// Referenced classes of package org.intellij.plugins.ui:
//            SimpleListModel

public class SelectDialog extends DialogWrapper {
    private static class MyListCellRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Choice choice = (Choice)value;
            String text = String.valueOf(choice.text);
            if (text.length() > 70) {
                text = text.substring(0, 70) + "...";
            }
            return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
        }

        private MyListCellRenderer() {
        }

    }

    public static interface ChoiceListener {

        public abstract void itemChosen(Object obj);
    }

    public static class Choice {

        public String text;
        public Object value;

        public Choice(String text, Object value) {
            this.text = text;
            this.value = value;
        }
    }


    private static final Logger LOG = LogUtil.getLogger();
    private JList jList;
    private SimpleListModel listModel;
    private String title;
    private ChoiceListener runAfterOk;
    private boolean done;
    private boolean ok;
    private long start;
    private Object defaultChoice;
    private ListCellRenderer myListCellRenderer;

    public SelectDialog(Project project) {
        super(project, false);
        ok = false;
    }

    private void init(String title, Choice defaultChoice, Choice choices[], ChoiceListener runAfterOk, ListCellRenderer listCellRenderer) {
        this.title = title;
        this.runAfterOk = runAfterOk;
        listModel = new SimpleListModel(choices);
        jList = new JList(listModel);
        this.defaultChoice = defaultChoice;
        myListCellRenderer = listCellRenderer;
        setUndecorated(true);
        setModal(runAfterOk != null);
        init();
    }

    public SelectDialog(Project project, String title, String defaultChoice, String choices[], ChoiceListener runAfterOk) {
        super(project, false);
        ok = false;
        Choice newChoices[] = new Choice[choices.length];
        Choice defaultNewChoice = null;
        for (int i = 0; i < choices.length; i++) {
            String choice = choices[i];
            newChoices[i] = new Choice(choice, choice);
            if (choice.equals(defaultChoice)) {
                defaultNewChoice = newChoices[i];
            }
        }

        init(title, defaultNewChoice, newChoices, runAfterOk, new MyListCellRenderer());
    }

    public SelectDialog(Project project, String title, Choice defaultChoice, Choice choices[], ChoiceListener runAfterOk, ListCellRenderer listCellRenderer) {
        super(project, false);
        ok = false;
        init(title, defaultChoice, choices, runAfterOk, listCellRenderer);
    }

    public SelectDialog(Project project, String title, Choice defaultChoice, Choice choices[], ChoiceListener runAfterOk) {
        super(project, false);
        ok = false;
        init(title, defaultChoice, choices, runAfterOk, new MyListCellRenderer());
    }

    public boolean isCancelled() {
        return !ok;
    }

    public boolean isOk() {
        return ok;
    }

    public Object getSelected() {
        return ((Choice)jList.getSelectedValue()).value;
    }

    public JList getList() {
        return jList;
    }

    protected void init() {
        jList.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                dispose();
            }
        });
        jList.setBorder(new EmptyBorder(4, 4, 4, 4));
        jList.setSelectedIndex(0);
        jList.setSelectionMode(0);
        jList.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                long l = System.currentTimeMillis();
                if (l - start <= 10L) {
                    try {
                        Point location = getLocation();
                        (new Robot()).mouseMove((int)location.getX(), (int)location.getY());
                    }
                    catch (AWTException e1) {
                        SelectDialog.LOG.error("Cannot move mouse", e1);
                    }
                }
            }

            public void mousePressed(MouseEvent event) {
                ok = true;
                dispose();
                event.consume();
            }

        });
        jList.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseMoved(MouseEvent e) {
                jList.setSelectedIndex(jList.locationToIndex(e.getPoint()));
            }

        });
        jList.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    ok = true;
                    dispose();
                    e.consume();
                }
            }

        });
        jList.setCellRenderer(myListCellRenderer);
        super.init();
    }

    protected JComponent createNorthPanel() {
        JLabel jLabel = new JLabel(title);
        jLabel.setBackground(new Color(214, 214, 214));
        JPanel p = new JPanel(new FlowLayout(1, 1, 1));
        p.add(jLabel);
        return p;
    }

    protected JComponent createCenterPanel() {
        return jList;
    }

    protected Border createContentPaneBorder() {
        return new BevelBorder(1, Color.DARK_GRAY, Color.GRAY, Color.WHITE, new Color(216, 216, 216));
    }

    protected Action[] createActions() {
        return new Action[0];
    }

    public void show() {
        jList.setSelectedValue(defaultChoice, true);
        start = System.currentTimeMillis();
        super.show();
    }

    protected void dispose() {
        super.dispose();
        if (!isCancelled() && !done) {
            done = true;
            runAfterOk.itemChosen(getSelected());
        }
    }





}

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   CheckInOptionsPanel.java

package net.sourceforge.transparent.actions.checkin;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.*;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.ui.RefreshableOnComponent;

public class CheckInOptionsPanel extends JPanel
    implements RefreshableOnComponent {

    protected JTextField scrField;
    protected Project project;

    public CheckInOptionsPanel() {
        scrField = new JTextField();
        setLayout(new BorderLayout());
        add(new Label("SCR Number"), "North");
        add(getScrField(), "Center");
        scrField.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                scrField.selectAll();
            }

            public void focusLost(FocusEvent focusevent) {
            }
        });
    }

    public JTextField getScrField() {
        return scrField;
    }

    public void setScrField(JTextField scrField) {
        this.scrField = scrField;
    }

    public void setScr(String scr) {
        getScrField().setText(scr);
    }

    public String getScr() {
        return getScrField().getText();
    }

    public void setShowScrField(boolean showField) {
        setVisible(showField);
    }

    public boolean isShowScrField() {
        return isVisible();
    }

    public JComponent getComponent() {
        return this;
    }

    public void refresh() {
    }

    public void saveState() {
    }

    public void restoreState() {
    }
}

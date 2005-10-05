package net.sourceforge.transparent.actions;

/*
 * Copyright (c) 2004 JetBrains s.r.o. All  Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * -Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduct the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the distribution.
 *
 * Neither the name of JetBrains or IntelliJ IDEA
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. JETBRAINS AND ITS LICENSORS SHALL NOT
 * BE LIABLE FOR ANY DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT
 * OF OR RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL JETBRAINS OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN
 * IF JETBRAINS HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 */

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputException;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.VcsConfiguration;
import com.intellij.openapi.vcs.checkin.CheckinEnvironment;
import com.intellij.openapi.vcs.ui.Refreshable;
import com.intellij.openapi.vcs.ui.RefreshableOnComponent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.OptionsDialog;
import net.sourceforge.transparent.TransparentConfiguration;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class CheckoutDialog
        extends OptionsDialog
        implements Refreshable {

    private final String              myLabel;
    private final JTextArea           myCommentArea = new JTextArea();
    private final VcsConfiguration    myConfiguration;
    protected Collection<Refreshable> myAdditionalComponents = new ArrayList<Refreshable>();
    private TransparentConfiguration  myTransparentConfig;
    private final CheckinEnvironment  myEnvironment;

    private boolean myToBeShown = true;


    public CheckoutDialog(Project project, TransparentConfiguration transparentConfig, VcsConfiguration configuration, VirtualFile fileToCheckin,
                          CheckinEnvironment env) {
        super(project);
        myEnvironment = env;
        myTransparentConfig = transparentConfig;
        myConfiguration = configuration;
        myCommentArea.setText(getInitialMessage(fileToCheckin));
        myLabel = fileToCheckin.getPresentableUrl();
        setTitle("Checkout Comment");
        init();
    }

    private String getInitialMessage(VirtualFile fileToCheckin) {
        CheckinEnvironment environment = myEnvironment;
        VcsConfiguration configuration = myConfiguration;

        return "";
//        return getInitialMessage(environment, fileToCheckin, configuration);

    }

    protected boolean isToBeShown() {
      return myToBeShown;
    }

    protected void setToBeShown(boolean value, boolean onOk) {
        myToBeShown = value;
        myTransparentConfig.checkoutComment = myToBeShown;
    }

    protected JComponent createCenterPanel() {
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());

      JPanel commentArea = new JPanel(new BorderLayout());
      commentArea.add(new JLabel(myLabel), BorderLayout.NORTH);
      commentArea.add(new JScrollPane(getCommentArea()), BorderLayout.CENTER);

      panel.add(commentArea, BorderLayout.CENTER);

      return panel;
    }

    private JPanel createAdditionalPanel() {
      JPanel result = new JPanel(new BorderLayout());

      RefreshableOnComponent additionalOptionsPanel = myEnvironment.createAdditionalOptionsPanelForCheckinFile(this);
      if (additionalOptionsPanel != null) {
        result.add(additionalOptionsPanel.getComponent(), BorderLayout.NORTH);
        myAdditionalComponents.add(additionalOptionsPanel);
      }

      RefreshableOnComponent specificAdditionalOptions = myEnvironment.createAdditionalOptionsPanel(this, false);
      if (specificAdditionalOptions != null) {
        result.add(specificAdditionalOptions.getComponent(), BorderLayout.SOUTH);
        myAdditionalComponents.add(specificAdditionalOptions);
      }

      return result;
    }

//    public static String getInitialMessage(CheckinEnvironment environment,
//                                           VirtualFile fileToCheckin,
//                                           VcsConfiguration configuration) {
//
//        String defaultMessage = (environment.getDefaultMessageFor(new FilePath[] { fileToCheckin }));
//        if (defaultMessage != null) {
//            return defaultMessage;
//        }
//
//        if (!configuration.SAVE_LAST_COMMIT_MESSAGE) {
//            return "";
//        }
//        return configuration.LAST_COMMIT_MESSAGE;
//    }
//
//    public static String getInitialMessage(VcsConfiguration configuration) {
////                                           Project project) {
//        return (getInitialMessage(((com.intellij.openapi.vcs.checkin.CheckinEnvironment) ((com.intellij.openapi.localVcs.LocalVcsServices) LocalVcsServices.getInstance(project)).createCheckinEnvironment()), null, configuration));
//    }

    public JComponent getPreferredFocusedComponent() {
        if (myConfiguration.PUT_FOCUS_INTO_COMMENT) {
            return myCommentArea;
        }
        return null;
    }

    public String getPreparedComment() {
        return myEnvironment.prepareCheckinMessage(getComment());
    }

    public String getComment() {
        return myCommentArea.getText().trim();
    }

    protected void doOKAction() {
        if (myConfiguration.FORCE_NON_EMPTY_COMMENT && getComment().length() == 0) {
            int requestForCheckin = Messages.showYesNoDialog("Check out with empty comment?",
                                                             "Comment Is Empty",
                                                             Messages.getWarningIcon());
            if (requestForCheckin != OK_EXIT_CODE) {
                return;
            }
        }

//        myConfiguration.LAST_COMMIT_MESSAGE = (getComment());
        try {
            saveState();
            super.doOKAction();
        } catch (InputException ex) {
            ex.show();
        }
    }

    protected JTextArea getCommentArea() {
        initCommentArea();
        return myCommentArea;
    }

    protected VcsConfiguration getConfiguration() {
        return myConfiguration;
    }

    protected void initCommentArea() {
        myCommentArea.setRows(3);
        myCommentArea.setWrapStyleWord(true);
        myCommentArea.setLineWrap(true);
        myCommentArea.setSelectionStart(0);
        myCommentArea.setSelectionEnd(myCommentArea.getText().length());
    }

    public void refresh() {
        for (Refreshable component : myAdditionalComponents) {
            component.refresh();
        }
    }

    public void saveState() {
        for (Refreshable component : myAdditionalComponents) {
            component.saveState();
        }
    }

    public void restoreState() {
        for (Refreshable component : myAdditionalComponents) {
            component.restoreState();
        }
    }

    public void show() {
        refresh();
        super.show();
    }

    protected boolean shouldSaveOptionsOnCancel() {
        return false;
    }
}
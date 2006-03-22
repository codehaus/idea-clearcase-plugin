// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   TransparentConfigurable.java

package net.sourceforge.transparent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import net.sourceforge.transparent.actions.checkin.CheckInConfig;
import org.intellij.plugins.ExcludedPathsFromVcsConfiguration;
import org.intellij.plugins.ui.pathlisteditor.PathListEditorPanel;
import org.intellij.plugins.util.JPanelBuilder;

// Referenced classes of package net.sourceforge.transparent:
//            TransparentConfiguration

public class TransparentConfigurable
    implements ProjectComponent, Configurable {

    private static final Logger LOG = Logger.getInstance("net.sourceforge.transparent.TransparentConfigurable");
    private JCheckBox markExternalChangesAsUpToDateCheckBox;
    private JComponent panel;
    private JComboBox clearcaseImpl;
    private JTextField root;
    private JTextField scrTextFileName;
    private JCheckBox reservedCheckout;
    private JCheckBox automaticCheckout;
    private JCheckBox checkoutComment;
    private JCheckBox checkInUseHijack;
    private JCheckBox offline;
    private PathListEditorPanel excludePanel;
    private Project project;
    private CheckInConfig checkInConfig;
    private TransparentConfiguration transparentConfig;

    public TransparentConfigurable(Project project) {
        this.project = project;
        init();
    }

    public void projectOpened() {
    }

    public void projectClosed() {
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    public String getComponentName() {
        return "TransparentConfigurable";
    }

    public String getDisplayName() {
        return "ClearCase Options";
    }

    public Icon getIcon() {
        return null;
    }

    public String getHelpTopic() {
        return null;
    }

    public void disposeUIResources() {
    }

    public JComponent createComponent() {
        return panel;
    }

    public void init() {
        transparentConfig = TransparentConfiguration.getInstance(project);
        checkInConfig = CheckInConfig.getInstance(project);
        initUI();
    }

    private void initUI() {
        JPanelBuilder optionsBuilder = new JPanelBuilder();
        optionsBuilder.add(createCheckboxesPanel());
        optionsBuilder.add(createFieldsPanel());
        optionsBuilder.add(createExcludePanel());
        panel = optionsBuilder.getPanel();
    }

    private JPanel createCheckboxesPanel() {
        JPanelBuilder checkboxesBuilder = new JPanelBuilder();
        offline = new JCheckBox("Work offline (hijack on edit instead of check out)");
        offline.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                resetCheckInOutCheckboxes();
            }

        });
        checkboxesBuilder.add(offline, new JLabel());
        reservedCheckout = new JCheckBox("Reserved checkouts");
        checkboxesBuilder.add(reservedCheckout, new JLabel());
        automaticCheckout = new JCheckBox(getAutomaticCheckoutCheckBoxLabel());
        checkboxesBuilder.add(automaticCheckout, new JLabel());
        checkoutComment = new JCheckBox("Prompt for comment on checkout");
        checkboxesBuilder.add(checkoutComment, new JLabel());
        checkInUseHijack = new JCheckBox("Check in use hijacks (check out automatically hijacked file on check in)");
        checkboxesBuilder.add(checkInUseHijack, new JLabel());
        return checkboxesBuilder.getPanel();
    }

    private String getAutomaticCheckoutCheckBoxLabel() {
        return "Automatic " + (offline.isSelected() ? "hijacks" : "checkouts") + " on edit without asking";
    }

    private void resetCheckInOutCheckboxes() {
        boolean isOffline = offline.isSelected();
        reservedCheckout.setEnabled(!isOffline);
        automaticCheckout.setText(getAutomaticCheckoutCheckBoxLabel());
        checkInUseHijack.setEnabled(!isOffline);
        if (isOffline) {
            checkInUseHijack.setSelected(true);
        }
    }

    private JPanel createFieldsPanel() {
        JPanelBuilder fieldsBuilder = new JPanelBuilder();
        clearcaseImpl = createImplementationComboBox();
        fieldsBuilder.add(new JLabel("Implementation"), clearcaseImpl);
        root = new JTextField();
        fieldsBuilder.add(new JLabel("ClearCase root"), root);
        scrTextFileName = new JTextField();
        fieldsBuilder.add(new JLabel("SCR text file"), scrTextFileName);
        return fieldsBuilder.getPanel();
    }

    private JPanel createExcludePanel() {
        JPanelBuilder builder = new JPanelBuilder();
        excludePanel = new PathListEditorPanel(getClearCaseDefaultRoot(), ExcludedPathsFromVcsConfiguration.getInstance(project));
        builder.add(excludePanel);
        markExternalChangesAsUpToDateCheckBox = new JCheckBox("Exclude changes made outside IDEA");
        builder.add(markExternalChangesAsUpToDateCheckBox, new JLabel());
        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createCompoundBorder(createEtchedTitleBorder("Exclude from VCS"), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        return panel;
    }

    public static TitledBorder createEtchedTitleBorder(String s) {
        return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), s);
    }

    protected JComboBox createImplementationComboBox() {
        JComboBox clearcaseImpl = new JComboBox();
        String implementations[] = transparentConfig.getAvailableImplementations();
        for (String implementation : implementations) {
            clearcaseImpl.addItem(implementation);
        }

        final ListCellRenderer _oldRenderer = clearcaseImpl.getRenderer();
        clearcaseImpl.setRenderer(new ListCellRenderer() {

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String name = (String)value;
                name = name.substring(name.lastIndexOf('.') + 1);
                return _oldRenderer.getListCellRendererComponent(list, name, index, isSelected, cellHasFocus);
            }

        });
        clearcaseImpl.setPreferredSize(clearcaseImpl.getPreferredSize());
        return clearcaseImpl;
    }

    public void reset() {
        createComponent();
        clearcaseImpl.setSelectedItem(transparentConfig.implementation);
        if (transparentConfig.clearcaseRoot.equals("")) {
            root.setText(getClearCaseDefaultRoot());
        } else {
            root.setText(transparentConfig.clearcaseRoot);
        }
        scrTextFileName.setText(checkInConfig.scrTextFileName);
        reservedCheckout.setSelected(transparentConfig.checkoutReserved);
        automaticCheckout.setSelected(transparentConfig.automaticCheckout);
        checkoutComment.setSelected(transparentConfig.checkoutComment);
        checkInUseHijack.setSelected(transparentConfig.checkInUseHijack);
        offline.setSelected(transparentConfig.offline);
        resetCheckInOutCheckboxes();
        markExternalChangesAsUpToDateCheckBox.setSelected(transparentConfig.markExternalChangeAsUpToDate);
        excludePanel.reset();
    }

    protected String getClearCaseDefaultRoot() {
        ProjectRootManager prjRootMgr = ProjectRootManager.getInstance(project);
        VirtualFile roots[] = prjRootMgr.getContentRoots();
        String ccRoot = "";
        if (roots.length > 0) {
            ccRoot = roots[0].getPresentableUrl();
        }
        return ccRoot;
    }

    public void apply() throws ConfigurationException {
        applyChangesToConfiguration();
        checkInConfig.scrTextFileName = scrTextFileName.getText();
        excludePanel.apply();
    }

    public void applyChangesToConfiguration() {
        transparentConfig.implementation = (String)clearcaseImpl.getSelectedItem();
        transparentConfig.clearcaseRoot = root.getText();
        transparentConfig.checkoutReserved = reservedCheckout.isSelected();
        transparentConfig.automaticCheckout = automaticCheckout.isSelected();
        transparentConfig.checkoutComment = checkoutComment.isSelected();
        transparentConfig.checkInUseHijack = checkInUseHijack.isSelected();
        transparentConfig.offline = offline.isSelected();
        transparentConfig.markExternalChangeAsUpToDate = markExternalChangesAsUpToDateCheckBox.isSelected();
        transparentConfig.notifyListenersOfChange();
    }

    public boolean isModified() {
        return hasImplementationChanged()
                || hasClearcaseRootChanged()
                || hasScrTextFileNameChanged()
                || transparentConfig.checkoutReserved != reservedCheckout.isSelected() 
                || transparentConfig.automaticCheckout != automaticCheckout.isSelected()
                || transparentConfig.checkoutComment != checkoutComment.isSelected()
                || transparentConfig.checkInUseHijack != checkInUseHijack.isSelected()
                || transparentConfig.offline != offline.isSelected()
                || transparentConfig.markExternalChangeAsUpToDate != markExternalChangesAsUpToDateCheckBox.isSelected()
                || excludePanel.isModified();
    }

    private boolean hasClearcaseRootChanged() {
        return transparentConfig.clearcaseRoot == null || !transparentConfig.clearcaseRoot.equals(root.getText());
    }

    private boolean hasScrTextFileNameChanged() {
        return checkInConfig.scrTextFileName == null || !checkInConfig.scrTextFileName.equals(scrTextFileName.getText());
    }

    private boolean hasImplementationChanged() {
        return transparentConfig.implementation == null || !transparentConfig.implementation.equals(clearcaseImpl.getSelectedItem());
    }
    
    protected void setClearcaseImpl(JComboBox clearcaseImpl) {
        this.clearcaseImpl = clearcaseImpl;
    }


}

package net.sourceforge.transparent;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import org.intellij.plugins.ListenerNotifier;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;

/**
 * this is the persistent state of the transparent plugin - just anything that needs to be persisted as a field
 *
 * @stereotype singleton
 */
public class TransparentConfiguration implements ListenerNotifier, JDOMExternalizable, ProjectComponent {

    private static final String MARK_EXTERNAL_CHANGES_AS_UP_TO_DATE_FIELD = "MARK_EXTERNAL_CHANGES_AS_UP_TO_DATE";

    // configuration fields
    public String implementation = CommandLineClearCase.class.getName();
    public String clearcaseRoot = "";
    public boolean checkoutReserved;
    public boolean automaticCheckout;
    public boolean checkoutComment;
    public boolean markExternalChangeAsUpToDate = true;
    public boolean checkInUseHijack = true;
    public boolean offline;
    public String updateOnCheckoutOption;

    // other fields
    private PropertyChangeSupport listenerSupport = new PropertyChangeSupport(this);
    private Field markExternalChangesAsUpToDateField;
    private BaseComponent lvcsConfiguration;
    private Project project;

    public void readExternal(Element element) throws InvalidDataException {
        System.out.println("TransparentConfiguration.readExternal : " + this);
        DefaultJDOMExternalizer.readExternal(this, element);
    }

    public void writeExternal(Element element) throws WriteExternalException {
        System.out.println("TransparentConfiguration.writeExternal : " + this);
        DefaultJDOMExternalizer.writeExternal(this, element);
    }

    public TransparentConfiguration(Project project) {
        this.project = project;
    }

    public void projectOpened() {
        LOG.debug("projectOpened");
    }

    public void projectClosed() {
    }

    public void initComponent() {
        logConfig();
        initExternalChangesAreUpToDateField();
    }

    private void logConfig() {
        LOG.debug("##### Loading " + TransparentVcs.class.getName() + " version " + new Version().getVersion() + "###########");
        LOG.debug("#####    implementation        = " + implementation);
        LOG.debug("#####    clearcaseRoot         = " + clearcaseRoot);
        LOG.debug("#####    checkoutReserved      = " + checkoutReserved);
        LOG.debug("#####    automaticCheckout     = " + automaticCheckout);
        LOG.debug("#####    externalChangeUpToDate= " + markExternalChangeAsUpToDate);
        LOG.debug("#####    checkInUseHijack      = " + checkInUseHijack);
        LOG.debug("#####    offline               = " + offline);
        LOG.debug("#####    updateOnCheckoutOption      = " + updateOnCheckoutOption);
    }

    private void initExternalChangesAreUpToDateField() {
        if (project == null || !isAriadna()) {
            return;
        }
        lvcsConfiguration = getLvcsConfiguration(project);
        if (lvcsConfiguration != null) {
            markExternalChangesAsUpToDateField = getMarkExternalChangesAsUpToDateField(lvcsConfiguration);
            resetLcvsConfiguration();
        } else {
            LOG.debug("Found no LvcsConfiguration. MarkExternalChangesAsUpToDate won't work");
        }
    }

    private boolean isAriadna() {
        ApplicationInfo info = ApplicationInfo.getInstance();
        LOG.debug(
            "##### IDEA Used = " +
                info.getVersionName() +
                " " +
                info.getMajorVersion() +
                "." +
                info.getMinorVersion() +
                " " +
                info.getBuildNumber());
        return info.getVersionName().equals("Ariadna");
    }

    public void disposeComponent() {
    }

    public String getComponentName() {
        return "TransparentConfiguration";
    }

    public static TransparentConfiguration getInstance(Project project) {
        return project.getComponent(TransparentConfiguration.class);
    }

    public PropertyChangeListener[] getListeners() {
        return listenerSupport.getPropertyChangeListeners();
    }

    public void addListener(PropertyChangeListener listener) {
        listenerSupport.addPropertyChangeListener(listener);
    }

    public void notifyListenersOfChange() {
        logConfig();
        listenerSupport.firePropertyChange("configuration", null, this);
        resetLcvsConfiguration();
    }

    public void removeListener(PropertyChangeListener listener) {
        listenerSupport.removePropertyChangeListener(listener);
    }

    /**
     * Returns the LvcsConfiguration.
     *
     * @param project the project
     * @return The base component
     */
    @Nullable
    public static BaseComponent getLvcsConfiguration(Project project) {
        Object[] components = project.getComponents(Object.class);
        if (components == null) {
            return null;
        }
        for (Object c : components) {
            if (BaseComponent.class.isAssignableFrom(c.getClass())) {
                BaseComponent bc = (BaseComponent) c;
                if (bc.getComponentName().equals("LvcsConfiguration") ||
                    bc.getComponentName().equals("LvcsProjectConfiguration")) {
                    return bc;
                }
            }
        }
        LOG.debug("Could not find LvcsConfiguration");
        return null;
    }

    public static Field getMarkExternalChangesAsUpToDateField(BaseComponent lvcsConfiguration) {
        try {
            return lvcsConfiguration.getClass().getField(MARK_EXTERNAL_CHANGES_AS_UP_TO_DATE_FIELD);
        } catch (NoSuchFieldException e) {
            LOG.debug("Could not find field " + MARK_EXTERNAL_CHANGES_AS_UP_TO_DATE_FIELD);
        } catch (SecurityException e) {
            LOG.debug("Could not access field " + MARK_EXTERNAL_CHANGES_AS_UP_TO_DATE_FIELD);
        }
        return null;
    }

    private void resetLcvsConfiguration() {
        if (markExternalChangesAsUpToDateField != null) {
            try {
                markExternalChangesAsUpToDateField.setBoolean(lvcsConfiguration, markExternalChangeAsUpToDate);
            } catch (SecurityException e) {
                LOG.debug(e);
            } catch (IllegalAccessException e) {
                LOG.debug(e);
            }
        }
    }

    private static final Logger LOG = Logger.getInstance("net.sourceforge.transparent.TransparentConfiguration");

    public String[] getAvailableImplementations() {
        String[] implementations = {
//         NewCommandLineClearCase.class.getName(),
            CommandLineClearCase.class.getName(),
            "net.sourceforge.transparent.NativeClearCase",
            "net.sourceforge.transparent.NewNativeClearCase",
            "net.sourceforge.transparent.NewDummyClearCase",
            TestClearCase.class.getName()
        };
        return implementations;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("TransparentConfiguration");
        sb.append("{updateOnCheckoutOption='").append(updateOnCheckoutOption).append('\'');
        sb.append(", implementation='").append(implementation).append('\'');
        sb.append(", clearcaseRoot='").append(clearcaseRoot).append('\'');
        sb.append(", checkoutReserved=").append(checkoutReserved);
        sb.append(", automaticCheckout=").append(automaticCheckout);
        sb.append(", checkoutComment=").append(checkoutComment);
        sb.append(", markExternalChangeAsUpToDate=").append(markExternalChangeAsUpToDate);
        sb.append(", checkInUseHijack=").append(checkInUseHijack);
        sb.append(", offline=").append(offline);
        sb.append('}');
        return sb.toString();
    }
}

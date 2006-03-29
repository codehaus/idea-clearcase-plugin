package net.sourceforge.transparent;

import com.jacob.com.Variant;
import net.sourceforge.clearcase.comapi.*;
import net.sourceforge.clearcase.simple.ClearcaseException;
import net.sourceforge.clearcase.simple.ClearcaseFactory;
import net.sourceforge.clearcase.simple.IClearcase;

import java.io.File;
import java.lang.reflect.Field;

public class NewNativeClearCase extends AbstractClearCase {

    public NewNativeClearCase() throws ClearcaseException {
        super(ClearcaseFactory.JNI);
    }

    protected Application getClearCase() {
        Class ccClass = cc.getClass();
        Application ccase = null;
        try {
            Field f = ccClass.getDeclaredField("ccase");
            f.setAccessible(true); //Make the variable accessible
            ccase = (Application) f.get(ccClass);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return ccase;
    }

    public ICCActivities getActivities(String viewRootPath) {
        Application ccase = getClearCase();
        ICCView viewRoot = ccase.getView(viewRootPath);
        return viewRoot.getStream().getActivities();
    }

    public ICCActivity getViewCurrentActivity(String viewRootPath) {
        Application ccase = getClearCase();
        ICCView viewRoot = ccase.getView(viewRootPath);
        return viewRoot.getCurrentActivity();
    }

    public void setViewCurrentActivity(String viewRootPath, ICCActivity activity) {
        Application ccase = getClearCase();
        ICCView viewRoot = ccase.getView(viewRootPath);
        viewRoot.setActivity(activity);
    }

    public ICCActivity getVersionActivity(String filePath) {
        Application ccase = getClearCase();
        ICCVersion version = ccase.getVersion(new Variant(filePath));
        return ccase.getActivityOfVersion(version);
    }

    public boolean isLatestVersion(File file) {
        Application ccase = getClearCase();
        return ccase.getVersion(new Variant(file.getPath())).getIsLatest();
    }

}

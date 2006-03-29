package net.sourceforge.transparent;

import com.jacob.com.Variant;
import junit.framework.TestCase;
import net.sourceforge.clearcase.comapi.*;
import net.sourceforge.clearcase.simple.ClearcaseJNI;

import java.lang.reflect.Field;

/**
 * @author Gilles Philippart
 */
public class TestActivity extends TestCase {

    private Application ccase;
    private String user;
    private String viewPath;

    protected void setUp() throws Exception {
        super.setUp();
        ClearcaseJNI clearcaseJNI = new ClearcaseJNI();
        Class klass = clearcaseJNI.getClass();
        Field f = klass.getDeclaredField("ccase");
        f.setAccessible(true); //Make the variable accessible
        ccase = (Application) f.get(klass);
        user = "EUR\\gphilip";
        viewPath = "C:/USERS/projects/isl_prd_2r2_lt1_intp0_snap";
    }

    public void testOldVersionCheckout() throws NoSuchFieldException, IllegalAccessException {
        assertFalse("version is NOT latest", isLatest(viewPath + "/product/.classpath"));
        assertTrue("version is latest", isLatest(viewPath + "/product/.project"));

    }

    private boolean isLatest(String file) {
        ICCVersion version = ccase.getVersion(new Variant(file));
        boolean latest = version.getIsLatest();
        System.out.println("latest (" + file + ")=" + latest);
        return latest;
    }

    public void testActivity() throws NoSuchFieldException, IllegalAccessException {
        String file = viewPath + "/product/test/src/idea/product/backend/transformer/clp-toEprom-279593.xml";
        ICCView view = ccase.getView(viewPath);
        ICCStream stream = view.getStream();
        ICCActivities activities = stream.getActivities();
        int count = activities.getCount();
        System.out.println("Stream " + stream.getTitle() + " has " + count + " activities");
        for (int i = 1; i <= count; i++) {
            ICCActivity activity = activities.getItem(i);
            String owner = activity.getOwner();
            if (owner.equals(user)) {
                String name = activity.getHeadline();
                System.out.println("activity# " + i + " : " + name);
            }
        }
        ICCVersion version = ccase.getVersion(new Variant(file));
        System.out.println("Current version of file " + file + " : " + version.getIdentifier());
        ICCActivity activityOfVersion = ccase.getActivityOfVersion(version);
        System.out.println("Activity of this version = " + activityOfVersion.getName());
        System.out.println("view tag name : " + view.getTagName());
        assertNotNull(version);
        ICCView viewForFile = ccase.getView(file);
        System.out.println("viewForFile : " + viewForFile);
        System.out.println("viewForFile.host : " + viewForFile.getHost());
        assertNotNull(viewForFile);

    }

}

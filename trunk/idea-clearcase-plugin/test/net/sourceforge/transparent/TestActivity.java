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

    public void testActivity() {
        ClearcaseJNI clearcaseJNI = new ClearcaseJNI();
        Class klass = clearcaseJNI.getClass();
        Application ccase = null;
        try {
            Field f = klass.getDeclaredField("ccase");
            f.setAccessible(true); //Make the variable accessible
            ccase = (Application) f.get(klass);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        String user = "PIM\\gphilip";
        String viewRootPath = "C:/USERS/projects/isl_prd_2r2_lt1_intp0";
        String file = viewRootPath + "/product/test/src/idea/product/backend/transformer/clp-toEprom-279593.xml";
        ICCView viewRoot = ccase.getView(viewRootPath);
        ICCActivities activities = viewRoot.getStream().getActivities();
        int count = activities.getCount();
        System.out.println("Stream has = " + count + " activities");
        for (int i = 1; i <= count; i++) {
            ICCActivity item = activities.getItem(i);
            String owner = item.getOwner();
//            System.out.println("owner = " + owner);
            if (owner.equals(user)) {
                String name = item.getName();
                System.out.println("name = " + name);
            }
        }
        ICCVersion version = ccase.getVersion(new Variant(file));
        System.out.println("version = " + version);
        ICCActivity activityOfVersion = ccase.getActivityOfVersion(version);
        System.out.println("activityOfVersion = " + activityOfVersion.getName());
        System.out.println("viewRoot.getTagName = " + viewRoot.getTagName());
        assertNotNull(version);
        ICCView view = ccase.getView(file);
        System.out.println("view = " + view);
        System.out.println("view.host = " + view.getHost());
        assertNotNull(view);
    }

}

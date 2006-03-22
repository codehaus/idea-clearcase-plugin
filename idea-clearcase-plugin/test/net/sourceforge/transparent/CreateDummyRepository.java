package net.sourceforge.transparent;

import net.sourceforge.clearcase.simple.ClearcaseDummy;
import net.sourceforge.clearcase.simple.ClearcaseException;
import net.sourceforge.clearcase.simple.ClearcaseFactory;
import net.sourceforge.clearcase.simple.IClearcase;

/**
 * @author Gilles Philippart
 */
public class CreateDummyRepository {

    public static void main(String[] args) throws ClearcaseException {
        IClearcase instance = ClearcaseFactory.getInstance().createInstance(ClearcaseFactory.DUMMY);
        if (instance instanceof ClearcaseDummy) {
            ClearcaseDummy clearcaseDummy = (ClearcaseDummy) instance;
            String file = "C:\\USERS\\PersonalProjects\\sandbox\\main-module\\src";
            clearcaseDummy.add(file, "added src", true, false);
            clearcaseDummy.destroy();
            System.out.println("Added " + file);
        }

    }

}

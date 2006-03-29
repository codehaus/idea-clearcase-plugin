package net.sourceforge.transparent;

import net.sourceforge.clearcase.simple.ClearcaseException;
import net.sourceforge.clearcase.simple.ClearcaseFactory;

import java.io.File;

/**
 * @author Gilles Philippart
 */
public class NewDummyClearCase extends AbstractClearCase {

    public NewDummyClearCase() throws ClearcaseException {
        super(ClearcaseFactory.DUMMY);
    }

    /**
     * Don't know how to query with cleartool this information
     *
     * @param file
     * @return
     */
    public boolean isLatestVersion(File file) {
        return true;
    }

}

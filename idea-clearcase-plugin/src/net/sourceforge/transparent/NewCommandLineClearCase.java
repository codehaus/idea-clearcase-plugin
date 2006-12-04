package net.sourceforge.transparent;

import net.sourceforge.clearcase.simple.ClearcaseException;
import net.sourceforge.clearcase.simple.ClearcaseFactory;

import java.io.File;

/**
 * This class is to {@link CommandLineClearCase} what {@link NewNativeClearCase} is to {@link NativeClearCase}
 *
 * @see NewNativeClearCase
 */
public class NewCommandLineClearCase extends AbstractClearCase {

    public NewCommandLineClearCase() throws ClearcaseException {
        super(ClearcaseFactory.CLI);
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

package net.sourceforge.transparent;

import net.sourceforge.clearcase.simple.ClearcaseException;
import net.sourceforge.clearcase.simple.ClearcaseFactory;

/**
 * @author Gilles Philippart
 */
public class NewDummyClearCase extends AbstractClearCase {

    public NewDummyClearCase() throws ClearcaseException {
        super(ClearcaseFactory.DUMMY);
    }
}

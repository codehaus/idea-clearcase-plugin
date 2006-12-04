package net.sourceforge.transparent;

import net.sourceforge.transparent.actions.VcsAction;

import java.io.IOException;

/**
 * @author Gilles Philippart
 */
public class ClearcaseUtils {

    public static void cleartool(String... subcmd) throws ClearCaseException {
        try {
            String[] command = Runner.getCommand("cleartool", subcmd);
            (new Runner()).runAsynchronously(command);
        }
        catch (IOException e) {
            VcsAction.LOG.error(e);
            throw new ClearCaseException(e.getMessage());
        }
    }

    public static void cleartoolSync(String... subcmd) throws ClearCaseException {
        String[] command = Runner.getCommand("cleartool", subcmd);
        (new Runner()).run(command);
    }
}

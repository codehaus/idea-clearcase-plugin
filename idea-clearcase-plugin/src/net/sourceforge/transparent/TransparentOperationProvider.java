package net.sourceforge.transparent;

import java.util.Map;

import com.intellij.openapi.vcs.StandardOperationsProvider;
import com.intellij.openapi.vcs.VcsException;

public class TransparentOperationProvider implements StandardOperationsProvider {

    private TransparentVcs transparentVcs;

    public TransparentOperationProvider(TransparentVcs transparentVcs) {
        this.transparentVcs = transparentVcs;
    }

    public void checkinFile(String path, Object parameters, Map userData)
            throws VcsException {
        transparentVcs.checkinFile(path, parameters);
    }

    public void addFile(String folderPath, String name, Object parameters,
                        Map userData) throws VcsException {
        transparentVcs.addFile(folderPath, name, parameters);
    }

    public void removeFile(String path, Object parameters, Map userData)
            throws VcsException {
        transparentVcs.removeFile(path, parameters);
    }

    public void addDirectory(String parentPath, String name, Object parameters,
                             Map userData) throws VcsException {
        transparentVcs.addDirectory(parentPath, name, parameters);
    }

    public void removeDirectory(String path, Object parameters, Map userData)
            throws VcsException {
        transparentVcs.removeDirectory(path, parameters);
    }
}
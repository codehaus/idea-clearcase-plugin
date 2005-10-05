package net.sourceforge.transparent;

import com.intellij.openapi.localVcs.LocalVcsServices;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusProvider;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFilePropertyEvent;
import org.intellij.openapi.OpenApiFacade;

public class TransparentFileStatusProvider
        implements FileStatusProvider {

    public static final Key<FileStatus> STATUS = new Key<FileStatus>("Transparent.Status");

    private final TransparentVcs transparentVcs;
    private FileStatusProvider lvcsProvider;

    public TransparentFileStatusProvider(TransparentVcs transparentVcs, final Project project) {
        this.transparentVcs = transparentVcs;
        lvcsProvider = LocalVcsServices.getInstance(project).getFileStatusProvider();

        VirtualFileManager.getInstance().addVirtualFileListener(new VirtualFileAdapter() {
            public void propertyChanged(VirtualFilePropertyEvent event) {
                if (isExcluded(event.getFile())) {
                    return;
                }
                boolean isChangeOfWritability = event.getPropertyName().equals("writable");
                if (isChangeOfWritability) {
                    FileStatus newStatus = null;
                    if (isFileModified(event)) {
                        newStatus = FileStatus.MODIFIED;
                    }
                    event.getFile().putUserData(STATUS, newStatus);
                    OpenApiFacade.getFileStatusManager(project).fileStatusChanged(event.getFile());
                }
            }

            public void contentsChanged(VirtualFileEvent event) {
                if (isExcluded(event.getFile())) {
                    return;
                }
                event.getFile().putUserData(STATUS, null);
            }

        });
    }

    private boolean isFileModified(VirtualFilePropertyEvent event) {
        return event.getNewValue().equals(Boolean.TRUE);
    }

    public FileStatus getStatus(VirtualFile virtualFile) {
        if (isExcluded(virtualFile)) {
            return FileStatus.UNKNOWN;
        }

        if (virtualFile.getUserData(STATUS) == null) {
            return lvcsProvider.getStatus(virtualFile);
        } else {
            return virtualFile.getUserData(STATUS);
        }
    }

    private boolean isExcluded(VirtualFile virtualFile) {
        return !transparentVcs.getFileFilter().accept(virtualFile);
    }
}

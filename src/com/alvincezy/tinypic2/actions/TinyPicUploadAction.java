package com.alvincezy.tinypic2.actions;

import com.alvincezy.tinypic2.TinyPicOptionsConfigurable;
import com.alvincezy.tinypic2.util.StringUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvince on 2017/6/28.
 *
 * @author alvince.zy@gmail.com
 * @version 2.0, 7/12/2017
 * @since 2.0
 */
public class TinyPicUploadAction extends TinifyAction {

    private List<VirtualFile> mTinifySource = new ArrayList<>();

    public TinyPicUploadAction() {
        super();
    }

    @Override
    protected void performAction(@Nonnull AnActionEvent anActionEvent, @Nonnull Project project) {
        super.performAction(anActionEvent, project);
        if (StringUtil.isNotEmpty(mPrefs.getApiKey())) {
            pickFiles(project);
        } else {
            TinyPicOptionsConfigurable.showSettingsDialog(project);
        }
    }

    private void pickFiles(Project project) {
        mTinifySource.clear();
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, true, false, false, false, true);
        VirtualFile[] selectedFiles = FileChooser.chooseFiles(descriptor, project, null);
        if (selectedFiles.length > 0) {
            for (VirtualFile source : selectedFiles) {
                parseFilePicked(source);
            }
        }
    }

    private void parseFilePicked(VirtualFile file) {
        if (file.isDirectory()) {
            VfsUtilCore.visitChildrenRecursively(file, new VirtualFileVisitor() {
                @Override
                public boolean visitFile(@NotNull VirtualFile file) {
                    parseFilePicked(file);
                    return true;
                }
            });
        } else {
            String filename = file.getName().toLowerCase();
            if (filename.endsWith(".jpg") || filename.endsWith(".png")) {
                mTinifySource.add(file);
            }
        }
    }
}

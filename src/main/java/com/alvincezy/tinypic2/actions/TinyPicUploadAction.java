package com.alvincezy.tinypic2.actions;

import com.alvincezy.tinypic2.TinifyFlowable;
import com.alvincezy.tinypic2.TinyPicOptionsConfigurable;
import com.alvincezy.tinypic2.util.StringUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import com.intellij.openapi.wm.impl.status.StatusBarUtil;
import com.tinify.Tinify;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alvince on 2017/6/28.
 *
 * @author alvince.zy@gmail.com
 * @version 2.0, 7/14/2017
 * @since 2.0
 */
public class TinyPicUploadAction extends TinifyAction {

    private static final String TAG = "TinyPicUploadAction";

    private List<VirtualFile> mTinifySource = new ArrayList<>();

    private volatile Map<String, Runnable> mTaskPool = new HashMap<>();
    private ExecutorService tinifyThreadPool = Executors.newFixedThreadPool(3);

    public TinyPicUploadAction() {
        super();
    }

    @Override
    protected void performAction(@Nonnull AnActionEvent anActionEvent, @Nonnull Project project) {
        super.performAction(anActionEvent, project);
        if (StringUtil.isNotEmpty(mPrefs.getApiKey())) {
            Tinify.setKey(mPrefs.getApiKey());
            pickFiles(project);
            uploadAndTinify();
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
        VfsUtilCore.visitChildrenRecursively(file, new VirtualFileVisitor() {
            @Override
            public boolean visitFile(@NotNull VirtualFile file) {
                String filename = file.getName().toLowerCase();
                if (filename.endsWith(".jpg") || filename.endsWith(".png")) {
                    mTinifySource.add(file);
                }
                return true;
            }
        });
    }

    private void uploadAndTinify() {
        mTaskPool.clear();
        if (Tinify.validate()) {
            setEnabledInModalContext(false);
            ProgressManager.getInstance().run(new Task.Backgroundable(mProject, "Perform Picture Tinify") {
                @Override
                public void run(@NotNull ProgressIndicator indicator) {
                    mTinifySource.forEach(file -> tinifyThreadPool.execute(new TaskRunnable(file)));
                    while (true) {
                        if (mTaskPool.isEmpty()) break;
                    }
                    indicator.setText("Complete Picture Tinify");
                    StatusBarUtil.setStatusBarInfo(mProject, "图片压缩完成");
                }
            });
        } else {
            Messages.showInfoMessage("Validate failure.", TAG);
        }
    }


    class TaskRunnable implements Runnable {
        private String name;
        private TinifyFlowable flowable;

        TaskRunnable(VirtualFile file) {
            this.name = file.getPath();
            flowable = new TinifyFlowable(file);
        }

        @Override
        public void run() {
            mTaskPool.put(name, this);
            try {
                flowable.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (flowable.performTinify()) {
                try {
                    //noinspection ConstantConditions
                    flowable.result().toFile(name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            flowable.file().refresh(true, false);
            mTaskPool.remove(name);
        }
    }
}

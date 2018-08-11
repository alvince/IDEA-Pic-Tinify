package com.alvincezy.tinypic2.actions

import com.alvincezy.tinypic2.*
import com.alvincezy.tinypic2.exts.supportTinify
import com.alvincezy.tinypic2.model.VirtualFileAware
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileVisitor
import com.tinify.Tinify
import org.apache.commons.lang.StringUtils
import java.io.IOException
import java.util.*

/**
 * Created by alvince on 2017/6/28.
 *
 * @author alvince.zy@gmail.com
 * @version 1.0.3-SNAPSHOT, 2018/8/11
 * @since 1.0
 */
class TinyPicUploadAction : TinifyAction() {

    companion object {
        private const val TAG = "TinyPicUploadAction"
    }

    @Volatile
    private var taskPool = HashMap<String, Runnable>()

    private val logger = Logger.getInstance(javaClass)
    private val tinifySource = ArrayList<VirtualFileAware>()

    override fun performAction(actionEvent: AnActionEvent, project: Project) {
        super.performAction(actionEvent, project)
        if (StringUtils.isEmpty(preferences.apiKey)) {
            TinyPicOptionsConfigurable.showSettingsDialog(project)
        } else {
            Tinify.setKey(preferences.apiKey)
            pickFiles(project)
        }
    }

    private fun pickFiles(project: Project) {
        tinifySource.clear()
        val descriptor = FileChooserDescriptor(true, true, false, false, false, true)
        val selectedFiles = FileChooser.chooseFiles(descriptor, project, project.baseDir)
        tinify {
            if (selectedFiles.isNotEmpty()) {
                selectedFiles.forEach { parseFilePicked(it) }
                uploadAndTinify()
            }
        }
    }

    @Suppress("name_shadowing")
    private fun parseFilePicked(file: VirtualFile) {
        VfsUtilCore.visitChildrenRecursively(file, object : VirtualFileVisitor<Any>() {
            override fun visitFile(file: VirtualFile): Boolean {
                if (file.supportTinify()) {
                    val fileW = VirtualFileAware(file)
                    if (tinifySource.contains(fileW)) {
                        return false
                    }
                    tinifySource.add(fileW)
                }
                return true
            }
        })
    }

    private fun uploadAndTinify() {
        taskPool.clear()

        if (!Tinify.validate()) {
            Messages.showInfoMessage("Validate failure.", TAG)
        }

        isEnabledInModalContext = false
        ProgressManager.getInstance().run(
                object : Task.Backgroundable(project, Constants.APP_NAME, false) {
                    override fun run(indicator: ProgressIndicator) {
                        indicator.text = "Perform Picture Tinify"
                        tinifySource.map { it.file }
                                .filter { TinifyStack.pushFileTinify(it.path) }
                                .forEach { file -> io(TaskRunnable(file)) }
                        do {
                            try {
                                Thread.sleep(50L)
                            } catch (ex: Exception) {
                                logger.error(ex)
                            }
                        } while (!taskPool.isEmpty())

                        indicator.text2 = "Complete Picture Tinify"
                        Notifications.Bus.notify(Notification(Constants.DISPLAY_GROUP_PROMPT,
                                Constants.APP_NAME, "图片压缩完成", NotificationType.INFORMATION))
                    }
                })
    }


    internal inner class TaskRunnable(file: VirtualFile) : Runnable {
        private val path: String = file.path
        private val flowable: TinifyFlowable = TinifyFlowable(file)

        override fun run() {
            taskPool[path] = this

            try {
                flowable.load()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (flowable.tinify()) {
                flowable.file().refresh(true, false)
                taskPool.remove(path)
            }
            TinifyStack.removeFileTinify(path)
        }
    }
}

package com.alvincezy.tinypic2.actions

import com.alvincezy.tinypic2.Constants
import com.alvincezy.tinypic2.TinifyFlowable
import com.alvincezy.tinypic2.TinyPicOptionsConfigurable
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
import rx.Observable
import rx.schedulers.Schedulers
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors

/**
 * Created by alvince on 2017/6/28.
 *
 * @author alvince.zy@gmail.com
 * @version 1.0.1, 7/21/2017
 * @since 1.0
 */
class TinyPicUploadAction : TinifyAction() {

    companion object {
        private val TAG = "TinyPicUploadAction"
    }

    @Volatile internal var taskPool = HashMap<String, Runnable>()

    private val logger = Logger.getInstance(javaClass)
    private val tinifySource = ArrayList<VirtualFileAware>()
    private val tinifyThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

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
        Observable.just(selectedFiles)
                .subscribeOn(Schedulers.io())
                .filter { selectedFiles.isNotEmpty() }
                .subscribe({
                    selectedFiles.forEach { parseFilePicked(it) }
//                    logger.debug("${tinifySource.toArray()}")
                    uploadAndTinify()
                }, { it.printStackTrace() })
    }

    @Suppress("name_shadowing")
    private fun parseFilePicked(file: VirtualFile) {
        VfsUtilCore.visitChildrenRecursively(file, object : VirtualFileVisitor<Any>() {
            override fun visitFile(file: VirtualFile): Boolean {
                if (file.name.endsWith(".jpg", true) || file.name.endsWith(".png", true)) {
                    val fileW = VirtualFileAware(file)
                    if (tinifySource.contains(fileW))
                        return false
                    tinifySource.add(fileW)
                }
                return true
            }
        })
    }

    private fun uploadAndTinify() {
        taskPool.clear()
        if (Tinify.validate()) {
            isEnabledInModalContext = false
            ProgressManager.getInstance().run(
                    object : Task.Backgroundable(project, Constants.APP_NAME, false) {
                        override fun run(indicator: ProgressIndicator) {
                            indicator.text = "Perform Picture Tinify"
                            tinifySource.map { it.file }
                                    .forEach { file -> tinifyThreadPool.execute(TaskRunnable(file)) }
                            while (true) {
                                if (taskPool.isEmpty()) break
                            }
                            indicator.text2 = "Complete Picture Tinify"
                            Notifications.Bus.notify(Notification(Constants.DISPLAY_GROUP_PROMPT,
                                    Constants.APP_NAME, "图片压缩完成", NotificationType.INFORMATION))
                        }
                    })
        } else {
            Messages.showInfoMessage("Validate failure.", TAG)
        }
    }


    internal inner class TaskRunnable(file: VirtualFile) : Runnable {
        private val name: String = file.path
        private val flowable: TinifyFlowable = TinifyFlowable(file)

        override fun run() {
            taskPool.put(name, this)
            try {
                flowable.load()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (flowable.performTinify()) {
                try {
                    flowable.result()!!.toFile(name)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            flowable.file().refresh(true, false)
            taskPool.remove(name)
        }
    }
}

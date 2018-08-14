package com.alvincezy.tinypic2.actions

import com.alvincezy.tinypic2.*
import com.alvincezy.tinypic2.exts.supportTinify
import com.alvincezy.tinypic2.model.VirtualFileAware
import com.alvincezy.tinypic2.presenter.prepare
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
import java.io.IOException
import java.util.*

/**
 * Created by alvince on 2017/6/28.
 *
 * @author alvince.zy@gmail.com
 * @version 1.1.0-SNAPSHOT, 2018/8/14
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
        val apiKey = preferences.apiKey
        if (prepare(project, apiKey)) {
            pickAndTinify(project)
        }
    }

    private fun pickAndTinify(project: Project) {
        tinifySource.clear()
        val descriptor = FileChooserDescriptor(true, true, false, false, false, true)
        val selectedFiles = FileChooser.chooseFiles(descriptor, project, project.baseDir)
        enable(false)
        tinify {
            if (selectedFiles.isEmpty()) {
                enable(true)
                return@tinify
            }
            ProgressManager.getInstance().run(object : Task.Backgroundable(project, Constants.APP_NAME, false) {
                override fun run(indicator: ProgressIndicator) {
                    indicator.text = "Perform Picture Tinify"
                    selectedFiles.forEach { parseFilePicked(it) }
                    console("tinify source ->\n\t$tinifySource")
                    uploadAndTinify()
                    console("task pool ->\n\t$taskPool")
                    if (taskPool.isEmpty()) {
                        return
                    }

                    do {
                        try {
                            Thread.sleep(50L)
                        } catch (ex: Exception) {
                            err(ex.stackTrace.toString())
                            logger.error(ex.message, ex)
                        }
                    } while (!taskPool.isEmpty())

                    indicator.text2 = "Complete Picture Tinify"
                    notify("图片压缩完成")
                    enable(true)
                }
            })
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
            Messages.showInfoMessage("Tinify invalidate.", TAG)
            return
        }

        tinifySource.map { it.file }
                .filter { TinifyStack.pushFileTinify(it.path) }
                .forEach { file ->
                    console("io -> tinify[ $file ]")
                    io(TaskRunnable(file))
                }
    }


    inner class TaskRunnable(file: VirtualFile) : Runnable {
        private val name = file.name
        private val path = file.path
        private val flowable: TinifyFlowable = TinifyFlowable(file)

        init {
            taskPool[name] = this
        }

        override fun run() {
            try {
                flowable.load()
            } catch (ex: IOException) {
                console(ex)
                err(ex.stackTrace.toString())
                logger.error(ex.message, ex)
                return
            }

            if (flowable.tinify()) {
                taskPool.remove(name)
                flowable.file().refresh(false, false)
            }
            TinifyStack.removeFileTinify(path)
        }
    }
}

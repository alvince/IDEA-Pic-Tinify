package com.alvincezy.tinypic2.actions

import com.alvincezy.tinypic2.*
import com.alvincezy.tinypic2.exts.supportTinify
import com.alvincezy.tinypic2.model.VirtualFileAware
import com.alvincezy.tinypic2.tinify.PluginStat
import com.alvincezy.tinypic2.tinify.TinifyBackgroundTask
import com.alvincezy.tinypic2.tinify.getTopSelection
import com.alvincezy.tinypic2.tinify.prepare
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
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.VirtualFileVisitor
import com.tinify.Tinify

/**
 * Created by alvince on 2017/6/28.
 *
 * @author alvince.zy@gmail.com
 * @version 1.1.1, 2018/8/24
 * @since 1.0
 */
class TinyPicUploadAction : TinifyAction() {

    companion object {
        private const val TAG = "TinyPicUploadAction"
    }

    private val logger = Logger.getInstance(javaClass)
    private val source = ArrayList<VirtualFile>()

    @Volatile
    private var taskRunning = false

    override fun performAction(actionEvent: AnActionEvent, project: Project) {
        val apiKey = preferences.apiKey
        if (prepare(project, apiKey)) {
            pickAndTinify(project)
        }
    }

    private fun pickAndTinify(project: Project) {
        val descriptor = FileChooserDescriptor(true, true, false, false, false, true)
        val urlSelectTo = PluginStat.urlSelectToTinify(project)
        console("base directory: $urlSelectTo")
        val selectedFiles = FileChooser.chooseFiles(descriptor, project,
                VirtualFileManager.getInstance().findFileByUrl(urlSelectTo))
        if (selectedFiles.isEmpty()) {
            return@pickAndTinify
        }

        val rootSelection = getTopSelection(selectedFiles)
        if (rootSelection != null) {
            console("select root dir: $rootSelection")
            PluginStat.pickFileDefault = rootSelection.path
        }
        enable(false)
        ProgressManager.getInstance().run(object : Task.Backgroundable(project, Constants.APP_NAME, true) {
            override fun run(indicator: ProgressIndicator) {
                tinify {
                    val tinifySource = ArrayList<VirtualFileAware>()
                    selectedFiles.forEach { tinifySource.addAll(parseFilePicked(it)) }
                    console("tinify source ->\n\t$tinifySource")

                    source.clear()
                    val launch = validateSelections(tinifySource)
                    taskRunning = true
                    if (launch) {
                        uploadAndTinify()
                    }
                }

                do {
                    try {
                        Thread.sleep(50L)
                    } catch (ex: InterruptedException) {
                        console(ex)
                        logger.warn(ex)
                    }
                } while (!taskRunning)
            }
        })
    }

    @Suppress("name_shadowing")
    private fun parseFilePicked(file: VirtualFile): List<VirtualFileAware> {
        val result = ArrayList<VirtualFileAware>()
        VfsUtilCore.visitChildrenRecursively(file, object : VirtualFileVisitor<Any>() {
            override fun visitFile(file: VirtualFile): Boolean {
                if (file.supportTinify()) {
                    val fileW = VirtualFileAware(file)
                    if (result.contains(fileW)) {
                        return false
                    }
                    result.add(fileW)
                }
                return true
            }
        })
        return result
    }

    private fun validateSelections(source: List<VirtualFileAware>): Boolean {
        if (!Tinify.validate()) {
            Messages.showInfoMessage("Tinify invalidate.", TAG)
            return false
        }

        val actualList = source.map { it.file }
                .filter { TinifyStack.pushFileTinify(it.path) }
        val size = actualList.size
        val notEmpty = size > 0
        if (notEmpty) {
            this.source.addAll(actualList)
        }
        return notEmpty
    }

    private fun uploadAndTinify() {
        val size = source.size
        var complete = 0
        val logBuffer = StringBuilder()
        source.forEach { file ->
            console("io -> tinify[ $file ]")
            TinifyBackgroundTask(project, file, false) { tinify, succeed ->
                if (succeed) {
                    logBuffer.append("${tinify.verbose()}\n")
                }
                complete = complete.inc()
            }.runTask(preferences.isBackupBeforeTinify)
        }
        do {
            try {
                Thread.sleep(50L)
            } catch (ex: Exception) {
                err(ex.stackTrace.toString())
                logger.error(ex.message, ex)
            }
        } while (complete < size)

        enable(true)
        notify(logBuffer.toString())
    }
}

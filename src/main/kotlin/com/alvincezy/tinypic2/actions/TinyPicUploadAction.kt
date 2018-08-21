package com.alvincezy.tinypic2.actions

import com.alvincezy.tinypic2.*
import com.alvincezy.tinypic2.exts.supportTinify
import com.alvincezy.tinypic2.model.VirtualFileAware
import com.alvincezy.tinypic2.tinify.TinifyBackgroundTask
import com.alvincezy.tinypic2.tinify.prepare
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileVisitor
import com.tinify.Tinify
import java.util.*

/**
 * Created by alvince on 2017/6/28.
 *
 * @author alvince.zy@gmail.com
 * @version 1.1.1, 2018/8/21
 * @since 1.0
 */
class TinyPicUploadAction : TinifyAction() {

    companion object {
        private const val TAG = "TinyPicUploadAction"
    }

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

            selectedFiles.forEach { parseFilePicked(it) }
            console("tinify source ->\n\t$tinifySource")
            uploadAndTinify()
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
        if (!Tinify.validate()) {
            Messages.showInfoMessage("Tinify invalidate.", TAG)
            return
        }

        val actualList = tinifySource.map { it.file }
                .filter { TinifyStack.pushFileTinify(it.path) }
        val size = actualList.size
        if (size == 0) {
            return
        }

        var complete = 0
        val logBuffer = StringBuilder()
        actualList.forEach { file ->
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

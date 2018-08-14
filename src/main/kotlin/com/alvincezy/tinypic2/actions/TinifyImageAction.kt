package com.alvincezy.tinypic2.actions

import com.alvincezy.tinypic2.*
import com.alvincezy.tinypic2.exts.supportTinify
import com.alvincezy.tinypic2.tinify.prepare
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.io.IOException

/**
 * Action as tinify image file selected.
 *
 * Created by alvince on 2018/8/14.
 *
 * @author alvince.zy@gmail.com
 * @version 1.1.0-SNAPSHOT, 2018/8/14
 * @since 1.1.0
 */
class TinifyImageAction : TinifyAction() {

    private val logger = Logger.getInstance(javaClass)

    override fun performAction(actionEvent: AnActionEvent, project: Project) {
        console("$this -> $actionEvent")

        if (!prepare(project, preferences.apiKey)) {
            return
        }

        val file = actionEvent.getData(CommonDataKeys.VIRTUAL_FILE) as VirtualFile
        if (!TinifyStack.pushFileTinify(file.path)) {
            return
        }

        ProgressManager.getInstance().run(FileTinifyTask(project, file))
    }

    override fun validateAction(actionEvent: AnActionEvent?): Boolean {
        val file = actionEvent?.getData(CommonDataKeys.VIRTUAL_FILE) as VirtualFile
        val support = file.supportTinify()
        console("${javaClass.name} => validate[ $support ]")
        actionEvent.presentation.isEnabledAndVisible = support
        return support
    }


    /**
     * File tinify background task
     */
    inner class FileTinifyTask(project: Project, val file: VirtualFile)
        : Task.Backgroundable(project, Constants.APP_NAME, false) {

        private var complete = false

        override fun run(indicator: ProgressIndicator) {
            val path = file.path
            val flowable = TinifyFlowable(file)

            indicator.text = "Perform Picture Tinify"
            console("Tinify source -> $path")
            io {
                try {
                    flowable.load()
                } catch (ex: IOException) {
                    console(ex)
                    err(ex.stackTrace.toString())
                    logger.error(ex.message, ex)
                    return@io
                }

                if (flowable.tinify()) {
                    flowable.file().refresh(false, false)
                }

                complete = true
                TinifyStack.removeFileTinify(path)
            }
            do {
                try {
                    Thread.sleep(50L)
                } catch (ex: Exception) {
                    err(ex.stackTrace.toString())
                    logger.error(ex.message, ex)
                }
            } while (!complete)
            notify(flowable.verbose())
        }
    }
}

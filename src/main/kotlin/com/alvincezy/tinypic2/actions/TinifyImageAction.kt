package com.alvincezy.tinypic2.actions

import com.alvincezy.tinypic2.TinifyStack
import com.alvincezy.tinypic2.console
import com.alvincezy.tinypic2.exts.supportTinify
import com.alvincezy.tinypic2.tinify.TinifyBackgroundTask
import com.alvincezy.tinypic2.tinify.prepare
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * Action as tinify image file selected.
 *
 * Created by alvince on 2018/8/14.
 *
 * @author alvince.zy@gmail.com
 * @version 1.1.1, 2018/8/21
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

        val task = TinifyBackgroundTask(project, file)
        task.logger = logger
        task.runTask(preferences.isBackupBeforeTinify)
    }

    override fun validateAction(actionEvent: AnActionEvent?): Boolean {
        val file: VirtualFile? = actionEvent?.getData(CommonDataKeys.VIRTUAL_FILE)
        val support = file.supportTinify()
        console("${javaClass.name} => validate[ $support ]")
        actionEvent?.presentation?.isEnabledAndVisible = support
        return support
    }
}

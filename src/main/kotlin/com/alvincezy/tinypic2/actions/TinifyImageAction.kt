package com.alvincezy.tinypic2.actions

import com.alvincezy.tinypic2.console
import com.alvincezy.tinypic2.exts.supportTinify
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

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

    override fun performAction(actionEvent: AnActionEvent, project: Project) {
    }

    override fun validateAction(actionEvent: AnActionEvent?): Boolean {
        val file = actionEvent?.getData(CommonDataKeys.VIRTUAL_FILE) as VirtualFile
        val support = file.supportTinify()
        console("${javaClass.name} => validate[ $support ]")
        actionEvent.presentation.isEnabledAndVisible = support
        return support
    }
}

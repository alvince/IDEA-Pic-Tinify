package com.alvincezy.tinypic2.actions

import com.alvincezy.tinypic2.Preferences
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

/**
 * Created by alvince on 17-7-11.
 *
 * @author alvince.zy@gmail.com
 * @version 2.0, 7/12/2017
 * @since 2.0
 */
open class TinifyAction internal constructor() : AnAction() {

    protected lateinit var project: Project

    protected var preferences = Preferences.getInstance()

    override fun actionPerformed(actionEvent: AnActionEvent?) {
        actionEvent ?: return
        project = AnAction.getEventProject(actionEvent)!!
        performAction(actionEvent, project)
    }

    protected open fun performAction(actionEvent: AnActionEvent, project: Project) {}
}

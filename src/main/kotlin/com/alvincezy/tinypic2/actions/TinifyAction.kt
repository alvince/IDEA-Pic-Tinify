package com.alvincezy.tinypic2.actions

import com.alvincezy.tinypic2.Preferences
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

/**
 * Created by alvince on 17-7-11.
 *
 * @author alvince.zy@gmail.com
 * @version 1.0.3-SNAPSHOT, 2018/8/12
 * @since 1.0
 */
open class TinifyAction internal constructor() : AnAction(), AnAction.TransparentUpdate {

    companion object {
        var isEnable: Boolean = true
    }

    protected val preferences = Preferences.getInstance()

    protected lateinit var project: Project
    protected var actionEvent: AnActionEvent? = null

    override fun actionPerformed(actionEvent: AnActionEvent?) {
        actionEvent ?: return

        project = AnAction.getEventProject(actionEvent)!!
        this.actionEvent = actionEvent
        performAction(actionEvent, project)
    }

    override fun update(e: AnActionEvent?) {
        super.update(e)
        val presentation = actionEvent?.presentation
        presentation?.isEnabled = TinifyAction.isEnable
    }

    protected open fun performAction(actionEvent: AnActionEvent, project: Project) {}

    protected fun enable(enable: Boolean) {
        TinifyAction.isEnable = enable
        update(actionEvent)
    }
}

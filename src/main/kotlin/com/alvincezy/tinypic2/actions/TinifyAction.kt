package com.alvincezy.tinypic2.actions

import com.alvincezy.tinypic2.Preferences
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

/**
 * Created by alvince on 17-7-11.
 *
 * @author alvince.zy@gmail.com
 * @version 1.1.0, 2018/8/14
 * @since 1.0
 */
abstract class TinifyAction internal constructor() : AnAction(), AnAction.TransparentUpdate {

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

        if (validateAction(e)) {
            actionEvent?.presentation?.isEnabled = TinifyAction.isEnable
        }
    }

    /**
     * Perform this action from user
     */
    protected abstract fun performAction(actionEvent: AnActionEvent, project: Project)

    /**
     * Validate if current action should appeared
     *
     * @return true as default show this action
     */
    protected open fun validateAction(actionEvent: AnActionEvent?): Boolean = true

    protected fun enable(enable: Boolean) {
        TinifyAction.isEnable = enable
        update(actionEvent)
    }
}

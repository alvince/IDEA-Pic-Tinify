package com.alvincezy.tinypic2

import com.alvincezy.tinypic2.ui.PreferencesPanel
import com.intellij.openapi.Disposable
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

/**
 * TinyPic 选项配置
 *
 * Created by alvince on 2017/6/28.
 *
 * @author alvince.zy@gmail.com
 * @version 1.0, 7/18/2017
 * @since 1.0
 */
class TinyPicOptionsConfigurable : SearchableConfigurable, Configurable.NoScroll, Disposable {

    companion object {
        fun showSettingsDialog(project: Project?) {
            if (project != null) {
                ShowSettingsUtil.getInstance().showSettingsDialog(project, TinyPicOptionsConfigurable::class.java)
            }
        }
    }

    private val preferences: Preferences = Preferences.getInstance()
    private var settingsPanel: PreferencesPanel? = null

    override fun getId(): String {
        return this.helpTopic
    }

    @Nls
    override fun getDisplayName(): String {
        return "TinyPic 2"
    }

    override fun getHelpTopic(): String {
        return "com.alvincezy.reference.settings.plugin.tinypic2"
    }

    override fun enableSearch(option: String?): Runnable? {
        return Runnable { print("enableSearch") }
    }

    override fun createComponent(): JComponent? {
        settingsPanel = PreferencesPanel()
        return settingsPanel!!.create(preferences)
    }

    override fun isModified(): Boolean {
        return settingsPanel!!.isModified
    }

    @Throws(ConfigurationException::class)
    override fun apply() {
        settingsPanel!!.apply()
    }

    override fun reset() {
        settingsPanel!!.reset()
    }

    override fun disposeUIResources() {
        Disposer.dispose(this)
    }

    override fun dispose() {
        settingsPanel = null
    }
}

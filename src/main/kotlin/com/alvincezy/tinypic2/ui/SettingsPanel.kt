package com.alvincezy.tinypic2.ui

import com.alvincezy.tinypic2.Constants
import com.alvincezy.tinypic2.Preferences
import com.alvincezy.tinypic2.util.ComponentUtil
import com.intellij.ide.BrowserUtil
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.layout.panel
import java.awt.BorderLayout
import javax.swing.*

/**
 * Plugin settings panel
 *
 * Created by alvince on 18-8-28.
 *
 * @author alvince.zy@gmail.com
 * @version 1.1.2-SNAPSHOT, 2018/8/31
 * @since 1.1.2-SNAPSHOT
 */
class SettingsPanel(private val prefs: Preferences) {

    var modified = false
        get() = apiKeyUpdated or backupOptionChanged
        private set

    private var apiKeyUpdated = false
    private var backupOptionChanged = false

    private lateinit var apiKeyField: JTextField
    private lateinit var backupCheck: JCheckBox

    fun create(): JComponent {
        val panel = panel {}
        panel.layout = BorderLayout()
        panel.add(tinifyPanel(), BorderLayout.NORTH)
        panel.add(optionsPanel(), BorderLayout.CENTER)
        return panel
    }

    fun apply() {
        prefs.apiKey = ComponentUtil.getInputText(apiKeyField)
        prefs.isBackupBeforeTinify = backupCheck.isSelected
        resetStatus()
    }

    fun reset() {
        apiKeyField.text = prefs.apiKey
        backupCheck.isSelected = prefs.isBackupBeforeTinify
        resetStatus()
    }

    private fun tinifyPanel(): JPanel = panel {
        val panel = panel {
            row("API Key: ") {
                apiKeyField = JTextField(prefs.apiKey)
                apiKeyField(grow)
            }
            apiKeyField.addActionListener {
                val text = ComponentUtil.getInputText(it.source as JTextField)
                apiKeyUpdated = text != prefs.apiKey
            }
            val label = "没有 API key？ 点击获取"
            val link = Constants.LINK_TINY_PNG_DEVELOPER
            noteRow("""<a href="$link">$label</a>""") { BrowserUtil.browse(it) }
        }
        panel.border = IdeBorderFactory.createTitledBorder("TinyPng")
        return panel
    }

    private fun optionsPanel(): JPanel = panel {
        val panel = panel {
            row("备份原图") {
                backupCheck = checkBox("", prefs.isBackupBeforeTinify)
            }
            backupCheck.addChangeListener {
                val selected = (it.source as JToggleButton).isSelected
                backupOptionChanged = selected != prefs.isBackupBeforeTinify
            }
        }
        panel.border = IdeBorderFactory.createTitledBorder("Options")
        return panel
    }

    private fun resetStatus() {
        apiKeyUpdated = false
        backupOptionChanged = false
    }
}

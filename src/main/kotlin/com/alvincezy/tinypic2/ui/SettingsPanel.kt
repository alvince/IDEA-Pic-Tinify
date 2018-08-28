package com.alvincezy.tinypic2.ui

import com.alvincezy.tinypic2.Constants
import com.intellij.ide.BrowserUtil
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.layout.panel
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

/**
 * Plugin settings panel
 *
 * Created by alvince on 18-8-28.
 *
 * @author alvince.zy@gmail.com
 * @version 1.1.2-SNAPSHOT, 2018/8/28
 * @since 1.1.2-SNAPSHOT
 */
class SettingsPanel {

    private lateinit var apiKeyField: JTextField

    var modified = false
        private set

    fun create(): JComponent {
        val panel = panel {}
        panel.layout = BorderLayout()
        panel.background = Color.LIGHT_GRAY
        panel.add(tinifyPanel(), BorderLayout.NORTH)
        panel.add(optionsPanel(), BorderLayout.CENTER)
        return panel
    }

    fun apply() {

    }

    fun reset() {

    }

    private fun tinifyPanel(): JPanel = panel {
        apiKeyField = JTextField()
        val panel = panel {
            row("API Key: ") { apiKeyField }
            val label = "没有 Api Key？ 申请一个！"
            val link = Constants.LINK_TINY_PNG_DEVELOPER
            noteRow("<a href='$link'>$label</a>") {
                BrowserUtil.browse(Constants.LINK_TINY_PNG_DEVELOPER)
            }
        }
        panel.border = IdeBorderFactory.createTitledBorder("TinyPng")
        panel.background = Color.GRAY
        apiKeyField.alignmentX = Component.LEFT_ALIGNMENT
        return panel
    }

    private fun optionsPanel(): JPanel = panel {
        val panel = panel {

        }
        panel.border = IdeBorderFactory.createTitledBorder("Options")
        panel.background = Color.PINK
        return panel
    }
}

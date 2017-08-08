package com.alvincezy.tinypic2.components

import com.alvincezy.tinypic2.Constants
import com.alvincezy.tinypic2.Preferences
import com.alvincezy.tinypic2.TinyPicOptionsConfigurable
import com.intellij.ide.util.PropertiesComponent
import com.intellij.notification.Notification
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.project.Project
import org.apache.commons.lang.StringUtils
import javax.swing.event.HyperlinkEvent

/**
 * Created by alvince on 17-7-20.
 *
 * @author alvince.zy@gmail.com
 * @version 1.0.1, 7/21/2017
 * @since 1.0.1
 */
class TinifyComponent(project: Project) : AbstractProjectComponent(project) {

    companion object {
        const val PROP_PROMPT_SETTINGS_IGNORE = "com.alvincezy.TinyPic2.PROMPT_SETTINGS_IGNORE"
    }

    internal lateinit var preferences: Preferences

    override fun initComponent() {
        super.initComponent()
        preferences = Preferences.getInstance()
    }

    override fun projectOpened() {
        if (StringUtils.isEmpty(preferences.apiKey)
                && !PropertiesComponent.getInstance().getBoolean(PROP_PROMPT_SETTINGS_IGNORE, false)) {
            val notificationContent = "当前 Api Key 为空，请设置 Api Key<br/>%s&nbsp;&nbsp;&nbsp;&nbsp;%s"
                    .format(Constants.HTML_LINK_SETTINGS, Constants.HTML_LINK_IGNORE)
            val notification = Notification(Constants.DISPLAY_GROUP_PROMPT,
                    Constants.APP_NAME, notificationContent, NotificationType.WARNING,
                    object : NotificationListener.Adapter() {
                        override fun hyperlinkActivated(notification: Notification, event: HyperlinkEvent) {
                            notification.expire()

                            when (event.description) {
                                Constants.HTML_DESCRIPTION_SETTINGS ->
                                    TinyPicOptionsConfigurable.showSettingsDialog(myProject)
                                Constants.HTML_DESCRIPTION_IGNORE ->
                                    PropertiesComponent.getInstance().setValue(PROP_PROMPT_SETTINGS_IGNORE, true)
                            }
                        }
                    })
            Notifications.Bus.notify(notification, myProject)
        }
    }
}
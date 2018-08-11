package com.alvincezy.tinypic2

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import java.lang.Exception

/*
 * Created by alvince on 18-8-12.
 *
 * @author alvince.zy@gmail.com
 */

fun notify(content: String) {
    notify(null, content, NotificationType.INFORMATION)
}

fun notify(title: String?, content: String, type: NotificationType) {
    if (content.isEmpty()) {
        return
    }
    Notifications.Bus.notify(Notification(Constants.DISPLAY_GROUP_PROMPT,
            if (title.isNullOrEmpty()) Constants.APP_NAME else title!!,
            content, type))
}

fun err(error: String) {
    notify(null, error, NotificationType.ERROR)
}

fun console(content: Any?) {
    content ?: return
    if (Constants.DEBUG) {
        when (content) {
            is Exception -> println(content.stackTrace.toString())
            else -> println(content)
        }
    }
}

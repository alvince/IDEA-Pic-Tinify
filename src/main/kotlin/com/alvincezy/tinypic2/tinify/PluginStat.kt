package com.alvincezy.tinypic2.tinify

import com.intellij.openapi.project.Project

/**
 * Singleton plugin stat wrapper
 *
 * Created by alvince on 18-8-24.
 *
 * @author alvince.zy@gmail.com
 * @version 1.1.1, 2018/8/24
 */
object PluginStat {

    var pickFileDefault: String = ""

    /**
     * Obtain files dir-url select to
     */
    fun urlSelectToTinify(project: Project): String = "file://${if (pickFileDefault.isEmpty()) project.baseDir.path else pickFileDefault}"
}

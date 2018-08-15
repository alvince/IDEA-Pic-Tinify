package com.alvincezy.tinypic2.tinify

import com.alvincezy.tinypic2.TinyPicOptionsConfigurable
import com.intellij.openapi.project.Project
import com.tinify.Tinify

fun prepare(project: Project?, apiKey: String): Boolean {
    if (apiKey.isEmpty()) {
        TinyPicOptionsConfigurable.showSettingsDialog(project)
        return false
    }
    Tinify.setKey(apiKey)
    return true
}

package com.alvincezy.tinypic2.tinify

import com.alvincezy.tinypic2.TinyPicOptionsConfigurable
import com.alvincezy.tinypic2.exts.supportTinify
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.tinify.Tinify
import java.io.File

/**
 * Indicate current in debug mode.
 *
 * Should disable this while build plugin release
 */
const val DEBUG_MODE = true

fun prepare(project: Project?, apiKey: String): Boolean {
    if (apiKey.isEmpty()) {
        TinyPicOptionsConfigurable.showSettingsDialog(project)
        return false
    }
    Tinify.setKey(apiKey)
    return true
}

fun backupTinifySource(file: VirtualFile, refresh: Boolean = false) {
    if (file.supportTinify()) {
        val backupDir = File("${file.parent.path}/backup")
        if (!backupDir.exists()) {
            backupDir.mkdirs()
        }

        val target = File(backupDir, file.name)
        if (target.exists()) {
            return
        }

        File(file.path).copyTo(target, true)

        if (!refresh) return
        VirtualFileManager.getInstance()
                .findFileByUrl(target.toURL().toString())
                ?.refresh(true, false)
    }
}

fun getTopSelection(selections: Array<VirtualFile>): VirtualFile? {
    if (selections.isEmpty()) return null

    var root = selections[0]
    selections.forEach {
        val filePath = if (it.isDirectory) it.path else it.parent.path

        if (root.path.contains(filePath)) {
            root = it
        }
    }
    return root
}

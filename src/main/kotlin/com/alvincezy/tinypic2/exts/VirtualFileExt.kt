package com.alvincezy.tinypic2.exts

import com.intellij.openapi.vfs.VirtualFile

fun VirtualFile?.supportTinify(): Boolean = this != null
        && path.isNotEmpty() && name.isNotEmpty()
        && (name.endsWith(".jpg", true) || name.endsWith(".png", true))

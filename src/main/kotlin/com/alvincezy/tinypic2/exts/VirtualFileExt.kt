package com.alvincezy.tinypic2.exts

import com.alvincezy.tinypic2.util.StringUtil
import com.intellij.openapi.vfs.VirtualFile

fun VirtualFile?.supportTinify(): Boolean = this != null
        && StringUtil.isNotEmpty(name)
        && name.endsWith(".jpg", true)
        && name.endsWith(".png", true)

package com.alvincezy.tinypic2.model

import com.intellij.openapi.vfs.VirtualFile

/**
 * Created by alvince on 2017/7/21.
 *
 * @author alvince.zy@gmail.com
 * @version 1.0.1, 7/21/2017
 */
class VirtualFileAware(val file: VirtualFile) {

    var fileUri = file.url

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as VirtualFileAware

        if (fileUri != other.fileUri) return false

        return true
    }

    override fun hashCode(): Int {
        return fileUri.hashCode()
    }

    override fun toString(): String {
        return fileUri
    }
}
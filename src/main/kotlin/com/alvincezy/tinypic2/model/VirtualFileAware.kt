package com.alvincezy.tinypic2.model

import com.intellij.openapi.vfs.VirtualFile

/**
 * Created by alvince on 2017/7/21.
 *
 * @author alvince.zy@gmail.com
 * @version 1.0.2, 2018/8/10
 */
class VirtualFileAware(val file: VirtualFile) {

    var fileUri = file.url

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other?.javaClass != javaClass) {
            return false
        }

        return fileUri == (other as VirtualFileAware).fileUri
    }

    override fun hashCode(): Int = fileUri.hashCode()

    override fun toString(): String = fileUri
}

package com.alvincezy.tinypic2

import com.intellij.openapi.vfs.VirtualFile
import com.tinify.Result
import com.tinify.Source
import com.tinify.Tinify
import java.io.IOException

/**
 * Created by alvince on 17-7-14.
 *
 * @author alvince.zy@gmail.com
 */
class TinifyFlowable(
        private val file: VirtualFile
) {

    var source: Source? = null
        private set
    var result: Result? = null
        private set

    @Throws(IOException::class)
    fun load() {
        source = Tinify.fromFile(file.path)
    }

    fun tinify(): Boolean {
        source ?: return false

        try {
            result = source!!.result()
            result ?: return false

            result!!.toFile(file.path)
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun file(): VirtualFile {
        return file
    }
}

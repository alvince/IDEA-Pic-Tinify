package com.alvincezy.tinypic2

import com.alvincezy.tinypic2.tinify.Verbose
import com.intellij.openapi.vfs.VirtualFile
import com.tinify.Result
import com.tinify.Source
import com.tinify.Tinify
import java.io.IOException

/**
 * Created by alvince on 17-7-14.
 *
 * @author alvince.zy@gmail.com
 * @version 1.1.2, 2018/8/31
 * @since 1.0.0
 */
class TinifyFlowable(private val file: VirtualFile) {

    var source: Source? = null
        private set

    var result: Result? = null
        private set

    private val verbose = Verbose(file)

    @Throws(IOException::class)
    fun load() {
        source = Tinify.fromFile(file.path)
    }

    fun tinify(): Boolean {
        source ?: return false

        try {
            result = source!!.result()
            result ?: return false

            verbose.complete(result!!.size())
            result!!.toFile(file.path)
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun file(): VirtualFile = file

    fun verbose(): String = verbose.log()
}

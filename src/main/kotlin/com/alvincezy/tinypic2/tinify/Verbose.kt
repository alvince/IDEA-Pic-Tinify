package com.alvincezy.tinypic2.tinify

import com.intellij.openapi.vfs.VirtualFile

/**
 * Created by alvince on 18-8-15.
 *
 * @author alvince.zy@gmail.com
 * @version 1.1.0-SNAPSHOT, 2018/8/15
 * @since 1.1.0
 */
class Verbose(val file: VirtualFile) {

    val startTime = System.currentTimeMillis()
    val originalSize = file.length

    var timeCost: Long = 0L
    var compressedSize = 0L

    fun complete(size: Int) {
        val curTime = System.currentTimeMillis()
        timeCost = curTime.minus(startTime)

        if (size < originalSize) {
            compressedSize = originalSize.minus(size)
        }
    }

    fun log(): String {
        val rate = compressedSize.toFloat().div(originalSize).times(100)
        return "Tinify ${file.path} with ${rate.toInt()}% compressed, cost ${timeSeconds(timeCost)}"
    }
}

fun timeSeconds(timeMillis: Long): String {
    val seconds = timeMillis.div(1000).toInt()
    val rem = timeMillis.rem(1000).toInt()

    return if (rem < 100) "${seconds}[s]" else "$seconds.${rem.div(10)}[s]"
}

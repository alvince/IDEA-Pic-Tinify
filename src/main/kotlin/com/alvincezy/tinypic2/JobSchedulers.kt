package com.alvincezy.tinypic2

import java.util.concurrent.Executors

/**
 * Created by alvince on 2018/8/10.
 *
 * @author alvince.zy@gmail.com
 * @version 1.0.3-SNAPSHOT, 2018/8/11
 * @since 1.0.3
 */
object JobExecutors {

    private val ioExecutor = Executors.newCachedThreadPool()
    private val tinifyExecutor = Executors.newFixedThreadPool(3)

    fun execute(action: Runnable, schedule: String) {
        when (schedule) {
            "io" -> ioExecutor.execute(action)
            "tinify" -> tinifyExecutor.execute(action)
        }
    }

    fun execute(schedule: String, action: () -> Unit) {
        when (schedule) {
            "io" -> ioExecutor.execute(action)
            "tinify" -> tinifyExecutor.execute(action)
        }
    }
}

fun io(action: Runnable) {
    JobExecutors.execute(action, "io")
}

fun io(action: () -> Unit) {
    JobExecutors.execute("io", action)
}

fun tinify(action: () -> Unit) {
    JobExecutors.execute("tinify", action)
}

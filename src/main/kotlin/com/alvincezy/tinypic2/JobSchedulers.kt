package com.alvincezy.tinypic2

import java.util.concurrent.Executors

object JobExecutors {

    private val ioExecutor = Executors.newCachedThreadPool()

    fun execute(action: Runnable, schedule: String) {
        when (schedule) {
            "io" -> ioExecutor.execute(action)
        }
    }

    fun execute(schedule: String, action: () -> Unit) {
        when (schedule) {
            "io" -> ioExecutor.execute(action)
        }
    }
}

fun io(action: Runnable) {
    JobExecutors.execute(action, "io")
}

fun io(action: () -> Unit) {
    JobExecutors.execute("io", action)
}

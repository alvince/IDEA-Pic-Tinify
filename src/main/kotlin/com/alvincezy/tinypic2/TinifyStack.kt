package com.alvincezy.tinypic2

object TinifyStack {

    val tinifyTasks = HashMap<String, Runnable>()

    fun clear() {
        synchronized(this) {
            tinifyTasks.clear()
        }
    }

    fun isEmpty(): Boolean = tinifyTasks.isEmpty()
}

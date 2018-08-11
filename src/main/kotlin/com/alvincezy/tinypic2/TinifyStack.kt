package com.alvincezy.tinypic2

import java.util.ArrayList
import kotlin.collections.HashMap

/**
 * Created by alvince on 2018/8/10.
 *
 * @author alvince.zy@gmail.com
 * @version 1.0.3-SNAPSHOT, 2018/8/11
 * @since 1.0.3
 */
object TinifyStack {

    val tinifyTasks = HashMap<String, Runnable>()
    val tinifyTaskPool = ArrayList<String>()

    fun clear() {
        synchronized(this) {
            tinifyTasks.clear()
        }
    }

    fun hasTaskProcessing(): Boolean = tinifyTasks.isEmpty()

    fun pushFileTinify(file: String): Boolean {
        if (file.isNotEmpty()) {
            return false
        }
        synchronized(this) {
            if (tinifyTaskPool.contains(file)) {
                return false
            }
            tinifyTaskPool.add(file)
            return true
        }
    }

    fun removeFileTinify(file: String) {
        if (file.isNotEmpty()) {
            return
        }
        synchronized(this) {
            if (tinifyTaskPool.contains(file)) {
                tinifyTaskPool.remove(file)
            }
        }
    }
}

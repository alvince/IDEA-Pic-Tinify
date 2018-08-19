package com.alvincezy.tinypic2.tinify

import com.alvincezy.tinypic2.*
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.io.IOException

/**
 * Tinify background task
 *
 * Created by alvince on 18-8-15.
 *
 * @author alvince.zy@gmail.com
 * @version 1.1.1, 2018/8/19
 */
class TinifyBackgroundTask(project: Project?, val file: VirtualFile,
                           val notify: Boolean = true, cancelable: Boolean = false, val callback: (TinifyFlowable, Boolean) -> Unit)
    : Task.Backgroundable(project, Constants.APP_NAME, cancelable) {

    var logger: Logger? = null
    lateinit var flowable: TinifyFlowable
        private set

    private var complete = false
    private var failured = false
    private var backupOnTinify = false

    constructor(project: Project?, file: VirtualFile, notify: Boolean = true, cancelable: Boolean = false)
            : this(project, file, notify, cancelable, { _, _ -> })

    override fun run(indicator: ProgressIndicator) {
        flowable = TinifyFlowable(file)
        val path = flowable.file().path

        indicator.text = "Compress ${file.path}"
        console("Tinify source -> $path")

        if (backupOnTinify) {
            backupTinifySource(file)
        }

        try {
            flowable.load()
        } catch (ex: IOException) {
            console(ex)
            failured = true
            err(ex.stackTrace.toString())
            logger?.error(ex.message, ex)
        }

        if (flowable.tinify()) {
            flowable.file().refresh(false, false)
            complete = true
        }
        TinifyStack.removeFileTinify(path)

        if (notify) {
            notify(flowable.verbose())
        }
        callback.invoke(flowable, complete)
    }

    fun runTask(backup: Boolean = false) {
        backupOnTinify = backup
        ProgressManager.getInstance().run(this)
    }
}

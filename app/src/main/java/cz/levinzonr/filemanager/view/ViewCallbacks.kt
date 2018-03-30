package cz.levinzonr.filemanager.view

import cz.levinzonr.filemanager.model.File

interface ViewCallbacks {

    fun onLoadingStart()

    fun onLoadingFinished(items: ArrayList<File>)

    fun onError(e: String)

    fun onFileDeleted(num: Int, max: Int, file: File)


    fun onFilesDeleted()
}
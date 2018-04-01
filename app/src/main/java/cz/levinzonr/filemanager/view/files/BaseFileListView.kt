package cz.levinzonr.filemanager.view.files

import cz.levinzonr.filemanager.model.File

interface BaseFileListView {

    fun onLoadingStart()
    fun onLoadingFinished(items: ArrayList<File>)
    fun onError(e: String)
    fun onFolderSelected(file: File)
    fun onFileSelected(file: File)
}
package cz.levinzonr.filemanager.view.folderchooser

import cz.levinzonr.filemanager.model.File

interface FileExplorerView {

    fun onLoadingStart()
    fun onLoadingFinished(items: ArrayList<File>)
    fun onError(e: String)
    fun onFolderSelected(file: File)
    fun onFileSelected(file: File)
}
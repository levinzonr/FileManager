package cz.levinzonr.filemanager.view.fileslist

import cz.levinzonr.filemanager.view.folderchooser.FileExplorerView

interface FileListMvpView : FileExplorerView {

    fun onFileDeleted(num: Int, max: Int)

    fun onFilesDeleted()

    fun startActionMode()

    fun destroyActionMode()

    fun updateActionMode(itemsCount: Int)


}
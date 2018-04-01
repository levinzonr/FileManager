package cz.levinzonr.filemanager.view.files.fileslist

import cz.levinzonr.filemanager.view.files.BaseFileListView

interface FileListCabView : BaseFileListView {

    fun onFileDeleted(num: Int, max: Int)

    fun onFilesDeleted()

    fun startActionMode()

    fun destroyActionMode()

    fun updateActionMode(itemsCount: Int)


}
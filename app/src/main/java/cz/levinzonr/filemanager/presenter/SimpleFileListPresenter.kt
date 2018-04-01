package cz.levinzonr.filemanager.presenter

import cz.levinzonr.filemanager.view.fileslist.RecyclerItemView

class SimpleFileListPresenter : FilesPresenter() {

    override fun bindItemAtPosition(pos: Int, view: RecyclerItemView) {
        val file = items[pos]
        when {
            file.isDirectory -> view.setFolderView(file.name)
            else -> view.setCheckedView(file.name)
        }
    }

    override fun onFileSelected(pos: Int) {
        if (items[pos].isDirectory) view?.onFolderSelected(items[pos])
    }

    override fun onLongClick(pos: Int) {
        // do nothinh
    }
}
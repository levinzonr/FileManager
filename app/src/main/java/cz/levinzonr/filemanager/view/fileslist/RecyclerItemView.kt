package cz.levinzonr.filemanager.view.fileslist

interface RecyclerItemView {

    fun setFileView(name: String)

    fun setFolderView(name: String)

    fun setCheckedView(name: String)

}
package cz.levinzonr.filemanager.view.files

interface RecyclerItemView {

    fun setFileView(name: String)

    fun setFolderView(name: String)

    fun setCheckedView(name: String)

}
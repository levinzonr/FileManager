package cz.levinzonr.filemanager.view.folderchooser

import cz.levinzonr.filemanager.presenter.FilesListCabPresenter
import cz.levinzonr.filemanager.presenter.FilesPresenter
import cz.levinzonr.filemanager.presenter.SimpleFileListPresenter
import cz.levinzonr.filemanager.view.files.BaseFileListFragment

class FolderChooserFragment : BaseFileListFragment() {

    override fun initPresenter() {
        presenter = FilesListCabPresenter()
    }

    override fun presenter(): SimpleFileListPresenter {
        return presenter as SimpleFileListPresenter
    }



}
package cz.levinzonr.filemanager.presenter

import android.util.Log
import cz.levinzonr.filemanager.model.DataManager
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.view.fileslist.FileListMvpView
import cz.levinzonr.filemanager.view.fileslist.RecyclerItemView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class FilesListPresenter : Presenter<FileListMvpView>() {


    lateinit var checked: ArrayList<Int>
    var isActionModeActive =  false

    override fun onAttach(view: FileListMvpView) {
        super.onAttach(view)
        checked = ArrayList()
    }

   companion object {
       const val TAG = "FileListPresenter"
   }

    fun performDeletion() {
       disposable.add(Observable.fromIterable(checked)
               .map { items[it] }
               .map { java.io.File(it.path) }
               .map { dataManager.removeFile(it) }
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeWith(object :DisposableObserver<File>(){
                   var cnt = 0
                   override fun onComplete() {
                    Log.d(TAG, "Done all")
                       (view as FileListMvpView).onFilesDeleted()
                       onActionModeDestroy()
                   }
                   override fun onNext(t: File) {
                        Log.d(TAG, "done prosceeing $t")
                        items.remove(t)
                       (view as FileListMvpView).onFileDeleted(cnt++, checked.size)
                   }

                   override fun onError(e: Throwable) {
                     Log.d(TAG, "error $e")
                   }
               }))
    }

    override fun onLongClick(position: Int) {
        (view as FileListMvpView).startActionMode()
        isActionModeActive = true
        markItemAtPosition(position)
    }

    fun onActionModeDestroy() {
        isActionModeActive = false
        (view as FileListMvpView).destroyActionMode()
        checked.clear()
        (view as FileListMvpView).updateActionMode(0)
    }

    private fun markItemAtPosition(pos: Int) {
        if (checked.contains(pos)) {
            checked.remove(pos)
        } else {
            checked.add(pos)
        }
        if (checked.size == 0) {
            onActionModeDestroy()
        }
        (view as FileListMvpView).updateActionMode(checked.size)
    }

    override fun onFileSelected(pos: Int) {
        val file = items[pos]
        when {
            isActionModeActive -> markItemAtPosition(pos)
            file.isDirectory -> view?.onFolderSelected(file)
            else -> view?.onFileSelected(file)
        }
    }

    fun restoreActionMode(restored: ArrayList<Int>) {
        (view as FileListMvpView).startActionMode()
        isActionModeActive = true
        checked = restored
        (view as FileListMvpView).updateActionMode(checked.size)
    }


    override fun bindItemAtPosition(pos: Int, view: RecyclerItemView) {
        val file = items[pos]
        when {
            checked.contains(pos) -> view.setCheckedView(file.name)
            file.isDirectory -> view.setFolderView(file.name)
            else -> view.setFileView(file.name)
        }
    }

}
package cz.levinzonr.filemanager.presenter

import android.util.Log
import cz.levinzonr.filemanager.model.DataManager
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.view.fileslist.RecyclerItemView
import cz.levinzonr.filemanager.view.folderchooser.FileExplorerView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

abstract class FilesPresenter : BasePresenter<FileExplorerView>{

    protected var view: FileExplorerView? = null
    protected val dataManager = DataManager()
    protected var disposable = CompositeDisposable()
    protected lateinit var items: ArrayList<File>

    override fun onAttach(view: FileExplorerView) {
        this.view = view
        items = ArrayList()
    }

    override fun onDetach() {
        if (!disposable.isDisposed) {
            disposable.clear()
        }
        view = null
    }

    fun getFilesInFolder(path: String) {
        view?.onLoadingStart()
        disposable.add( Observable.just(path)
                .map { t -> dataManager.getFilesFromDirectory(t)  }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<ArrayList<File>>(){
                    override fun onComplete() {
                        Log.d(FilesListPresenter.TAG, "onComplete")
                        view?.onLoadingFinished(items)
                    }

                    override fun onNext(t: ArrayList<File>) {
                        Log.d(FilesListPresenter.TAG, "onNExt")
                        items = t
                    }

                    override fun onError(e: Throwable) {
                        Log.d(FilesListPresenter.TAG, "onError")
                        view?.onError(e.toString())
                    }
                }))
    }

    abstract fun bindItemAtPosition(pos: Int, view: RecyclerItemView)

    fun itemsCount() = items.size

    abstract fun onFileSelected(pos: Int)

    abstract fun onLongClick(pos: Int)

}
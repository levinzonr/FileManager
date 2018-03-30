package cz.levinzonr.filemanager.presenter

import android.util.Log
import cz.levinzonr.filemanager.model.DataManager
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.view.fileslist.FilesListFragment
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

class FilesListPresenter : Presenter<FilesListFragment> {

    private var view: FilesListFragment? = null
    private val dataManager = DataManager()
    private var disposable: Disposable? = null

    override fun onAttach(view: FilesListFragment) {
        this.view = view

    }

   companion object {
       const val TAG = "FileListPresenter"
   }

    fun getFilesInFolder(path: String) {
        view?.onLoadingStart()

         disposable = Observable.just(path)
                 .map { t -> dataManager.files(t)  }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<ArrayList<File>>(){
                    override fun onComplete() {
                        Log.d(TAG, "onComplete")
                    }

                    override fun onNext(t: ArrayList<File>) {
                        Log.d(TAG, "onNExt")
                        view?.onLoadingFinished(t)
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, "onError")
                        view?.onError(e.toString())
                    }
                })
    }

    override fun onDetach() {
        view = null
        disposable?.dispose()
    }

}
package cz.levinzonr.filemanager.model

import android.content.Context
import android.os.Environment
import android.util.Log
import io.reactivex.Observable
import java.io.File
import java.util.concurrent.TimeUnit

class DataManager {

    companion object {
        const val TAG = "DataManager"
        val ROOT_DIR = Environment.getRootDirectory().absolutePath
    }

    fun files(path: String = ROOT_DIR) : Observable<ArrayList<cz.levinzonr.filemanager.model.File>> {
        val dir = File(path)
        Log.d(TAG, ROOT_DIR)
        val list = ArrayList<cz.levinzonr.filemanager.model.File>()
        for (file in dir.listFiles()) {
            Log.d(TAG, file.name )
            list.add(cz.levinzonr.filemanager.model.File.fromFile(file))
        }
       return Observable.just(list).delay(2, TimeUnit.SECONDS)
    }

}
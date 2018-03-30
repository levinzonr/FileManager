package cz.levinzonr.filemanager.model

import android.content.Context
import android.os.Environment
import android.os.Looper
import android.util.Log
import io.reactivex.Observable
import java.io.File

class DataManager {

    companion object {
        const val TAG = "DataManager"
    }

    fun files(path: String) : ArrayList<cz.levinzonr.filemanager.model.File> {
        val dir = File(path)
        Log.d(TAG, path)
        val list = ArrayList<cz.levinzonr.filemanager.model.File>()
        for (file in dir.listFiles()) {
            list.add(cz.levinzonr.filemanager.model.File.fromFile(file))
        }
       return list
    }

}
package cz.levinzonr.filemanager.model

import android.util.Log
import java.io.File

class DataManager {

    companion object {
        const val TAG = "DataManager"
    }

    fun getFilesFromDirectory(path: String) : ArrayList<cz.levinzonr.filemanager.model.File> {
        val dir = File(path)
        Log.d(TAG, "Getting files from $path on ${Thread.currentThread().name}")
        val list = ArrayList<cz.levinzonr.filemanager.model.File>()
        for (file in dir.listFiles()) {
            list.add(cz.levinzonr.filemanager.model.File.fromFile(file))
        }
       return list
    }

    fun removeFile(file: File) : cz.levinzonr.filemanager.model.File {
        Log.d(TAG, "Deleting ${file.name} thread: ${Thread.currentThread().name}")
        file.deleteRecursively()
        return cz.levinzonr.filemanager.model.File.fromFile(file)
    }

}
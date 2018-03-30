package cz.levinzonr.filemanager.model

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File

class DataManager {

    companion object {
        const val TAG = "DataManager"
        val ROOT_DIR = Environment.getRootDirectory().absolutePath
    }

    fun files(path: String = ROOT_DIR) : ArrayList<cz.levinzonr.filemanager.model.File> {
        val dir = File(path)
        Log.d(TAG, ROOT_DIR)
        val list = ArrayList<cz.levinzonr.filemanager.model.File>()
        for (file in dir.listFiles()) {
            Log.d(TAG, file.name )
            list.add(cz.levinzonr.filemanager.model.File.fromFile(file))
        }
        return list
    }

}
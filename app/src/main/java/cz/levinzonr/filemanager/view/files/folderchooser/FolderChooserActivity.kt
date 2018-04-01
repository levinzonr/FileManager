package cz.levinzonr.filemanager.view.files.folderchooser

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.view.files.BaseFileListFragment

import kotlinx.android.synthetic.main.activity_folder_chooser.*

class FolderChooserActivity : AppCompatActivity(), BaseFileListFragment.OnFilesFragmentInteraction {

    companion object {

        const val ACTION = "FolderChoose"
        const val ARG_PATH = "InitialPath"
        const val RESULT_OK = 1
        const val RESULT_CANCELED = 0


        fun startForResult(appCompatActivity: AppCompatActivity, string: String, rc: Int) {
            val intent = Intent(appCompatActivity, FolderChooserActivity::class.java)
            intent.putExtra(ARG_PATH, string)
            appCompatActivity.startActivityForResult(intent, rc )
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_chooser)
        setSupportActionBar(toolbar)
        val path = intent.extras.getString(ARG_PATH)
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, BaseFileListFragment.newInstance(path))
                    .commit()

    }

    override fun onFileSelected(file: File) {
        Toast.makeText(this, "not a folder", Toast.LENGTH_SHORT).show()
    }

    override fun onDirectorySelected(file: File) {
        val result = Intent(ACTION, Uri.parse(file.path))
        setResult(RESULT_OK, result)
        finish()
    }
}

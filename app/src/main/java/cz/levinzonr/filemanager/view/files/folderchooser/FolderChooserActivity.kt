package cz.levinzonr.filemanager.view.files.folderchooser

import android.app.FragmentTransaction
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.view.files.BaseFileListFragment

import kotlinx.android.synthetic.main.activity_folder_chooser.*
import kotlinx.android.synthetic.main.content_folder_chooser.*

class FolderChooserActivity : AppCompatActivity(), BaseFileListFragment.OnFilesFragmentInteraction {

    private lateinit var path: String

    companion object {

        const val ACTION = "FolderChoose"
        const val ARG_PATH = "InitialPath"
        const val RESULT_OK = 1
        const val RESULT_CANCELED = 0
        const val SAVED_PATH = "SavedPathh"


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

        path = intent.extras.getString(ARG_PATH)
        if (savedInstanceState == null)
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, BaseFileListFragment.newInstance(path))
                    .commit()

        else
            path = savedInstanceState.getString(SAVED_PATH)

        button_confirm.setOnClickListener({
            val result = Intent(ACTION, Uri.parse(path))
            setResult(RESULT_OK, result)
            finish()
        })

    }

    override fun onFileSelected(file: File) {
        Toast.makeText(this, "not a folder", Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(SAVED_PATH, path)
    }

    override fun onDirectorySelected(file: File) {
        path = file.path
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, BaseFileListFragment.newInstance(path))
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                .commit()
    }


    override fun onUpButtonClikced(path: String) {
        this.path = path
    }
}

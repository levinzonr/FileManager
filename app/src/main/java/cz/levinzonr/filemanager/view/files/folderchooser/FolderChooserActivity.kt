package cz.levinzonr.filemanager.view.files.folderchooser

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cz.levinzonr.filemanager.R

import kotlinx.android.synthetic.main.activity_folder_chooser.*

class FolderChooserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_chooser)
        setSupportActionBar(toolbar)

    }

}

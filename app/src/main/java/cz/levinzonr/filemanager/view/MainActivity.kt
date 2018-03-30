package cz.levinzonr.filemanager.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.view.fileslist.FilesListFragment

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FilesListFragment.OnFilesFragmentInteraction {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null)
            loadFragment(Environment.getRootDirectory().absolutePath, false)
    }

    private fun loadFragment(path: String, withBackStack: Boolean= true) {
        Log.d(TAG, "Mountig fragment: $path, add: $withBackStack")
        val tr = supportFragmentManager.
                beginTransaction()
                .replace(R.id.container, FilesListFragment.newInstance(path))
        if (withBackStack)
            tr.addToBackStack(null)

        tr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        tr.commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onFileSelected(file: File) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.fromFile(java.io.File(file.path))
        startActivity(Intent.createChooser(intent, "G"))

    }

    override fun onDirectorySelected(file: File) {
        loadFragment(file.path)
    }
}

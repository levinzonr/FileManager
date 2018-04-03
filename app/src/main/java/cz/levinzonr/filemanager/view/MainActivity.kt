package cz.levinzonr.filemanager.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v4.provider.DocumentFile
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.MimeTypeMap
import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.helpers.PreferenceHelper
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.view.files.fileslist.FileListCabFragment
import cz.levinzonr.filemanager.view.files.BaseFileListFragment
import cz.levinzonr.filemanager.view.preferences.PreferencesActivity
import cz.levinzonr.filemanager.view.welcome.WelcomeActivity

import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import android.content.ActivityNotFoundException
import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.FileProvider
import cz.levinzonr.filemanager.BuildConfig
import java.nio.file.Files.isDirectory



class MainActivity : AppCompatActivity(), BaseFileListFragment.OnFilesFragmentInteraction {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        } else {
            if (savedInstanceState == null)
                loadFragment(PreferenceHelper.getInstance(this).defaultPath(), false)
        }
    }

    private fun loadFragment(path: String, withBackStack: Boolean= true) {
        Log.d(TAG, "Mountig fragment: $path, add: $withBackStack")
        val tr = supportFragmentManager.
                beginTransaction()
                .replace(R.id.container, FileListCabFragment.newInstance(path))
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
            R.id.action_settings -> { PreferencesActivity.startAsIntent(this); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onFileSelected(file: File) {
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.provider", java.io.File(file.path))
            } else {
                Uri.fromFile(java.io.File(file.path))
            }
            Intent().apply {
                action = Intent.ACTION_VIEW
                setDataAndType(uri, MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.type))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                if (resolveActivity(packageManager) != null) {
                    startActivity(this)
                } else {
                    Toast.makeText(this@MainActivity, R.string.no_app_found, Toast.LENGTH_SHORT).show()
                }
            }
    }



    override fun onDirectorySelected(file: File) {
        loadFragment(file.path)
    }

    override fun onUpButtonClikced(path: String) {
    }



}

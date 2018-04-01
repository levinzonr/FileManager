package cz.levinzonr.filemanager.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.MimeTypeMap
import android.widget.Toast
import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.view.files.fileslist.FileListCabFragment
import cz.levinzonr.filemanager.view.files.BaseFileListFragment
import cz.levinzonr.filemanager.view.preferences.PreferencesActivity

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BaseFileListFragment.OnFilesFragmentInteraction {

    companion object {
        const val TAG = "MainActivity"
        const val RC_PERMISTION_ACCESS = 1221
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "No permission", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE), RC_PERMISTION_ACCESS)

        } else {

            if (savedInstanceState == null)
                loadFragment(Environment.getExternalStorageDirectory().absolutePath, false)
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
        val intent = Intent()
        intent.type = file.type
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.fromFile(java.io.File(file.path))
        intent.type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.type)
        Log.d(TAG, "intentType: ${intent.type}")
        startActivity(Intent.createChooser(intent, "Open with"))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_PERMISTION_ACCESS) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Log.d(TAG, "Granter")
            } else {
                Log.d(TAG, "Denied")
            }
        }
        Log.d(TAG, grantResults.toString())
    }

    override fun onDirectorySelected(file: File) {
        loadFragment(file.path)
    }

}

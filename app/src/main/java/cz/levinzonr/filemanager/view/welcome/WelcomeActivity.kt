package cz.levinzonr.filemanager.view.welcome

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.Manifest
import android.content.Intent
import android.os.PersistableBundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.widget.Toast
import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.view.MainActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    companion object {
        const val TAG = "WelcomeActivity"
        const val RC_PERMISTION_ACCESS = 1221
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        requestPermission()
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE), RC_PERMISTION_ACCESS)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_PERMISTION_ACCESS) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                Log.d(TAG, "Granter")
            } else {
                Snackbar.make(welcome_view, R.string.welcome_message, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.action_continue, {
                            requestPermission()
                        }).show()
            }
        }
        Log.d(TAG, grantResults.toString())
    }

}

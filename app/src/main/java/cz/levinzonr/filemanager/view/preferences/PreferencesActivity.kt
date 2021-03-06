package cz.levinzonr.filemanager.view.preferences

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.helpers.PreferenceHelper
import cz.levinzonr.filemanager.presenter.SettingsPresenter
import cz.levinzonr.filemanager.view.files.folderchooser.FolderChooserActivity

import kotlinx.android.synthetic.main.activity_preferences.*

class PreferencesActivity : AppCompatActivity(), PreferencesFragment.OnPreferenceFragmentInteraction {

    private lateinit var presenter: SettingsPresenter

    companion object {

        const val RC_CODE = 1123

        fun startAsIntent(context: Context) {
            val intent = Intent(context, PreferencesActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        setSupportActionBar(toolbar)

        val fragment = PreferencesFragment()
        presenter = SettingsPresenter(application)
        presenter.onAttach(fragment)

        supportFragmentManager.beginTransaction()
                .replace(R.id.container,fragment)
                .commit()

    }

    override fun onSelect() {
        FolderChooserActivity.startForResult(this, PreferenceHelper(this).defaultPath(),RC_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_CODE) {
            if (resultCode == FolderChooserActivity.RESULT_OK) {
                presenter.setDefaultDir(data?.data.toString())
                Toast.makeText(this, data?.data.toString(), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "canceled", Toast.LENGTH_SHORT).show()

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}

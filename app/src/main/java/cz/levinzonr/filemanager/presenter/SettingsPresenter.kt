package cz.levinzonr.filemanager.presenter

import android.app.Application
import cz.levinzonr.filemanager.helpers.SharedPreferencesHelpers
import cz.levinzonr.filemanager.view.preferences.PreferencesMvpView

class SettingsPresenter(application: Application) : BasePresenter<PreferencesMvpView> {
    private var prefs = SharedPreferencesHelpers(application)
    private var view : PreferencesMvpView? = null

    override fun onAttach(view: PreferencesMvpView) {
        this.view = view
    }

    fun setDefaultDir(path: String) {
        prefs.updatePath(path)
        view?.onDirectoryChanged(path)
    }

    override fun onDetach() {
        view = null
    }
}
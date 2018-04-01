package cz.levinzonr.filemanager.presenter

import android.app.Application
import cz.levinzonr.filemanager.helpers.PreferenceHelper
import cz.levinzonr.filemanager.view.preferences.PreferencesMvpView

class SettingsPresenter(val application: Application) : BasePresenter<PreferencesMvpView> {
    private var view : PreferencesMvpView? = null

    override fun onAttach(view: PreferencesMvpView) {
        this.view = view
    }

    fun setDefaultDir(path: String) {
        PreferenceHelper.getInstance(application).updatePath(path)
        view?.onDirectoryChanged(path)
    }

    override fun onDetach() {
        view = null
    }
}
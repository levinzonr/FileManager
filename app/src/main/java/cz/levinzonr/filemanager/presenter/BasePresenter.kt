package cz.levinzonr.filemanager.presenter

interface BasePresenter<in V> {

    fun onAttach(view: V)

    fun onDetach()
}
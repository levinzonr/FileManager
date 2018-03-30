package cz.levinzonr.filemanager.presenter

interface Presenter<in V> {

    fun onAttach(view: V)

    fun onDetach()
}
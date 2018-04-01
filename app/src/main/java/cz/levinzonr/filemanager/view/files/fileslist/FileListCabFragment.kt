package cz.levinzonr.filemanager.view.files.fileslist


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.Toast

import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.presenter.FilesListCabPresenter
import cz.levinzonr.filemanager.view.files.BaseFileListFragment
import kotlinx.android.synthetic.main.activity_main.*

class FileListCabFragment : BaseFileListFragment(), FileListCabView {

    private lateinit var actionMode: ActionMode

    companion object {

        const val ARG_PATH = "FilePath"
        const val TAG = "FileListCabFragment"
        const val SAVED_ACTIONMODE = "ActionMode"
        const val SAVED_SELECTED = "SelectedItems"

        fun newInstance(path: String): FileListCabFragment {
            Log.d(TAG, "New Instacne")
            val fragment = FileListCabFragment()
            val bundle = Bundle()
            bundle.putString(ARG_PATH, path)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        path = arguments.getString(ARG_PATH)
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_files_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            val active = savedInstanceState.getBoolean(SAVED_ACTIONMODE)
            if (active) {
                presenter().restoreActionMode(savedInstanceState.getIntegerArrayList(SAVED_SELECTED))
            }
        }
    }

    override fun initPresenter() {
       presenter = FilesListCabPresenter()
    }

    override fun presenter(): FilesListCabPresenter {
        return presenter as FilesListCabPresenter
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
          outState?.putBoolean(SAVED_ACTIONMODE, presenter().isActionModeActive)
         outState?.putIntegerArrayList(SAVED_SELECTED, presenter().checked)
    }



    override fun onFilesDeleted() {
        Toast.makeText(context, "files deleted", Toast.LENGTH_SHORT).show()

    }

    override fun onFileDeleted(num: Int, max: Int) {
        adapter.notifyDataSetChanged()
        Toast.makeText(context, "${num + 1}/$max files deleted", Toast.LENGTH_SHORT).show()
    }

    override fun startActionMode() {
        (activity as AppCompatActivity).toolbar.startActionMode(object : ActionMode.Callback {
            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                return when (p1?.itemId) {
                    R.id.action_delete -> {
                        presenter().performDeletion()
                        return true
                    }
                    else -> false
                }
            }

            override fun onCreateActionMode(p0: ActionMode?, menu: Menu?): Boolean {
                activity.menuInflater.inflate(R.menu.menu_fileslist_context, menu)
                actionMode = p0!!
                return true
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }

            override fun onDestroyActionMode(p0: ActionMode?) {
                presenter().onActionModeDestroy()
            }
        })
    }

    override fun destroyActionMode() {
        actionMode.finish()
    }



    override fun updateActionMode(itemsCount: Int) {
        adapter.notifyDataSetChanged()
        actionMode.title = context.getString(R.string.checked_files, itemsCount)
    }

    override fun onLoadingFinished(items: ArrayList<File>) {
        super.onLoadingFinished(items)
        (context as AppCompatActivity).supportActionBar?.title = path.substringAfterLast("/")
        (context as AppCompatActivity).supportActionBar?.subtitle = path.substringBeforeLast("/")
    }

}

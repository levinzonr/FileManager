package cz.levinzonr.filemanager.view.fileslist


import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast

import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.presenter.FilesListPresenter
import cz.levinzonr.filemanager.view.folderchooser.FileExplorerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_files_list.*
import kotlinx.android.synthetic.main.fragment_files_list.view.*

class FilesListFragment : Fragment(), FileListMvpView {

    private lateinit var adapter: FilesListAdapter
    private lateinit var listener: OnFilesFragmentInteraction

    private lateinit var presenter: FilesListPresenter
    private lateinit var path: String
    private lateinit var progressView: View

    private var updateButton: MenuItem? = null
    private lateinit var actionMode: ActionMode

    interface OnFilesFragmentInteraction {
        fun onFileSelected(file: File)
        fun onDirectorySelected(file: File)
    }

    companion object {

        const val ARG_PATH = "FilePath"
        const val TAG = "FilesListFragment"
        const val SAVED_ACTIONMODE = "ActionMode"
        const val SAVED_SELECTED = "SelectedItems"

        fun newInstance(path: String): FilesListFragment {
            Log.d(TAG, "New Instacne")
            val fragment = FilesListFragment()
            val bundle = Bundle()
            bundle.putString(ARG_PATH, path)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as OnFilesFragmentInteraction
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        path = arguments.getString(ARG_PATH)
        (context as AppCompatActivity).supportActionBar?.title = path.substringAfterLast("/")
        (context as AppCompatActivity).supportActionBar?.subtitle = path.substringBeforeLast("/")
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_files_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: $path")
        presenter = FilesListPresenter()
        presenter.onAttach(this)
        presenter.getFilesInFolder(path)
        adapter = FilesListAdapter(context, presenter)
        recycler_view.adapter = adapter


        val columnsCnt = context.resources.getInteger(R.integer.grid_column_cnt)
        recycler_view.layoutManager = GridLayoutManager(context, columnsCnt)
        if (columnsCnt == 1) {
            recycler_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        if (savedInstanceState != null) {
            val actionModeActive = savedInstanceState.getBoolean(SAVED_ACTIONMODE)
            if (actionModeActive) {
                presenter.restoreActionMode(savedInstanceState.getIntegerArrayList(SAVED_SELECTED))
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_fileslist, menu)
        updateButton = menu?.findItem(R.id.action_refresh)
        progressView = LayoutInflater.from(context).inflate(R.layout.content_progress_bar, null)
        progressView.progress_bar.indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
          outState?.putBoolean(SAVED_ACTIONMODE, presenter.isActionModeActive)
         outState?.putIntegerArrayList(SAVED_SELECTED, presenter.checked)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_refresh -> {
                presenter.getFilesInFolder(path)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onLoadingStart() {
        Log.d(TAG, "onLoadingStart")
        progress_bar.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
        error_layout.visibility = View.GONE
        updateButton?.actionView = progressView

    }

    override fun onLoadingFinished(items: ArrayList<File>) {
        Log.d(TAG, "onLoadingFinshed")
        progress_bar.visibility = View.GONE
        recycler_view.visibility = View.VISIBLE
        error_layout.visibility = View.GONE
        updateButton?.actionView = null
    }

    override fun onError(e: String) {
        Log.d(TAG, "onError: $e")
        progress_bar.visibility = View.GONE
        recycler_view.visibility = View.GONE
        error_layout.visibility = View.VISIBLE
        updateButton?.actionView = null
    }

    override fun onDestroy() {
        val path = arguments.getString(ARG_PATH)
        Log.d(TAG, "onDestroy: $path")
        super.onDestroy()
        presenter.onDetach()
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
                        presenter.performDeletion()
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
                presenter.onActionModeDestroy()
            }
        })
    }

    override fun destroyActionMode() {
        actionMode.finish()
    }


    override fun onFolderSelected(file: File) {
        listener.onDirectorySelected(file)
    }


    override fun onFileSelected(file: File) {
        listener.onFileSelected(file)
    }

    override fun updateActionMode(itemsCount: Int) {
        adapter.notifyDataSetChanged()
        actionMode.title = context.getString(R.string.checked_files, itemsCount)
    }

}

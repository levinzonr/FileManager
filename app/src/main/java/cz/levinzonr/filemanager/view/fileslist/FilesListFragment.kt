package cz.levinzonr.filemanager.view.fileslist


import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.os.EnvironmentCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast

import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.presenter.FilesListPresenter
import cz.levinzonr.filemanager.view.ViewCallbacks
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_files_list.*
import kotlinx.android.synthetic.main.fragment_files_list.view.*

class FilesListFragment : Fragment(), FilesListAdapter.OnItemClickListener, ViewCallbacks {

    private lateinit var adapter: FilesListAdapter
    private lateinit var listener: OnFilesFragmentInteraction

    private lateinit var presenter: FilesListPresenter
    private lateinit var path: String

    private var updateButton: MenuItem? = null
    private lateinit var progressView: View

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

        fun newInstance(path: String) : FilesListFragment {
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
                              savedInstanceState: Bundle?): View?{
         path = arguments.getString(ARG_PATH)
        (context as AppCompatActivity).supportActionBar?.title = path.split("/").last()
        (context as AppCompatActivity).supportActionBar?.subtitle = path
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_files_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: $path")
        adapter = FilesListAdapter(context, this)
        recycler_view.adapter = adapter
        presenter = FilesListPresenter()
        presenter.onAttach(this)
        presenter.getFilesInFolder(path)

        val columnsCnt = context.resources.getInteger(R.integer.grid_column_cnt)
        recycler_view.layoutManager = GridLayoutManager(context, columnsCnt)
        if (columnsCnt == 1) {
            recycler_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        if (savedInstanceState != null) {
            val actionModeActive = savedInstanceState.getBoolean(SAVED_ACTIONMODE)
            if( actionModeActive) {
                startActionMode()
                adapter.checked = savedInstanceState.getIntegerArrayList(SAVED_SELECTED)
                actionMode.title = context.getString(R.string.checked_files, adapter.checked.size)
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
        outState?.putBoolean(SAVED_ACTIONMODE, adapter.actionMode)
        outState?.putIntegerArrayList(SAVED_SELECTED, adapter.checked)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.action_refresh -> {
                presenter.getFilesInFolder(path)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(file: File) {
        if (file.isDirectory) listener.onDirectorySelected(file)
        else listener.onFileSelected(file)
    }

    override fun onItemChecked(position: Int) {
        adapter.toggleSelection(position)
        actionMode.title = context.getString(R.string.checked_files, adapter.checked.size)
        if (adapter.checked.size == 0) {
            actionMode.finish()
        }
    }

    override fun onItemLongClick(position: Int) {
        startActionMode()
        onItemChecked(position)
    }

    private fun startActionMode() {
        (activity as AppCompatActivity).toolbar.startActionMode(object : ActionMode.Callback {
            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                return when(p1?.itemId) {
                    R.id.action_delete -> {
                        val list: List<File> = adapter.checked.map { adapter.items[it]  }
                        presenter.removeFiles(ArrayList(list))
                        actionMode.finish()
                        return true
                    }
                    else -> false
                }
            }

            override fun onCreateActionMode(p0: ActionMode?, menu: Menu?): Boolean {
                activity.menuInflater.inflate(R.menu.menu_fileslist_context, menu)
                if (p0 != null) {
                    actionMode = p0
                    return true
                }
                return false
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }

            override fun onDestroyActionMode(p0: ActionMode?) {
                adapter.onActionModeDestroyed()
            }
        })
        adapter.actionMode = true
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
        adapter.items = items
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

    override fun onFileDeleted(num: Int, max: Int, file: File) {
        Toast.makeText(context, "${num+1}/$max files deleted", Toast.LENGTH_SHORT).show()
        adapter.remove(file)
    }
}

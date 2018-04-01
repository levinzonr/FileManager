package cz.levinzonr.filemanager.view.files


import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.*

import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.presenter.FilesPresenter
import kotlinx.android.synthetic.main.fragment_files_list.*
import kotlinx.android.synthetic.main.fragment_files_list.view.*

abstract class BaseFileListFragment : Fragment(), BaseFileListView {

    protected lateinit var adapter: FilesListAdapter
    private lateinit var listener: OnFilesFragmentInteraction

    protected lateinit var presenter: FilesPresenter
    protected lateinit var path: String
    private lateinit var progressView: View

    private var updateButton: MenuItem? = null

    interface OnFilesFragmentInteraction {
        fun onFileSelected(file: File)
        fun onDirectorySelected(file: File)
    }

    companion object {

        const val ARG_PATH = "FilePath"
        const val TAG = "AbstractFilesListFrag"
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as OnFilesFragmentInteraction
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: $path")
        initPresenter()
        presenter.onAttach(this)
        presenter.getFilesInFolder(path)
        adapter = FilesListAdapter(context, presenter)
        recycler_view.adapter = adapter


        val columnsCnt = context.resources.getInteger(R.integer.grid_column_cnt)
        recycler_view.layoutManager = GridLayoutManager(context, columnsCnt)
        if (columnsCnt == 1) {
            recycler_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    abstract fun initPresenter()

    abstract fun presenter() : FilesPresenter

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_fileslist, menu)
        updateButton = menu?.findItem(R.id.action_refresh)
        progressView = LayoutInflater.from(context).inflate(R.layout.content_progress_bar, null)
        progressView.progress_bar.indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)

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


    override fun onFolderSelected(file: File) {
        listener.onDirectorySelected(file)
    }


    override fun onFileSelected(file: File) {
        listener.onFileSelected(file)
    }



}

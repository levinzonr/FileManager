package cz.levinzonr.filemanager.view.files


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

import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.presenter.FilesPresenter
import cz.levinzonr.filemanager.view.files.fileslist.FileListCabFragment
import kotlinx.android.synthetic.main.fragment_files_list.*
import kotlinx.android.synthetic.main.fragment_files_list.view.*

open class BaseFileListFragment : Fragment(), BaseFileListView {

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

        fun newInstance(path: String): BaseFileListFragment {
            Log.d(TAG, "New Instacne")
            val fragment = BaseFileListFragment()
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
        path = arguments.getString(FileListCabFragment.ARG_PATH)
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_files_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: $path")
        initPresenter()
        presenter.onAttach(this)
        presenter.getFilesInFolder(path)
        adapter = FilesListAdapter(context, presenter)
        recycler_view.adapter = adapter
        button_back.setOnClickListener({
            if (activity.supportFragmentManager.backStackEntryCount == 0) {
                val parent = java.io.File(path).parentFile
                path = parent.path
                arguments.putString(ARG_PATH, path)
                presenter.getFilesInFolder(path)
            } else {
                activity.onBackPressed()
            }
        })


        val columnsCnt = context.resources.getInteger(R.integer.grid_column_cnt)
        recycler_view.layoutManager = GridLayoutManager(context, columnsCnt)
        if (columnsCnt == 1) {
            recycler_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    open fun initPresenter() {
        presenter = FilesPresenter()
    }

    open fun presenter() : FilesPresenter {
        return presenter
    }

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

    override fun setParentButton(enabled: Boolean) {
        button_back.isEnabled = enabled
        if (enabled) button_back.visibility = View.VISIBLE
        else button_back.visibility = View.GONE
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
        adapter.notifyDataSetChanged()
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

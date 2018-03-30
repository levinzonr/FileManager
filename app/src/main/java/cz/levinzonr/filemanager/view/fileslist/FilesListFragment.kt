package cz.levinzonr.filemanager.view.fileslist


import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.os.EnvironmentCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.presenter.FilesListPresenter
import cz.levinzonr.filemanager.view.ViewCallbacks
import kotlinx.android.synthetic.main.fragment_files_list.*

class FilesListFragment : Fragment(), FilesListAdapter.OnItemClickListener, ViewCallbacks {

    private lateinit var adapter: FilesListAdapter
    private lateinit var listener: OnFilesFragmentInteraction

    private lateinit var presenter: FilesListPresenter

    interface OnFilesFragmentInteraction {
        fun onFileSelected(file: File)
        fun onDirectorySelected(file: File)
    }

    companion object {

        const val ARG_PATH = "FilePath"
        const val TAG = "FilesListFragment"

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
        val path = arguments.getString(ARG_PATH)
        (context as AppCompatActivity).supportActionBar?.title = path.split("/").last()
        (context as AppCompatActivity).supportActionBar?.subtitle = path

        return inflater.inflate(R.layout.fragment_files_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val path = arguments.getString(ARG_PATH)

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
    }

    override fun onItemClick(file: File) {
        if (file.isDirectory) listener.onDirectorySelected(file)
        else listener.onFileSelected(file)
    }

    override fun onLoadingStart() {
        Log.d(TAG, "onLoadingStart")
        progress_bar.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
        error_layout.visibility = View.GONE
    }

    override fun onLoadingFinished(items: ArrayList<File>) {
        Log.d(TAG, "onLoadingFinshed")
        progress_bar.visibility = View.GONE
        recycler_view.visibility = View.VISIBLE
        adapter.items = items
        error_layout.visibility = View.GONE
    }

    override fun onError(e: String) {
        Log.d(TAG, "onError: $e")
        progress_bar.visibility = View.GONE
        recycler_view.visibility = View.GONE
        error_layout.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        val path = arguments.getString(ARG_PATH)
        Log.d(TAG, "onDestroy: $path")
        super.onDestroy()
        presenter.onDetach()
    }


}

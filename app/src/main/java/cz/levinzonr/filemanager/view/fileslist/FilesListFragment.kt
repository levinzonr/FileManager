package cz.levinzonr.filemanager.view.fileslist


import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.model.DataManager
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.presenter.FilesListPresenter
import cz.levinzonr.filemanager.presenter.Presenter
import cz.levinzonr.filemanager.view.MainActivity
import cz.levinzonr.filemanager.view.ViewCallbacks
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_files_list.*

class FilesListFragment : Fragment(), FilesLinearAdapter.OnItemClickListener, ViewCallbacks {

    private lateinit var adapter: FilesLinearAdapter
    private lateinit var frameLayout: FrameLayout
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

        presenter = FilesListPresenter()
        presenter.onAttach(this)
        val path = arguments.getString(ARG_PATH)
        (context as AppCompatActivity).supportActionBar?.title = path.split("/").last()
        (context as AppCompatActivity).supportActionBar?.subtitle = path

        return inflater.inflate(R.layout.fragment_files_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FilesLinearAdapter(context, this)
        recycler_view.adapter = adapter

        val path = arguments.getString(ARG_PATH)
        presenter.getFilesInFolder(path)

        val columnsCnt = context.resources.getInteger(R.integer.grid_column_cnt)
        recycler_view.layoutManager = GridLayoutManager(context, columnsCnt)
        if (columnsCnt == 1) {
            recycler_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        Log.d(TAG, "onViewCreated")
    }

    override fun onItemClick(file: File) {
        if (file.isDirectory) listener.onDirectorySelected(file)
        else listener.onFileSelected(file)
    }

    override fun onLoadingStart() {
        Log.d(TAG, "onLoadingStart")
        progress_bar.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
    }

    override fun onLoadingFinished(items: ArrayList<File>) {
        Log.d(TAG, "onLoadingFinshed")
        progress_bar.visibility = View.GONE
        recycler_view.visibility = View.VISIBLE
        adapter.items = items
    }

    override fun onError(e: String) {
        Log.d(TAG, "onError: $e")
        progress_bar.visibility = View.GONE
        recycler_view.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }



}

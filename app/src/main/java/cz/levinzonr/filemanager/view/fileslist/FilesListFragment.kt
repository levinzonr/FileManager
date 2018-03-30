package cz.levinzonr.filemanager.view.fileslist


import android.content.Context
import android.os.Bundle
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
import cz.levinzonr.filemanager.view.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_files_list.*

class FilesListFragment : Fragment(), FilesLinearAdapter.OnItemClickListener {

    private lateinit var adapter: FilesLinearAdapter
    private lateinit var frameLayout: FrameLayout
    private lateinit var listener: OnFilesFragmentInteraction

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
        val path = arguments.getString(ARG_PATH)
        (context as AppCompatActivity).supportActionBar?.title = path.split("/").last()
        (context as AppCompatActivity).supportActionBar?.subtitle = path

        return inflater.inflate(R.layout.fragment_files_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FilesLinearAdapter(context, this)
        val path = arguments.getString(ARG_PATH)
        if (path == "") adapter.items = ArrayList(DataManager().files())
        else adapter.items = ArrayList( DataManager().files(path) )
        recycler_view.adapter = adapter
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
}

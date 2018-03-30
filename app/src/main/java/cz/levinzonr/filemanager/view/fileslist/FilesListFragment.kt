package cz.levinzonr.filemanager.view.fileslist


import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast

import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.model.DataManager
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.model.MockData
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_files_list.*

class FilesListFragment : Fragment() {

    private lateinit var adapter: FilesLinearAdapter
    private lateinit var frameLayout: FrameLayout

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_files_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FilesLinearAdapter(context)
        adapter.items = ArrayList( DataManager().files() )
        recycler_view.adapter = adapter
        val columnsCnt = context.resources.getInteger(R.integer.grid_column_cnt)
        recycler_view.layoutManager = GridLayoutManager(context, columnsCnt)
        if (columnsCnt == 1) {
            recycler_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        Log.d(TAG, "onViewCreated")
    }


}

package cz.levinzonr.filemanager.view.fileslist


import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
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
        adapter = FilesLinearAdapter()
        adapter.items = ArrayList( MockData.data() )
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context)
        Log.d(TAG, "onViewCreated")
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        Log.d(TAG, "OnConfigChange")
        super.onConfigurationChanged(newConfig)
        recycler_view.recycledViewPool.clear()
        if (newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recycler_view.layoutManager = LinearLayoutManager(context)
        } else {
            recycler_view.layoutManager = GridLayoutManager(context, 4)
        }
    }

}

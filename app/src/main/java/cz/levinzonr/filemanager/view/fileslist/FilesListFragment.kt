package cz.levinzonr.filemanager.view.fileslist


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.model.File
import cz.levinzonr.filemanager.model.MockData
import kotlinx.android.synthetic.main.fragment_files_list.*

class FilesListFragment : Fragment() {

    private lateinit var adapter: FilesLinearAdapter

    interface OnFilesFragmentInteraction {
        fun onFileSelected(file: File)
        fun onDirectorySelected(file: File)
    }

    companion object {

        const val ARG_PATH = "FilePath"

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
    }

}

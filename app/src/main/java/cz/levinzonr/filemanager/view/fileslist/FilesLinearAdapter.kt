package cz.levinzonr.filemanager.view.fileslist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.model.File
import kotlinx.android.synthetic.main.item_file.view.*

class FilesLinearAdapter : RecyclerView.Adapter<FilesLinearAdapter.ViewHolder>() {
    var items = ArrayList<File>()

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bindView(file: File) {
            view.file_name.text = file.path
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        return ViewHolder(inflater.inflate(R.layout.item_file, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindView(items[position])
    }
}
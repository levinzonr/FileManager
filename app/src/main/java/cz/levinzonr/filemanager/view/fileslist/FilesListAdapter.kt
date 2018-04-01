package cz.levinzonr.filemanager.view.fileslist

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.presenter.FilesListPresenter
import cz.levinzonr.filemanager.presenter.FilesPresenter
import kotlinx.android.synthetic.main.item_file.view.*

class FilesListAdapter(val context:Context, val presenter: FilesPresenter) :
        RecyclerView.Adapter<FilesListAdapter.ViewHolder>() {


    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) , RecyclerItemView {

        override fun setFileView(name: String) {
            view.file_name.text = name
            view.file_icon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_description_black_48dp))
            view.file_icon.setColorFilter(Color.LTGRAY)
        }

        override fun setFolderView(name: String) {
            view.file_name.text = name
            view.file_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_folder_black_48dp))
            view.file_icon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))    }

        override fun setCheckedView(name: String) {
            view.file_name.text = name
            view.file_icon.setImageResource(R.drawable.ic_check_circle_black_48dp)
            view.file_icon.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        return ViewHolder(inflater.inflate(R.layout.item_file, parent, false))
    }

    override fun getItemCount(): Int = presenter.itemsCount()

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.view?.setOnClickListener{ presenter.onFileSelected(position) }
        holder?.view?.setOnLongClickListener{presenter.onLongClick(position); true}
        presenter.bindItemAtPosition(position, holder!!)
    }




}
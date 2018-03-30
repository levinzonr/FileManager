package cz.levinzonr.filemanager.view.fileslist

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.model.File
import kotlinx.android.synthetic.main.item_file.view.*

class FilesListAdapter(val context:Context, val listener: OnItemClickListener) : RecyclerView.Adapter<FilesListAdapter.ViewHolder>() {
    var items = ArrayList<File>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    var actionMode: Boolean = false
    var checked = ArrayList<Int>()

    interface OnItemClickListener {

        fun onItemClick(file: File)
        fun onItemChecked(position: Int)
        fun onItemLongClick(position: Int)
    }


    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bindView(file: File, position: Int) {
            view.setOnClickListener({
                if(actionMode) listener.onItemChecked(position)
                else listener.onItemClick(file)
            })
            view.setOnLongClickListener({
                if (!actionMode) {
                    actionMode = true
                    listener.onItemLongClick(position)
                }
                true
            })
            view.file_name.text = file.name
            if (!file.isDirectory) {
                view.file_icon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_description_black_48dp))
                view.file_icon.setColorFilter(Color.LTGRAY)
            } else {
                view.file_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_folder_black_48dp))
                view.file_icon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
            }

            if (actionMode && checked.contains(position)) {
                view.file_icon.setImageResource(R.drawable.ic_check_circle_black_48dp)
                view.file_icon.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        return ViewHolder(inflater.inflate(R.layout.item_file, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindView(items[position], position)
    }

    fun toggleSelection(position: Int) {
        if (checked.contains(position))
            checked.remove(position)
        else
            checked.add(position)

        notifyDataSetChanged()
    }

    fun onActionModeDestroyed() {
        checked.clear()
        actionMode = false
        notifyDataSetChanged()
    }


}
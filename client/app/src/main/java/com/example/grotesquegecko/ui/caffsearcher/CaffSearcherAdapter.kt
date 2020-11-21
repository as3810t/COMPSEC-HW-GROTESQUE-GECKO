package com.example.grotesquegecko.ui.caffsearcher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.grotesquegecko.R
import com.example.grotesquegecko.ui.caffsearcher.models.CaffPreview
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_caff_searcher_list_row.view.*
import timber.log.Timber

class CaffSearcherAdapter(private val context: Context) :
    ListAdapter<CaffPreview, CaffSearcherAdapter.ViewHolder>(CaffComparator) {

    var listener: Listener? = null

    interface Listener {
        fun onItemSelected(id: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.fragment_caff_searcher_list_row,
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.item = item

        Picasso
            .with(context)
            .load(item.imageSource)
            .placeholder(R.drawable.icon_gecko)
            .into(holder.caffPreview)

        holder.mainTitle.text = item.title
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val caffPreview: ImageView = itemView.caffListCaffPreview
        val mainTitle: TextView = itemView.caffListTitle

        var item: CaffPreview? = null

        init {
            itemView.setOnClickListener {
                item.let {
                    it?.id?.let { caffId -> listener?.onItemSelected(caffId) }
                }
                Timber.d("Adapter itemSelected")
            }
        }
    }
}
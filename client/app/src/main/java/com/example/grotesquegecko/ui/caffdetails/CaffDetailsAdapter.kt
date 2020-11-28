package com.example.grotesquegecko.ui.caffdetails

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.grotesquegecko.R
import com.example.grotesquegecko.data.network.models.CaffComment
import kotlinx.android.synthetic.main.fragment_caff_details_comment.view.*

class CaffDetailsAdapter(private val context: Context) :
    ListAdapter<CaffComment, CaffDetailsAdapter.ViewHolder>(CommentComparator){

    var listener: Listener? = null

    interface Listener {
        fun onItemSelected(id: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaffDetailsAdapter.ViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(
                        R.layout.fragment_caff_details_comment,
                        parent,
                        false
                )
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CaffDetailsAdapter.ViewHolder, position: Int) {
        val item = getItem(position)

        holder.item = item

        holder.commentUser.text = item.userName
        if (item.content != null) {
            holder.commentText.text = item.content
        } else {
            holder.commentText.text = context.getString(R.string.deleted_comment)
            holder.commentText.setTypeface(null, Typeface.ITALIC)
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentUser: TextView = itemView.caffCommentUser
        val commentText: TextView = itemView.caffCommentText

        var item: CaffComment? = null
    }
}

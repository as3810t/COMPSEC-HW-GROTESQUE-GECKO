package com.example.grotesquegecko.ui.caffdetails

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.grotesquegecko.R
import com.example.grotesquegecko.data.network.models.CaffComment
import kotlinx.android.synthetic.main.fragment_caff_details_comment.view.*
import timber.log.Timber

class CaffDetailsAdapter(private val context: Context, private val userId: String) :
    ListAdapter<CaffComment, CaffDetailsAdapter.ViewHolder>(CommentComparator){

    var listener: Listener? = null

    interface Listener {
        fun onCommentEdit(id: String, content: String)
        fun onCommentDelete(id: String)
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
        if (item.userId != userId || item.content == null){
            holder.commentDelete.visibility = View.GONE
            holder.commentEdit.visibility = View.GONE
        } else {
            holder.commentDelete.visibility = View.VISIBLE
            holder.commentEdit.visibility = View.VISIBLE
            holder.commentEdit.setOnClickListener {
                val editCommentDialog = AlertDialog.Builder(context)
                        .setView(R.layout.fragment_edit_comment)
                        .show()
                editCommentDialog.findViewById<EditText>(R.id.editCommentDialogContent)
                        .setText(item.content, TextView.BufferType.EDITABLE)
                editCommentDialog.findViewById<ImageView>(R.id.editCommentDialogCloseButton)
                        .setOnClickListener {
                            editCommentDialog.dismiss()
                        }
                editCommentDialog.findViewById<Button>(R.id.editCommentDialogSendButton)
                        .setOnClickListener {
                            val commentField: EditText = editCommentDialog.findViewById(R.id.editCommentDialogContent)
                            val comment: String = commentField.text.toString().trim()

                            if (comment.isEmpty()) {
                                commentField.error = 
                                        context.getString(R.string.comment_fragment_comment_required)
                                commentField.requestFocus()
                                return@setOnClickListener
                            }
                            item.let {
                                it?.id?.let { commentId -> listener?.onCommentEdit(commentId, content = commentField.text.toString()) }
                            }
                            Timber.d("Adapter itemSelected")

                            editCommentDialog.dismiss()
                        }

            }
            holder.commentDelete.setOnClickListener {
                item.let {
                    it?.id?.let { commentId -> listener?.onCommentDelete(commentId) }
                }
                Timber.d("Adapter itemSelected")
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentUser: TextView = itemView.caffCommentUser
        val commentText: TextView = itemView.caffCommentText
        val commentDelete: ImageView = itemView.caffCommentDelete
        val commentEdit: ImageView = itemView.caffCommentEdit

        var item: CaffComment? = null
    }
}

package com.example.grotesquegecko.ui.caffdetails

import androidx.recyclerview.widget.DiffUtil
import com.example.grotesquegecko.data.network.models.CaffComment

object CommentComparator : DiffUtil.ItemCallback<CaffComment>() {
    override fun areItemsTheSame(oldItem: CaffComment, newItem: CaffComment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CaffComment, newItem: CaffComment): Boolean {
        return oldItem == newItem
    }
}
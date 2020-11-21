package com.example.grotesquegecko.ui.caffsearcher

import androidx.recyclerview.widget.DiffUtil
import com.example.grotesquegecko.ui.caffsearcher.models.CaffPreview

object CaffComparator : DiffUtil.ItemCallback<CaffPreview>() {

    override fun areItemsTheSame(oldItem: CaffPreview, newItem: CaffPreview): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CaffPreview, newItem: CaffPreview): Boolean {
        return oldItem == newItem
    }

}
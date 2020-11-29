package com.example.grotesquegecko.ui.allusers

import androidx.recyclerview.widget.DiffUtil
import com.example.grotesquegecko.data.network.models.UserData

object UserComparator : DiffUtil.ItemCallback<UserData>() {

    override fun areItemsTheSame(oldItem: UserData, newItem: UserData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserData, newItem: UserData): Boolean {
        return oldItem == newItem
    }

}
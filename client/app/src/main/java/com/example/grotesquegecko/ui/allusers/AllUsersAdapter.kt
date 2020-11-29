package com.example.grotesquegecko.ui.allusers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.grotesquegecko.R
import com.example.grotesquegecko.data.network.models.UserData
import kotlinx.android.synthetic.main.fragment_all_users_row.view.*
import timber.log.Timber


class AllUsersAdapter(private val context: Context) :
    ListAdapter<UserData, AllUsersAdapter.ViewHolder>(UserComparator) {

    var listener: Listener? = null

    interface Listener {
        fun onEditItemSelected(id: String)
        fun onDeleteItemSelected(id: String)
        fun onChangePasswordItemSelected(id: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.fragment_all_users_row,
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.item = item
        holder.userUsername.text = item.username
        holder.userEmail.text = item.email

        holder.editButton.setOnClickListener {
            item.let {
                it?.id?.let { userId ->
                    Timber.d(userId)
                    listener?.onEditItemSelected(userId)
                }
            }
        }
        holder.deleteButton.setOnClickListener {
            item.let {
                it?.id?.let { userId ->
                    Timber.d(userId)
                    listener?.onDeleteItemSelected(userId)
                }
            }
        }
        holder.changePasswordButton.setOnClickListener {
            item.let {
                it?.id?.let { userId ->
                    Timber.d(userId)
                    listener?.onChangePasswordItemSelected(userId)
                }
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userUsername: TextView = itemView.allUsersUserName
        val userEmail: TextView = itemView.allUsersUserEmail
        val editButton: ImageButton = itemView.allUsersUserEditIcon
        val deleteButton: ImageButton = itemView.allUsersUserDeleteIcon
        val changePasswordButton: ImageButton = itemView.allUsersUserChangePasswordIcon

        var item: UserData? = null
    }
}
package com.example.instagram.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.databinding.NotificationsItemLayoutBinding
import com.example.instagram.model.NotificationModel
import com.example.instagram.onclickinterface.NotificationOnClick

class NotificationAdapter (private var notification : List<NotificationModel> , var notificationOnClick: NotificationOnClick) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    class ViewHolder(var binding: NotificationsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        // fun initialize interface for onClick notification adapter..
        fun initialize(viewHolder: ViewHolder, notificationModel: NotificationModel , action: NotificationOnClick){
            action.onClick(viewHolder , notificationModel , adapterPosition)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        return ViewHolder( NotificationsItemLayoutBinding.inflate( LayoutInflater.from(viewGroup.context), viewGroup, false ))
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val notification = notification[position]


        // call fun initialize for onClick ...
        viewHolder.initialize(viewHolder , notification , notificationOnClick )

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = notification.size
}
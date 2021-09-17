package com.example.instagram.onclickinterface

import com.example.instagram.model.NotificationModel
import com.example.instagram.ui.adapter.NotificationAdapter

interface NotificationOnClick {

    fun onClick(viewHolder: NotificationAdapter.ViewHolder, notificationModel: NotificationModel, petition: Int)
}
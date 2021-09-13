package com.example.instagram.onclickinterface

import com.example.instagram.ui.adapter.UserAdapter
import com.example.instagram.model.UserModel

interface UserOnClickListener {

    fun onClick(viewHolder : UserAdapter.ViewHolder, userModel: UserModel, position : Int)
}
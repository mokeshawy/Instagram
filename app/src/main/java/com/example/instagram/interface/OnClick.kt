package com.example.instagram.`interface`

import com.example.instagram.adapter.UserAdapter
import com.example.instagram.model.UserModel

interface OnClick {

    fun onClick( viewHolder: UserAdapter.ViewHolder , userModel: UserModel , position : Int)
}
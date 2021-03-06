package com.example.instagram.onclickinterface

import com.example.instagram.ui.adapter.PostAdapter
import com.example.instagram.model.PostModel

interface PostOnClickListener {

    fun onClick(viewHolder: PostAdapter.ViewHolder, postModel: PostModel, position : Int )
}
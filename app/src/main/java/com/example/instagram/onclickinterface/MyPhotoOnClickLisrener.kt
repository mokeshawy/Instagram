package com.example.instagram.onclickinterface

import com.example.instagram.model.PostModel
import com.example.instagram.ui.adapter.MyPhotoAdapter
import com.example.instagram.ui.adapter.PostAdapter

interface MyPhotoOnClickLisrener {

    fun onClick(viewHolder: MyPhotoAdapter.ViewHolder, postModel: PostModel, position : Int )
}
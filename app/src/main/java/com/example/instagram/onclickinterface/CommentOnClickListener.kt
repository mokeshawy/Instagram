package com.example.instagram.onclickinterface

import com.example.instagram.adapter.CommentAdapter
import com.example.instagram.model.CommentModel

interface CommentOnClickListener {

    fun onClick( viewHolder: CommentAdapter.ViewHolder , comment : CommentModel , position : Int )
}
package com.example.instagram.onclickinterface

import com.example.instagram.ui.adapter.CommentAdapter
import com.example.instagram.model.CommentModel

interface CommentOnClickListener {

    fun onClick(viewHolder: CommentAdapter.ViewHolder, comment : CommentModel, position : Int )
}
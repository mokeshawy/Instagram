package com.example.instagram.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.onclickinterface.CommentOnClickListener
import com.example.instagram.databinding.CommentItemLayoutBinding
import com.example.instagram.model.CommentModel
import com.squareup.picasso.Picasso

class CommentAdapter( var comment   : ArrayList<CommentModel> , var itemListOnClickListener: CommentOnClickListener) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {


    class ViewHolder(var binding : CommentItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        // initialize onClickUsersAdapter from interface
        fun initialize(viewHolder: CommentAdapter.ViewHolder, comment: CommentModel, action : CommentOnClickListener){
            action.onClick(viewHolder , comment , adapterPosition )
        }

    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        // Create a new view, which defines the UI of the list item
        return ViewHolder(CommentItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false))
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val comment = comment[position]

        viewHolder.binding.tvUserName.text = comment.userName
        viewHolder.binding.tvComment.text = comment.comment
        Picasso.get().load(comment.image).into(viewHolder.binding.ivUserProfileImageComment)

        viewHolder.initialize(viewHolder,comment,itemListOnClickListener)
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = comment.size

}
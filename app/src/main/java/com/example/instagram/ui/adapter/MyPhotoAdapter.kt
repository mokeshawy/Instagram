package com.example.instagram.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.databinding.ImageItemLayoutBinding
import com.example.instagram.databinding.UserItemLayoutBinding
import com.example.instagram.model.PostModel
import com.example.instagram.onclickinterface.MyPhotoOnClickLisrener
import com.squareup.picasso.Picasso

class MyPhotoAdapter (var post : ArrayList<PostModel>, var myPhotoOnClick: MyPhotoOnClickLisrener ) : RecyclerView.Adapter<MyPhotoAdapter.ViewHolder>() {


    class ViewHolder(var binding : ImageItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun initialize(viewHolder: ViewHolder , postModel: PostModel, action : MyPhotoOnClickLisrener){
            action.onClick(viewHolder,postModel,adapterPosition)
        }
    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        // Create a new view, which defines the UI of the list item
        return ViewHolder(ImageItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false))
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val post = post[position]


        Picasso.get().load(post.postImage).into(viewHolder.binding.ivImagePost)

        // call function for initialize.
        viewHolder.initialize(viewHolder,post,myPhotoOnClick)
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = post.size

}
package com.example.instagram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.`interface`.OnClick
import com.example.instagram.databinding.UserItemLayoutBinding
import com.example.instagram.model.UserModel
import com.squareup.picasso.Picasso

class UserAdapter (var mContext : Context , var mUsers: ArrayList<UserModel> , var isFragment : Boolean = false , var onClick: OnClick) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(var binding : UserItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        // initialize onClickUsersAdapter from interface
        fun initialize(viewHolder: ViewHolder, dataSet: UserModel, action : OnClick){
            action.onClick(viewHolder , dataSet , adapterPosition )
        }

    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        // Create a new view, which defines the UI of the list item
        return ViewHolder(UserItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false))
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val users = mUsers[position]

        viewHolder.binding.userNameSearch.text      = users.userName
        viewHolder.binding.userFullNameSearch.text  = users.fullName
        Picasso.get().load(users.image).into(viewHolder.binding.userProfileImageSearch)

        viewHolder.initialize( viewHolder , mUsers[position] , onClick)

    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mUsers.size
}
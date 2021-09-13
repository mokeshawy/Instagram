package com.example.instagram.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.R
import com.example.instagram.databinding.UserItemLayoutBinding
import com.example.instagram.model.UserModel
import com.example.instagram.onclickinterface.UserOnClickListener
import com.example.instagram.ui.fragment.searchfragment.SearchFragment
import com.example.instagram.ui.fragment.searchfragment.SearchViewModel
import com.example.instagram.utils.Const
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class UserAdapter( var mUsers: ArrayList<UserModel> , var userOnClickListener: UserOnClickListener , var isFragment : Boolean = false ) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {


    class ViewHolder(var binding : UserItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun initialize( viewHolder: ViewHolder , userModel: UserModel , action : UserOnClickListener){
            action.onClick(viewHolder,userModel,adapterPosition)
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

        viewHolder.initialize(viewHolder,users,userOnClickListener)
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mUsers.size

}
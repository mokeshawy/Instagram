package com.example.instagram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.R
import com.example.instagram.databinding.PostItemLayoutBinding
import com.example.instagram.model.CommentModel
import com.example.instagram.model.PostModel
import com.example.instagram.model.UserModel
import com.example.instagram.onclickinterface.CommentOnClickListener
import com.example.instagram.onclickinterface.PostOnClickListener
import com.example.instagram.ui.fragment.homefragment.HomeFragment
import com.example.instagram.ui.fragment.homefragment.HomeViewModel
import com.example.instagram.utils.Const
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class PostAdapter(private var dataSet: List<PostModel> , var postOnClickListener: PostOnClickListener) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(var binding : PostItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        // initialize onClickUsersAdapter from interface
        fun initialize(viewHolder: PostAdapter.ViewHolder, postModel: PostModel , action : PostOnClickListener){
            action.onClick(viewHolder , postModel , adapterPosition )
        }
    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        return ViewHolder(PostItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false))
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val post = dataSet[position]

        // call fun initialize.
        viewHolder.initialize(viewHolder,post,postOnClickListener)

    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
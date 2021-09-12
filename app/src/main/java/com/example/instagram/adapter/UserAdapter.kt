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
import com.example.instagram.ui.fragment.searchfragment.SearchFragment
import com.example.instagram.ui.fragment.searchfragment.SearchViewModel
import com.example.instagram.utils.Const
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class UserAdapter( var mUsers: ArrayList<UserModel> ,
                   var isFragment : Boolean = false ,
                   var searchViewModel: SearchViewModel ,
                   var searchFragment: SearchFragment) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {


    class ViewHolder(var binding : UserItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

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

        // all operation form search viewModel.
        if(FirebaseAuth.getInstance().currentUser!!.uid == users.uid){
                viewHolder.binding.followBtnSearch.visibility = View.GONE
            }else{
                viewHolder.binding.followBtnSearch.visibility = View.VISIBLE
        }
        // call fun for check following status.
        searchViewModel.checkFollowingStatus(users.uid,viewHolder.binding.followBtnSearch)

        // click follow and unFollow.
        viewHolder.binding.followBtnSearch.setOnClickListener {
            // call fun follow and unFollow
            searchViewModel.followAndUnFollow(viewHolder.binding.followBtnSearch,users)
        }

        viewHolder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(Const.BUNDLE_USER_MODEL , users)
            searchFragment.findNavController().navigate(R.id.action_searchFragment_to_profileFragment,bundle)
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mUsers.size

}
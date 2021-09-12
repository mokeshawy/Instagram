package com.example.instagram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.R
import com.example.instagram.databinding.PostItemLayoutBinding
import com.example.instagram.model.PostModel
import com.example.instagram.model.UserModel
import com.example.instagram.ui.fragment.homefragment.HomeFragment
import com.example.instagram.ui.fragment.homefragment.HomeViewModel
import com.example.instagram.utils.Const
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class PostAdapter(private var dataSet: List<PostModel> , var homeViewModel: HomeViewModel , var homeFragment: HomeFragment) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(var binding : PostItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        return ViewHolder(PostItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false))
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val post = dataSet[position]

        // firebase instance.
        val firebaseDatabase = FirebaseDatabase.getInstance()

        // contents of the view with that element
        Picasso.get().load(post.postImage).into(viewHolder.binding.ivPostImageHome)
        if( post.description == ""){
            viewHolder.binding.tvDescription!!.visibility = View.GONE
        }else{
            viewHolder.binding.tvDescription!!.visibility = View.VISIBLE
            viewHolder.binding.tvDescription!!.text = post.description
        }



        // retrieve data from user reference.
        firebaseDatabase.getReference(Const.USER_REFERENCE).child(post.publishre).addValueEventListener( object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val user = snapshot.getValue(UserModel::class.java)!!
                    Picasso.get().load(user.image).into(viewHolder.binding.ivUserProfileImageSearch)
                    viewHolder.binding.tvUserNameSearch.text   = user.userName
                    viewHolder.binding.tvPublisher.text   = user.fullName

                    // comment button.
                    viewHolder.binding.ivPostImageCommentBtn.setOnClickListener {

                        // send photo for post and profile image for user to comment fragment by bundle.
                        val bundle = bundleOf(Const.BUNDLE_IMAGE_POST to post.postImage ,
                                                    Const.BUNDLE_POST_ID  to post.postId ,
                                                    Const.BUNDLE_IMAGE_PROFILE to user.image ,
                                                    Const.BUNDLE_USER_NAME to user.userName ,
                                                    Const.BUNDLE_IMAGE_USER_POST_COMMENT to user.image)

                        homeFragment.findNavController().navigate(R.id.action_homeFragment_to_commentFragment,bundle)
                    }

                    viewHolder.binding.tvComments.setOnClickListener {
                        // send photo for post and profile image for user to comment fragment by bundle.
                        val bundle = bundleOf(Const.BUNDLE_IMAGE_POST to post.postImage , Const.BUNDLE_POST_ID  to post.postId , Const.BUNDLE_IMAGE_PROFILE to user.image )
                        homeFragment.findNavController().navigate(R.id.action_homeFragment_to_commentFragment,bundle)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Const.constToast(viewHolder.itemView.context,error.message)
            }
        })

        // call function for is likes from home viewModel.
        homeViewModel.isLikes(post.postId , viewHolder.binding.ivPostImageLikeBtn)

        // call function for get likes number.
        homeViewModel.numberOfLikes(viewHolder.binding.tvLikes,post.postId)

        // call function for get comment number from home viewModel.
        homeViewModel.numberOfComment(viewHolder.binding.tvComments,post.postId)
        // like button.
        viewHolder.binding.ivPostImageLikeBtn.setOnClickListener {
            if( viewHolder.binding.ivPostImageLikeBtn.tag == "Like"){
                firebaseDatabase.getReference(Const.LIKES_REFERENCE)
                    .child(post.postId).child(Const.getCurrentUser()).setValue(true)
            }else{
                firebaseDatabase.getReference(Const.LIKES_REFERENCE)
                    .child(post.postId).child(Const.getCurrentUser()).removeValue()
            }
        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
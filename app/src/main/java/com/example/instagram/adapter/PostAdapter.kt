package com.example.instagram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.R
import com.example.instagram.databinding.PostItemLayoutBinding
import com.example.instagram.model.PostModel
import com.example.instagram.model.UserModel
import com.example.instagram.utils.Const
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class PostAdapter(private var dataSet: List<PostModel> ) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

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
        viewHolder.binding.tvDescription!!.text = post.description


        // retrieve data from user reference.
        firebaseDatabase.getReference(Const.USER_REFERENCE).child(post.publishre).addValueEventListener( object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val user = snapshot.getValue(UserModel::class.java)!!
                    Picasso.get().load(user.image).into(viewHolder.binding.ivUserProfileImageSearch)
                    viewHolder.binding.tvUserNameSearch.text   = user.userName
                    viewHolder.binding.tvPublisher.text   = user.fullName
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Const.constToast(viewHolder.itemView.context,error.message)
            }
        })

        // call function for is likes.
        isLikes(post.postId , viewHolder.binding.ivPostImageLikeBtn)
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

    private fun isLikes(postId: String, ivPostImageLikeBtn: ImageView) {
        // firebase instance.
        val firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.getReference(Const.LIKES_REFERENCE).child(postId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child(Const.getCurrentUser()).exists()){
                    ivPostImageLikeBtn.setImageResource(R.drawable.heart_clicked)
                    ivPostImageLikeBtn.tag = "Liked"
                }else{
                    ivPostImageLikeBtn.setImageResource(R.drawable.heart_not_clicked)
                    ivPostImageLikeBtn.tag = "Like"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
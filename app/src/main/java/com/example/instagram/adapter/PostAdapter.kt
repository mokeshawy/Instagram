package com.example.instagram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.databinding.PostItemLayoutBinding
import com.example.instagram.model.PostModel
import com.example.instagram.model.UserModel
import com.example.instagram.utils.Const
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class PostAdapter (private val dataSet: List<PostModel> ) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private lateinit var binding : PostItemLayoutBinding

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        binding = PostItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.context))
        return ViewHolder(binding.root)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val post = dataSet[position]

        // contents of the view with that element
        Picasso.get().load(post.postImage).into(binding.ivPostImageHome)
        binding.tvDescription.text  = post.description

        // firebase instance.
        val firebaseDatabase    = FirebaseDatabase.getInstance()
        val userReference       = firebaseDatabase.getReference(Const.USER_REFERENCE)

        // retrieve data from user reference.
        userReference.child(post.publishre).addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val user = snapshot.getValue(UserModel::class.java)!!
                    Picasso.get().load(user.image).into(binding.ivUserProfileImageSearch)
                    binding.tvUserNameSearch.text   = user.userName
                    binding.tvPublisher.text        = user.fullName
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Const.constToast(viewHolder.itemView.context,error.message)
            }
        })

    }

    // Return the size of your dataSet (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
package com.example.instagram.ui.fragment.profilefragment

import android.app.Application
import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagram.R
import com.example.instagram.model.PostModel
import com.example.instagram.model.UserModel
import com.example.instagram.utils.Const
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    var tvAccountSetting    = MutableLiveData<String>("")
    var tvShowUserName      = MutableLiveData<String>("")
    var tvShowFullName      = MutableLiveData<String>("")
    var tvShowBio           = MutableLiveData<String>("")
    var tvTotalFollowing    = MutableLiveData<String>("0")
    var tvTotalFollowers    = MutableLiveData<String>("0")

    var postListLiveData = MutableLiveData<ArrayList<PostModel>>()
    var postList = ArrayList<PostModel>()

    var firebaseDatabase        = FirebaseDatabase.getInstance()
    var followingReference      = firebaseDatabase.getReference(Const.FOLLOW_REFERENCE)
    var postReference           = firebaseDatabase.getReference(Const.ADD_POST_REFERENCE)

    // get context
    val context = application.applicationContext as Application

    fun checkFollowAndFollowingButtonsStatus(userModel: UserModel ){
        if( followingReference != null){
            followingReference.child(Const.getCurrentUser()).child(Const.CHILD_FOLLOWING).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.child(userModel.uid).exists()){
                        tvAccountSetting.value = context.resources.getString(R.string.text_remove)
                    }else{
                        tvAccountSetting.value = context.resources.getString(R.string.text_follow)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Const.constToast(context,error.message)
                }
            })
        }
    }

    // fun follow from search page after entry profile for user.
    fun follow( userModel: UserModel){
        followingReference.child(Const.getCurrentUser()).child(Const.CHILD_FOLLOWING).child(userModel.uid).setValue(true).addOnCompleteListener { task ->
            if(task.isSuccessful){
                followingReference.child(userModel.uid).child(Const.CHILD_FOLLOWERS).child(Const.getCurrentUser()).setValue(true)
            }
        }
    }

    // fun remove from search page after entry profile for user.
    fun unFollow(userModel: UserModel){
        followingReference.child(Const.getCurrentUser()).child(Const.CHILD_FOLLOWING).child(userModel.uid).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                followingReference.child(userModel.uid).child(Const.CHILD_FOLLOWERS).child(Const.getCurrentUser()).removeValue()
            }
        }
    }

    // fun get followers.
    fun getFollowers(uid : String){
        followingReference.child(uid)
            .child(Const.CHILD_FOLLOWERS)
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    tvTotalFollowers.value = snapshot.childrenCount.toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Const.constToast(context,error.message)
            }
        })
    }

    // fun get followings.
    fun getFollowings(uid : String){
        followingReference.child(uid)
            .child(Const.CHILD_FOLLOWING)
            .addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    tvTotalFollowing.value = snapshot.childrenCount.toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {
               Const.constToast(context,error.message)
            }
        })
    }

    // fun get image from post in profile page.
    fun myPhoto(){
        postList = ArrayList()
        postReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    postList.clear()
                    for (ds in snapshot.children){
                        val post = ds.getValue(PostModel::class.java)!!
                        if( post.publishre == Const.getCurrentUser()){
                            postList.add(post)
                        }
                    }
                    postListLiveData.value = postList
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
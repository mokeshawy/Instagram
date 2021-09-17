package com.example.instagram.ui.fragment.profilefragment

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagram.R
import com.example.instagram.baseapp.BaseApp
import com.example.instagram.model.PostModel
import com.example.instagram.model.UserModel
import com.example.instagram.ui.adapter.MyPhotoAdapter
import com.example.instagram.utils.Const
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ProfileViewModel : ViewModel() {

    var tvAccountSetting    = MutableLiveData<String>("")
    var tvShowUserName      = MutableLiveData<String>("")
    var tvShowFullName      = MutableLiveData<String>("")
    var tvShowBio           = MutableLiveData<String>("")
    var tvTotalFollowing    = MutableLiveData<String>("0")
    var tvTotalFollowers    = MutableLiveData<String>("0")
    var tvTotalOfPost       = MutableLiveData<String>("0")

    // get all post for user in profile
    var postListLiveData = MutableLiveData<ArrayList<PostModel>>()
    var postList = ArrayList<PostModel>()

    // get post saved in profile page
    var postSavedLiveData   = MutableLiveData<ArrayList<PostModel>>()
    var postSaved           : List<String>?     = null
    var postSavedList       : List<PostModel>?  = null
    var myPhotoAdapter      : MyPhotoAdapter?   = null


    var firebaseDatabase        = FirebaseDatabase.getInstance()
    var followingReference      = firebaseDatabase.getReference(Const.FOLLOW_REFERENCE)
    var postReference           = firebaseDatabase.getReference(Const.ADD_POST_REFERENCE)
    var savedReference          = firebaseDatabase.getReference(Const.SAVE_REFERENCE)
    var notificationRef         = firebaseDatabase.getReference(Const.NOTIFICATION_REFERENCE)


    fun checkFollowAndFollowingButtonsStatus(userModel: UserModel ){
        if( followingReference != null){
            followingReference.child(Const.getCurrentUser()).child(Const.CHILD_FOLLOWING).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.child(userModel.uid).exists()){
                        tvAccountSetting.value = BaseApp.appContext.resources.getString(R.string.text_remove)
                    }else{
                        tvAccountSetting.value = BaseApp.appContext.resources.getString(R.string.text_follow)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Const.constToast(BaseApp.appContext,error.message)
                }
            })
        }
    }

    // fun follow from search page after entry profile for user.
    fun follow( userModel: UserModel){
        followingReference.child(Const.getCurrentUser()).child(Const.CHILD_FOLLOWING).child(userModel.uid).setValue(true).addOnCompleteListener { task ->
            if(task.isSuccessful){
                followingReference.child(userModel.uid).child(Const.CHILD_FOLLOWERS).child(Const.getCurrentUser()).setValue(true)
                // call function add notification when any user started follow from profile page..
                addNotification(userModel.uid)
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
                Const.constToast(BaseApp.appContext,error.message)
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
                    tvTotalFollowing.value = "${snapshot.childrenCount-1}"
                }
            }
            override fun onCancelled(error: DatabaseError) {
               Const.constToast(BaseApp.appContext,error.message)
            }
        })
    }

    // fun get image from post in profile page.
    fun myPhoto( uid : String){
        postList = ArrayList()
        postReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    postList.clear()
                    for (ds in snapshot.children){
                        val post = ds.getValue(PostModel::class.java)!!
                        if( post.publishre == uid){
                            postList.add(post)
                        }
                    }
                    postListLiveData.value = postList
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Const.constToast(BaseApp.appContext,error.message)
            }
        })
    }

    // function for get all number of post.
    fun getTotalNumberOfPost(){
        postReference.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var postCount = 0
               for ( ds in snapshot.children){
                   val post = ds.getValue(PostModel::class.java)!!
                   if(post.publishre == Const.getCurrentUser()){
                       postCount++
                   }
               }
                tvTotalOfPost.value = postCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Const.constToast(BaseApp.appContext,error.message)
            }
        })
    }

    // get post saved..
    fun postListSaved( uid : String){
        postSaved = ArrayList()
        savedReference.child(uid).addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(ds in snapshot.children){
                        (postSaved as ArrayList<String>).add(ds.key!!)
                    }
                    readSaveData()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Const.constToast(BaseApp.appContext,error.message)
            }
        })
    }

    // read save data..
    private fun readSaveData() {
        postSavedList = ArrayList()
        postReference.addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if( snapshot.exists()){
                    ( postSavedList as ArrayList<PostModel>).clear()
                    for ( ds in snapshot.children){
                        val post = ds.getValue(PostModel::class.java)!!
                        for(key in postSaved!!){
                            if( post.postId == key){
                                ( postSavedList as ArrayList<PostModel>).add(post)
                            }
                        }
                        postSavedLiveData.value = ( postSavedList as ArrayList<PostModel>)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Const.constToast(BaseApp.appContext,error.message)
            }
        })
    }

    // add notification when started following..
    private fun addNotification( userId : String){

        val notificationMap = HashMap<String , Any>()

        notificationMap[Const.CHILD_USERID_NOTIFICATION]    = Const.getCurrentUser()
        notificationMap[Const.CHILD_TEXT_NOTIFICATION]      = "started following you"
        notificationMap[Const.CHILD_POST_ID_NOTIFICATION]   = ""
        notificationMap[Const.CHILD_IS_POST_NOTIFICATION]   = true

        notificationRef.child(userId).push().setValue(notificationMap)

    }
}
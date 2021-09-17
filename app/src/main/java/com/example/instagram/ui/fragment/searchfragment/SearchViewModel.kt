package com.example.instagram.ui.fragment.searchfragment

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagram.R
import com.example.instagram.ui.adapter.UserAdapter
import com.example.instagram.baseapp.BaseApp
import com.example.instagram.model.UserModel
import com.example.instagram.utils.Const
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel

class SearchViewModel : ViewModel() {

    // for user search page..
    var mUser = MutableLiveData<ArrayList<UserModel>>()
    var user  = ArrayList<UserModel>()
    var etSearchText = MutableLiveData<String>("")
    val userAdapter : UserAdapter? = null

    // for get likes and following and followers page ..
    var userLIstLiveData = MutableLiveData<ArrayList<UserModel>>()
    var userList    : List<UserModel>?  = null
    var idList      : List<String>?     = null

    // firebase instance.
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var userReference       = firebaseDatabase.getReference(Const.USER_REFERENCE)
    var followingReference  = firebaseDatabase.getReference(Const.FOLLOW_REFERENCE)
    var likeReference       = firebaseDatabase.getReference(Const.LIKES_REFERENCE)
    var notificationRef     = firebaseDatabase.getReference(Const.NOTIFICATION_REFERENCE)


                                /* operation of search page */
    // fun for search user.
    fun searchUser(input : String ){
        userReference.orderByChild("fullName").startAt(input).endAt(input+"\uf8ff")
            .addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
               user.clear()
                for (ds in snapshot.children){
                    val mUserModel = ds.getValue(UserModel::class.java)!!
                    user.add(mUserModel)
                }
                mUser.value = user
                userAdapter?.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(BaseApp.appContext, error.message , Toast.LENGTH_SHORT).show()
            }

        })
    }

    // fun for show all data
    fun retrieveUser(){
        userReference.addValueEventListener( object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if(etSearchText.value!!.toString() == ""){
                    user.clear()
                    for (ds in snapshot.children){
                        val mUserModel = ds.getValue(UserModel::class.java)!!
                        user.add(mUserModel)
                    }
                    mUser.value = user
                    userAdapter?.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(BaseApp.appContext, error.message , Toast.LENGTH_SHORT).show()
            }
        })
    }

    // check following status.
    fun checkFollowingStatus(uid : String , followingButton : Button){
        followingReference.child(Const.getCurrentUser()).child(Const.CHILD_FOLLOWING).addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if( snapshot.child(uid).exists()){
                    followingButton.text = BaseApp.appContext.resources.getString(R.string.text_remove)
                }else{
                    followingButton.text = BaseApp.appContext.resources.getString(R.string.text_follow)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Const.constToast(BaseApp.appContext,error.message)
            }

        })
    }

    // fun follow and unFollow...
    fun followAndUnFollow(followButton : Button , userModel : UserModel){

        if( followButton.text.toString() == Const.BTN_FOLLOW){
            followingReference.child(Const.getCurrentUser()).child(Const.CHILD_FOLLOWING).child(userModel.uid).setValue(true).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        followingReference.child(userModel.uid).child(Const.CHILD_FOLLOWERS).child(Const.getCurrentUser()).setValue(true)
                    }
                }

            // call function for add notification when any user started follow any another user.
            addNotification(userModel.uid)
        }else{
            followingReference.child(Const.getCurrentUser()).child(Const.CHILD_FOLLOWING).child(userModel.uid).removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        followingReference.child(userModel.uid).child(Const.CHILD_FOLLOWERS).child(Const.getCurrentUser()).removeValue()
                    }
                }
        }
    }


                        /* operation of get likes and followers and following page..........*/

    // get number of user click likes on post in time line...
    fun getLikes( id : String){
        idList = ArrayList()
        likeReference.child(id).addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    ( idList as ArrayList<String> ).clear()
                    for ( ds in snapshot.children){
                        ( idList as ArrayList<String> ).add(ds.key!!)
                    }
                    // call function of show user..
                    showUsers()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Const.constToast(BaseApp.appContext,error.message)
            }
        })

    }

    // get number of user following...
    fun getFollowing(id : String){
        idList = ArrayList()
        followingReference.child(id)
            .child(Const.CHILD_FOLLOWING)
            .addValueEventListener( object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    ( idList as ArrayList<String> ).clear()
                    for ( ds in snapshot.children){
                        if(ds.key != Const.getCurrentUser()){
                            ( idList as ArrayList<String> ).add(ds.key!!)
                        }
                    }
                    // call function of show user..
                    showUsers()
                }
                override fun onCancelled(error: DatabaseError) {
                    Const.constToast(BaseApp.appContext,error.message)
                }
            })
    }

    // get number of user followers...
    fun getFollowers(id : String){
        idList = ArrayList()
        followingReference.child(id)
            .child(Const.CHILD_FOLLOWERS)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    ( idList as ArrayList<String> ).clear()
                    for ( ds in snapshot.children){
                        ( idList as ArrayList<String> ).add(ds.key!!)
                    }
                    // call function of show user..
                    showUsers()
                }
                override fun onCancelled(error: DatabaseError) {
                    Const.constToast(BaseApp.appContext,error.message)
                }
            })
    }

    // get user from database
    private fun showUsers() {
        userList = ArrayList()
        userReference.addValueEventListener( object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                (userList as ArrayList<UserModel>).clear()
                for (ds in snapshot.children){
                    val mUserModel = ds.getValue(UserModel::class.java)!!
                    for ( id in idList!!){
                        if( mUserModel.uid == id){
                            (userList as ArrayList<UserModel>).add(mUserModel)
                        }
                    }
                }
                userLIstLiveData.value = userList as ArrayList<UserModel>
                userAdapter?.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(BaseApp.appContext, error.message , Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun addNotification(userId : String ){

        val notificationMap = HashMap<String , Any>()

        notificationMap[Const.CHILD_USERID_NOTIFICATION]    = Const.getCurrentUser()
        notificationMap[Const.CHILD_TEXT_NOTIFICATION]      = "started following you"
        notificationMap[Const.CHILD_POST_ID_NOTIFICATION]   = ""
        notificationMap[Const.CHILD_IS_POST_NOTIFICATION]   = false

        notificationRef.child(userId).push().setValue(notificationMap)

    }
}
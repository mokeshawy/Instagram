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
import com.example.instagram.adapter.UserAdapter
import com.example.instagram.model.UserModel
import com.example.instagram.utils.Const
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    var mUser = MutableLiveData<ArrayList<UserModel>>()
    var user  = ArrayList<UserModel>()
    var etSearchText = MutableLiveData<String>("")
    val userAdapter : UserAdapter? = null

    // firebase instance.
    private var firebaseDatabase    = FirebaseDatabase.getInstance()
    private var userReference       = firebaseDatabase.getReference(Const.USER_REFERENCE)
    private var followingReference  = firebaseDatabase.getReference(Const.FOLLOW_REFERENCE)

    // get context
    val context = application.applicationContext as Application

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
                Toast.makeText(context, error.message , Toast.LENGTH_SHORT).show()
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
                Toast.makeText(context, error.message , Toast.LENGTH_SHORT).show()
            }
        })
    }

    // check following status.
    fun checkFollowingStatus(uid : String , followingButton : Button){
        followingReference.child(Const.getCurrentUser()).child(Const.CHILD_FOLLOWING).addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if( snapshot.child(uid).exists()){
                    followingButton.text = context.resources.getString(R.string.text_remove)
                }else{
                    followingButton.text = context.resources.getString(R.string.text_follow)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun followAndUnFollow(followButton : Button , userModel : UserModel){

        if( followButton.text.toString() == Const.BTN_FOLLOW){
            followingReference.child(Const.getCurrentUser()).child(Const.CHILD_FOLLOWING).child(userModel.uid).setValue(true).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        followingReference.child(userModel.uid).child(Const.CHILD_FOLLOWERS).child(Const.getCurrentUser()).setValue(true)
                    }
                }
        }else{
            followingReference.child(Const.getCurrentUser()).child(Const.CHILD_FOLLOWING).child(userModel.uid).removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        followingReference.child(userModel.uid).child(Const.CHILD_FOLLOWERS).child(Const.getCurrentUser()).removeValue()
                    }
                }
        }
    }
}
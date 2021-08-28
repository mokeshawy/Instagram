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
import com.example.instagram.model.UserModel
import com.example.instagram.utils.Const
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    var tvAccountSetting    = MutableLiveData<String>("")
    var ivUserImage         = MutableLiveData<ImageView>()

    var firebaseDatabase        = FirebaseDatabase.getInstance()
    var followingReference      = firebaseDatabase.getReference(Const.FOLLOW_REFERENCE)

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
                    Toast.makeText(context , error.message ,Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // fun follow from search page after entry profile for user.
    fun follow( userModel: UserModel){
        followingReference.child(Const.getCurrentUser())
            .child(Const.CHILD_FOLLOWING).child(userModel.uid).setValue(true).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    followingReference.child(Const.CHILD_FOLLOWERS).child(userModel.uid).child(Const.getCurrentUser()).setValue(true)
                }
            }
    }

    // fun remove from search page after entry profile for user.
    fun unFollow(userModel: UserModel){
        followingReference.child(Const.getCurrentUser())
            .child(Const.CHILD_FOLLOWING).child(userModel.uid).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    followingReference.child(Const.CHILD_FOLLOWERS).child(userModel.uid)
                        .child(Const.getCurrentUser()).removeValue()
                }
            }
    }

    // fun user info.
    fun userInfo( userModel: UserModel ){
        Picasso.get().load(userModel.image).into(ivUserImage.value)
    }
}
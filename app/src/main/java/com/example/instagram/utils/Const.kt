package com.example.instagram.utils

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

object Const {

    // user reference.
    const val USER_REFERENCE = "user"

    // follow reference.
    const val FOLLOW_REFERENCE = "Follow"

    // child for follow reference.
    const val CHILD_FOLLOWING = "Following"
    const val CHILD_FOLLOWERS = "Followers"

    // follow btn
    const val BTN_FOLLOW = "Follow"
    const val BTN_FOLLOWING = "Following"

    // bundle key
    const val BUNDLE_USER_MODEL = "userModelKey"
    // default image profile
    const val DEFAULT_IMAGE_PROFILE = "https://firebasestorage.googleapis.com/v0/b/instagram-1bd4b.appspot.com/o/Photo%2Fic_profile_image.png?alt=media&token=a33b0a81-c0a7-40e6-a1c8-9c5b5e2c555b"

    // shared preference key.
    const val SHARED_PREF_KEY = "preference"
    const val PUT_SHARE_PREF = "profileId"


    // constance toast.
    fun constToast( context: Context , str : String){
        Toast.makeText(context , str , Toast.LENGTH_SHORT).show()
    }

    // fun get id for user login
    fun getCurrentUser() : String{
        val firebaseAuth    = FirebaseAuth.getInstance().currentUser
        var currentUser     = ""
        if( currentUser !=null){
            currentUser = firebaseAuth!!.uid
        }
        return currentUser
    }

}
package com.example.instagram.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth

object Const {

    // user reference.
    const val USER_REFERENCE = "user"

    // child of user reference.
    const val CHILD_USER_NAME = "userName"
    const val CHILD_FULL_NAME = "fullName"
    const val CHILD_EMAIL     = "email"
    const val CHILD_BIO       = "bio"
    const val CHILD_IMAGE     = "image"

    // follow reference.
    const val FOLLOW_REFERENCE = "Follow"

    // child for follow reference.
    const val CHILD_FOLLOWING = "Following"
    const val CHILD_FOLLOWERS = "Followers"

    // add post reference.
    const val ADD_POST_REFERENCE = "addPost"

    // child add post.
    const val CHILD_PUBLISHRE_ADD_POST      = "publishre"
    const val CHILD_DESCRIPTION_ADD_POST    = "description"
    const val CHILD_POST_ID_ADD_POST        = "postId"
    const val CHILD_POST_IMAGE              = "postImage"

    // follow btn
    const val BTN_FOLLOW = "Follow"
    const val BTN_FOLLOWING = "Following"

    // bundle key
    const val BUNDLE_USER_MODEL = "userModelKey"
    // default image profile
    const val DEFAULT_IMAGE_PROFILE     = "https://firebasestorage.googleapis.com/v0/b/instagram-1bd4b.appspot.com/o/Photo%2Fic_profile_image.png?alt=media&token=a33b0a81-c0a7-40e6-a1c8-9c5b5e2c555b"
    const val DEFAULT_IMAGE_ADD_POST    = "https://firebasestorage.googleapis.com/v0/b/instagram-1bd4b.appspot.com/o/Photo%2Fdefault_image_add_post.png?alt=media&token=7f253d31-c304-4a07-a94d-0c31537e9f5e"

    // shared preference key.
    const val DATA_STORE_NAME   = "dataStore"
    const val USER_NAME_KEY     = "userNameKey"
    const val FULL_NAME_KEY     = "fullNameKey"
    const val BIO_KEY           = "bioKey"
    const val IMAGE_KEY         = "imageKey"

    // shared preference for cash emil and password.
    const val DATA_STORE_CASH_NAME  = "cashPref"
    const val EMAIL_KEY             = "email"
    const val PASSWORD_KEY          = "password"

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

    //This function is used check if the device is connected to the Internet or not.
    @RequiresApi(Build.VERSION_CODES.M)
    fun isNetworkAvailable(context: Context) : Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when{
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            //for other device how are able to connect with Ethernet
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

            else -> false
        }
    }
}
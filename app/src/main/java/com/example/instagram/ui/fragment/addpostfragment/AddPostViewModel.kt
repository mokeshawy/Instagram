package com.example.instagram.ui.fragment.addpostfragment

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagram.R
import com.example.instagram.baseapp.BaseApp
import com.example.instagram.utils.Const
import com.example.instagram.utils.CustomProgressDialog
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddPostViewModel : ViewModel() {

    val etDescription = MutableLiveData<String>("")

    var stateAfterAddPost = MutableLiveData<Boolean>()

    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var addPostReference    = firebaseDatabase.getReference(Const.ADD_POST_REFERENCE)
    var firebaseStorage     = FirebaseStorage.getInstance().reference

    // fun add new post.
    fun addPost(imageUri : Uri){
        if(etDescription.value!!.trim().isEmpty()){
            Const.constToast(BaseApp.appContext,BaseApp.appContext.resources.getString(R.string.et_description_post))
            CustomProgressDialog.hideProgressDialog()
        }else{
            val postId = addPostReference.push().key.toString()
            val postStorage : StorageReference = firebaseStorage.child("PostImage/"+System.currentTimeMillis())
            postStorage.putFile(imageUri).addOnCompleteListener { imageUrl ->
                if(imageUrl.isSuccessful){
                    postStorage.downloadUrl.addOnSuccessListener { imageDownload ->
                        val map = HashMap<String , Any>()

                        map[Const.CHILD_PUBLISHRE_ADD_POST]     = Const.getCurrentUser()
                        map[Const.CHILD_POST_IMAGE]             = imageDownload.toString()
                        map[Const.CHILD_POST_ID_ADD_POST]       = postId
                        map[Const.CHILD_DESCRIPTION_ADD_POST]   = etDescription.value!!

                        addPostReference.child(postId).updateChildren(map)

                        CustomProgressDialog.hideProgressDialog()
                        Const.constToast(BaseApp.appContext,"Post uploaded successfully")

                        stateAfterAddPost.value = true
                    }
                }else{
                    Const.constToast(BaseApp.appContext,imageUrl.exception!!.message.toString())
                    CustomProgressDialog.hideProgressDialog()
                }
            }
        }
    }
}
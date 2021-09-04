package com.example.instagram.ui.fragment.accountsettingfragemnt


import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.instagram.R
import com.example.instagram.utils.Const
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AccountSettingViewModel(application: Application) : AndroidViewModel(application){

    var etUserName = MutableLiveData<String>("")
    var etFullName = MutableLiveData<String>("")
    var etBio      = MutableLiveData<String>("")

    // get context.
    val context = application.applicationContext as Application

    // get firebase instance.
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var firebaseStorage = FirebaseStorage.getInstance().reference
    var userReference       = firebaseDatabase.getReference(Const.USER_REFERENCE)


    // update profile for user.
    fun updateProfile( imageUrl : Uri){
        // validate input.
        if(etFullName.value!!.trim().isEmpty()){
            Const.constToast(context,context.resources.getString(R.string.err_enter_full_name))
        }else if(etUserName.value!!.trim().isEmpty()){
            Const.constToast(context,context.resources.getString(R.string.err_enter_user_name))
        }else if(etBio.value!!.trim().isEmpty()){
            Const.constToast(context,context.resources.getString(R.string.err_message_for_bio))
        }else{
            var profileStorage : StorageReference = firebaseStorage.child("Photo/"+System.currentTimeMillis())
            profileStorage.putFile(imageUrl).addOnCompleteListener { imageUri ->
                if(imageUri.isSuccessful){
                    profileStorage.downloadUrl.addOnSuccessListener { uri ->
                        val map = HashMap<String,Any>()

                        map[Const.CHILD_FULL_NAME]  = etFullName.value!!
                        map[Const.CHILD_USER_NAME]  = etUserName.value!!
                        map[Const.CHILD_BIO]        = etBio.value!!
                        map[Const.CHILD_IMAGE]      = uri.toString()

                        userReference.child(Const.getCurrentUser()).updateChildren(map)
                    }
                }
            }
        }
    }
}
package com.example.instagram.ui.fragment.accountsettingfragemnt


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

class AccountSettingViewModel : ViewModel(){

    var etUserName = MutableLiveData<String>("")
    var etFullName = MutableLiveData<String>("")
    var etBio      = MutableLiveData<String>("")

    var stateAfterEditProfile = MutableLiveData<Boolean>()

    // get firebase instance.
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var firebaseStorage     = FirebaseStorage.getInstance().reference
    var userReference       = firebaseDatabase.getReference(Const.USER_REFERENCE)


    // update profile for user.
    fun updateProfile( imageUrl : Uri){
        // validate input.
        if(etFullName.value!!.trim().isEmpty()){
            Const.constToast(BaseApp.appContext, BaseApp.appContext.resources.getString(R.string.err_enter_full_name))
        }else if(etUserName.value!!.trim().isEmpty()){
            Const.constToast(BaseApp.appContext,BaseApp.appContext.resources.getString(R.string.err_enter_user_name))
        }else if(etBio.value!!.trim().isEmpty()){
            Const.constToast(BaseApp.appContext,BaseApp.appContext.resources.getString(R.string.err_message_for_bio))
        }else{
            val profileStorage : StorageReference = firebaseStorage.child("Photo/"+System.currentTimeMillis())
            profileStorage.putFile(imageUrl).addOnCompleteListener { imageUri ->
                if(imageUri.isSuccessful){
                    profileStorage.downloadUrl.addOnSuccessListener { uri ->
                        val map = HashMap<String,Any>()

                        map[Const.CHILD_FULL_NAME]  = etFullName.value!!
                        map[Const.CHILD_USER_NAME]  = etUserName.value!!
                        map[Const.CHILD_BIO]        = etBio.value!!
                        map[Const.CHILD_IMAGE]      = uri.toString()

                        userReference.child(Const.getCurrentUser()).updateChildren(map)
                        CustomProgressDialog.hideProgressDialog()

                        stateAfterEditProfile.value = true
                    }
                }else{
                    Const.constToast(BaseApp.appContext,imageUri.exception!!.message.toString())
                }
            }
        }
    }
}
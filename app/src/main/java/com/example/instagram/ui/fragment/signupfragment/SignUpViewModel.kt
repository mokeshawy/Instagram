package com.example.instagram.ui.fragment.signupfragment

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagram.R
import com.example.instagram.model.UserModel
import com.example.instagram.utils.Const
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    var etFullName    = MutableLiveData<String>("")
    var etUserName    = MutableLiveData<String>("")
    var etEmail       = MutableLiveData<String>("")
    var etPassword    = MutableLiveData<String>("")
    var etConfirmPassword   = MutableLiveData<String>("")

    val state = MutableLiveData<Boolean>()

    // get context.
    val context = application.applicationContext as Application

    // firebase instance.
    var firebaseAuth        = FirebaseAuth.getInstance()
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var userReference       = firebaseDatabase.getReference(Const.USER_REFERENCE)

    // create new account by fun signUp.
    fun signUp(){
        // validate input.
        if(etFullName.value!!.trim().isEmpty()){
            Const.constToast(context, context.resources.getString(R.string.err_enter_full_name))
        }else if(etUserName.value!!.trim().isEmpty()){
            Const.constToast(context,context.resources.getString(R.string.err_enter_user_name))
        }else if( etEmail.value!!.trim().isEmpty()){
            Const.constToast(context, context.resources.getString(R.string.err_enter_your_email))
        }else if(etPassword.value!!.trim().isEmpty()){
            Const.constToast(context, context.resources.getString(R.string.err_enter_your_password))
        }else if(etPassword.value!!.length < 6){
            Const.constToast(context, context.resources.getString(R.string.err_msg_the_password_is_not_less_than))
        }else if (etConfirmPassword.value!!.trim().isEmpty() ) {
            Const.constToast(context, context.resources.getString(R.string.err_enter_your_password))
        }else if(etPassword.value!! != etConfirmPassword.value!!){
            Const.constToast(context, context.resources.getString(R.string.err_enter_mismatch_password))
        }else{
            // operation for firebase Authentication.
            firebaseAuth.createUserWithEmailAndPassword(etEmail.value!! , etPassword.value!!).addOnCompleteListener {
                if(it.isSuccessful){

                    firebaseAuth.currentUser?.sendEmailVerification()
                    val uid = firebaseAuth.currentUser?.uid.toString()
                    val userModel = UserModel(uid,etUserName.value!!.toLowerCase(),etFullName.value!!.toLowerCase(),etEmail.value!!,context.getString(R.string.message_for_bio),Const.DEFAULT_IMAGE_PROFILE)

                    userReference.child(firebaseAuth.currentUser?.uid.toString()).setValue(userModel)
                    state.value = true

                }else{
                    state.value = false
                    Const.constToast(context, it.exception!!.message.toString())
                }
            }
        }
    }
}
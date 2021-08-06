package com.example.instagram.ui.signupfragment

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagram.R
import com.example.instagram.model.UserModel
import com.example.instagram.utils.Const
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpViewModel : ViewModel() {

    var etFullName    = MutableLiveData<String>("")
    var etUserName    = MutableLiveData<String>("")
    var etEmail       = MutableLiveData<String>("")
    var etPassword    = MutableLiveData<String>("")
    var etConfirmPassword   = MutableLiveData<String>("")

    // firebase instance.
    var firebaseAuth        = FirebaseAuth.getInstance()
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var userReference       = firebaseDatabase.getReference(Const.USER_REFERENCE)

    // create new account by fun signUp.
    fun signUp( context: Context , view : View){
        // validate input.
        if(etFullName.value!!.trim().isEmpty()){
            Snackbar.make( view , context.resources.getString(R.string.err_enter_full_name),Snackbar.LENGTH_SHORT).show()
        }else if(etUserName.value!!.trim().isEmpty()){
            Snackbar.make(view,context.resources.getString(R.string.err_enter_user_name),Snackbar.LENGTH_SHORT).show()
        }else if( etEmail.value!!.trim().isEmpty()){
            Snackbar.make(view,context.resources.getString(R.string.err_enter_your_email),Snackbar.LENGTH_SHORT).show()
        }else if(etPassword.value!!.trim().isEmpty()){
            Snackbar.make(view,context.resources.getString(R.string.err_enter_your_password),Snackbar.LENGTH_SHORT).show()
        }else if(etPassword.value!!.length < 6){
            Snackbar.make(view,context.resources.getString(R.string.err_msg_the_password_is_not_less_than),Snackbar.LENGTH_SHORT).show()
        }else if (etConfirmPassword.value!!.trim().isEmpty() ) {
            Snackbar.make(view,context.resources.getString(R.string.err_enter_your_password),Snackbar.LENGTH_SHORT).show()
        }else if(etPassword.value!! != etConfirmPassword.value!!){
            Snackbar.make(view,context.resources.getString(R.string.err_enter_mismatch_password),Snackbar.LENGTH_SHORT).show()
        }else{
            // operation for firebase Authentication.
            firebaseAuth.createUserWithEmailAndPassword(etEmail.value!! , etPassword.value!!).addOnCompleteListener {
                if(it.isSuccessful){

                    firebaseAuth.currentUser?.sendEmailVerification()
                    val uid = firebaseAuth.currentUser?.uid.toString()
                    val userModel = UserModel(uid,etUserName.value!!,etFullName.value!!,context.getString(R.string.message_for_bio),Const.DEFAULT_IMAGE_PROFILE)

                    userReference.child(firebaseAuth.currentUser?.uid.toString()).setValue(userModel)
                }else{
                    Snackbar.make(view,it.exception!!.message.toString(),Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}
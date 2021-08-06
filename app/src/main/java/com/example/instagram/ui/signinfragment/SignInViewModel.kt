package com.example.instagram.ui.signinfragment

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.instagram.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignInViewModel : ViewModel() {

    var etEmail = MutableLiveData<String>("")
    var etPassword = MutableLiveData<String>("")


    var firebaseAuth = FirebaseAuth.getInstance()

    fun login( context : Context , view : View){

        if(etEmail.value!!.trim().isEmpty()){
            Snackbar.make(view , context.resources.getString(R.string.err_enter_your_email),Snackbar.LENGTH_SHORT).show()
        }else if(etPassword.value!!.trim().isEmpty()){
            Snackbar.make(view , context.resources.getString(R.string.err_enter_your_password),Snackbar.LENGTH_SHORT).show()
        }else if(etPassword.value!!.length < 6){
            Snackbar.make(view , context.resources.getString(R.string.err_msg_the_password_not_less_than),Snackbar.LENGTH_SHORT).show()
        }else{
            firebaseAuth.signInWithEmailAndPassword(etEmail.value!! , etPassword.value!!).addOnCompleteListener {
                if(it.isSuccessful){
                    if(firebaseAuth.currentUser!!.isEmailVerified){
                        Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_homeFragment)
                    }else{
                        Snackbar.make(view , context.resources.getString(R.string.err_msg_confirm_email),Snackbar.LENGTH_SHORT).show()
                    }
                }else{
                    Snackbar.make(view, it.exception!!.message.toString(),Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}
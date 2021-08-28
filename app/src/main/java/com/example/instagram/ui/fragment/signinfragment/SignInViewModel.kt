package com.example.instagram.ui.fragment.signinfragment

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.instagram.R
import com.example.instagram.utils.Const
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    var etEmail     = MutableLiveData<String>("")
    var etPassword  = MutableLiveData<String>("")

    val state =  MutableLiveData<Boolean>()

    var firebaseAuth = FirebaseAuth.getInstance()

    // get context
    val context = application.applicationContext as Application

    fun login(){
        if(etEmail.value!!.trim().isEmpty()){
            Const.constToast(context,context.resources.getString(R.string.err_enter_your_email))
        }else if(etPassword.value!!.trim().isEmpty()){
            Const.constToast(context,context.resources.getString(R.string.err_enter_your_password))
        }else if(etPassword.value!!.length < 6){
            Const.constToast(context,context.resources.getString(R.string.err_msg_the_password_not_less_than))
        }else{
            firebaseAuth.signInWithEmailAndPassword(etEmail.value!! , etPassword.value!!).addOnCompleteListener {
                if(it.isSuccessful){
                    state.value = firebaseAuth.currentUser!!.isEmailVerified
                }else{
                    Const.constToast(context,it.exception!!.message.toString())
                }
            }
        }
    }
}
package com.example.instagram.ui.fragment.signinfragment

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.instagram.R
import com.example.instagram.model.UserModel
import com.example.instagram.utils.Const
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    var etEmail     = MutableLiveData<String>("")
    var etPassword  = MutableLiveData<String>("")

    val state =  MutableLiveData<Boolean>()

    var firebaseAuth = FirebaseAuth.getInstance()
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var userReference = firebaseDatabase.getReference(Const.USER_REFERENCE)

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

                    userReference.orderByChild(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener( object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for( ds in snapshot.children){
                                val user = ds.getValue(UserModel::class.java)!!
                                Log.d("Here","Info :"+user.uid)

                                val pref = context.getSharedPreferences(Const.SHARED_PREF_KEY,Context.MODE_PRIVATE).edit()
                                pref.putString(Const.PUT_UID_PREF,user.uid)
                                pref.putString(Const.PUT_USER_NAME_PREF,user.userName)
                                pref.putString(Const.PUT_FULL_NAME_PREF,user.fullName)
                                pref.putString(Const.PUT_BIO_PREF,user.bio)
                                pref.putString(Const.PUT_IMAGE_PREF,user.image)
                                pref.apply()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }else{
                    Const.constToast(context,it.exception!!.message.toString())
                }
            }
        }
    }
}
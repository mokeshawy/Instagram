package com.example.instagram.ui.fragment.signinfragment

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagram.R
import com.example.instagram.baseapp.BaseApp
import com.example.instagram.datastore.DataStoreRepository
import com.example.instagram.utils.Const
import com.example.instagram.utils.CustomProgressDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel
@Inject
constructor( private val dataStoreRepository: DataStoreRepository ) : ViewModel() {

    var etEmail     = MutableLiveData<String>("")
    var etPassword  = MutableLiveData<String>("")

    val state       =  MutableLiveData<Boolean>()

    var firebaseAuth        = FirebaseAuth.getInstance()
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var userReference       = firebaseDatabase.getReference(Const.USER_REFERENCE)



    fun login(){
        if(etEmail.value!!.trim().isEmpty()){
            Const.constToast(BaseApp.appContext,BaseApp.appContext.resources.getString(R.string.err_enter_your_email))
            CustomProgressDialog.hideProgressDialog()
        }else if(etPassword.value!!.trim().isEmpty()){
            Const.constToast(BaseApp.appContext,BaseApp.appContext.resources.getString(R.string.err_enter_your_password))
            CustomProgressDialog.hideProgressDialog()
        }else if(etPassword.value!!.length < 6){
            Const.constToast(BaseApp.appContext,BaseApp.appContext.resources.getString(R.string.err_msg_the_password_not_less_than))
            CustomProgressDialog.hideProgressDialog()
        }else{
            firebaseAuth.signInWithEmailAndPassword(etEmail.value!!.toLowerCase() , etPassword.value!!).addOnCompleteListener {
                if(it.isSuccessful){
                    state.value = firebaseAuth.currentUser!!.isEmailVerified

                    userReference.child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener( object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {

                            val userName    = snapshot.child(Const.CHILD_USER_NAME).value.toString()
                            val fullName    = snapshot.child(Const.CHILD_FULL_NAME).value.toString()
                            val bio         = snapshot.child(Const.CHILD_BIO).value.toString()
                            val image       = snapshot.child(Const.CHILD_IMAGE).value.toString()



                            try {
                                insertUserInfoInLocale(userName, fullName, bio, image)

                                insertEmailAndPassword(etEmail.value!! , etPassword.value!!)
                                CustomProgressDialog.hideProgressDialog()

                            }catch (e:Exception){

                            }

                        }
                        override fun onCancelled(error: DatabaseError) {
                            Const.constToast(BaseApp.appContext,error.message)
                            CustomProgressDialog.hideProgressDialog()
                        }
                    })
                }else{
                    Const.constToast(BaseApp.appContext,it.exception!!.message.toString())
                    CustomProgressDialog.hideProgressDialog()
                }
            }
        }
    }

    // insert data to data store instance from dataStore setting.
    fun insertUserInfoInLocale(userName : String ,
                     fullName : String ,
                     bio : String ,
                     image : String) = viewModelScope.launch {
                         dataStoreRepository.insertUserInfoInLocale(userName, fullName, bio, image)
    }

    // read data from data store.
    val userName    = dataStoreRepository.readUserName
    val fullName    = dataStoreRepository.readFullName
    val bio         = dataStoreRepository.readBio
    val image       = dataStoreRepository.readImage



    // insert email and password to data store
    fun insertEmailAndPassword( email : String , password : String) = viewModelScope.launch {
        dataStoreRepository.insertCashData(email,password)
    }
    // read email and password from dataStore
    val readEmail       = dataStoreRepository.readEmail
    val readPassword    = dataStoreRepository.readPassword

    // clear cash data.
    fun clearCashEmailAndPassword() = viewModelScope.launch {
        dataStoreRepository.clearCashData()
    }
}
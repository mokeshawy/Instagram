package com.example.instagram.ui.searchfragment

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagram.adapter.UserAdapter
import com.example.instagram.model.UserModel
import com.example.instagram.utils.Const
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchViewModel : ViewModel() {

    var mUser = MutableLiveData<ArrayList<UserModel>>()
    var user  = ArrayList<UserModel>()
    var etSearchText = MutableLiveData<String>("")
    val userAdapter : UserAdapter? = null

    // firebase instance.
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var userReference = firebaseDatabase.getReference(Const.USER_REFERENCE)

    // fun for search user.
    fun searchUser( context: Context , input : String ){
        userReference.orderByChild("fullName").startAt(input).endAt(input+"\uf8ff")
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               user.clear()
                for (ds in snapshot.children){
                    val mUserModel = ds.getValue(UserModel::class.java)!!
                    user.add(mUserModel)
                }
                mUser.value = user
                userAdapter?.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message , Toast.LENGTH_SHORT).show()
            }

        })
    }

    // fun for show all data
    fun retrieveUser(context: Context){
        userReference.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(etSearchText.value!!.toString() == ""){
                    user.clear()
                    for (ds in snapshot.children){
                        val mUserModel = ds.getValue(UserModel::class.java)!!
                        user.add(mUserModel)
                    }
                    mUser.value = user
                    userAdapter?.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message , Toast.LENGTH_SHORT).show()
            }
        })
    }
}
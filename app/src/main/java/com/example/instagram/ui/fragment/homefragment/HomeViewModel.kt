package com.example.instagram.ui.fragment.homefragment

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagram.adapter.PostAdapter
import com.example.instagram.baseapp.BaseApp
import com.example.instagram.model.PostModel
import com.example.instagram.utils.Const
import com.example.instagram.utils.CustomProgressDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel : ViewModel() {

    var postAdapterLiveData     = MutableLiveData<List<PostModel>>()
    var postList                : MutableList<PostModel>? = null
    var followingList           : MutableList<PostModel>? = null

    var postAdapter : PostAdapter? = null

    // firebase instance.
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var postReference       = firebaseDatabase.getReference(Const.ADD_POST_REFERENCE)
    var followingReference  = firebaseDatabase.getReference(Const.FOLLOW_REFERENCE)


    // get time line for follow user only.
    fun checkFollowings(){
        followingList = ArrayList()
        followingReference.child(FirebaseAuth.getInstance().currentUser!!.uid).child(Const.CHILD_FOLLOWING).addValueEventListener( object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   if(snapshot.exists()){
                       (followingList as ArrayList<String>).clear()
                       for(ds in snapshot.children){
                           ds.key?.let { (followingList as ArrayList<String>).add(it) }
                       }
                       retrievePosts()
                   }
                }
                override fun onCancelled(error: DatabaseError) {
                    Const.constToast(BaseApp.appContext,error.message)
                    CustomProgressDialog.hideProgressDialog()
                }
            })
    }

    private fun retrievePosts() {
        postList = ArrayList()
        postReference.addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                postList!!.clear()
                for(ds in snapshot.children){

                    val post = ds.getValue(PostModel::class.java)!!

                    for (userId in (followingList as ArrayList<String>)){
                        if(post.publishre == userId ){
                            postList!!.add(post)
                        }
                        postAdapter?.notifyDataSetChanged()
                        postAdapterLiveData.value = postList as ArrayList<PostModel>
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Const.constToast(BaseApp.appContext,error.message)
                CustomProgressDialog.hideProgressDialog()
            }

        })
    }
}
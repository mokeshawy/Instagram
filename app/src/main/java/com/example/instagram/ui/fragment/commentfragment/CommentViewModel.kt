package com.example.instagram.ui.fragment.commentfragment

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagram.ui.adapter.CommentAdapter
import com.example.instagram.baseapp.BaseApp
import com.example.instagram.model.CommentModel
import com.example.instagram.utils.Const
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommentViewModel : ViewModel() {

    var etComment = MutableLiveData<String>("")

    var commentLiveData = MutableLiveData<ArrayList<CommentModel>>()
    var commentList    = ArrayList<CommentModel>()

    var commentAdapter : CommentAdapter? = null

    var firebaseDatabase = FirebaseDatabase.getInstance()
    var commentReference = firebaseDatabase.getReference(Const.COMMENT_REFERENCE)


    fun addComment( postId : String , userName : String , image : String){

        val map = HashMap<String , Any>()
        map[Const.CHILD_COMMENT]                    = etComment.value!!
        map[Const.CHILD_PUBLISHRE_COMMENT]          = Const.getCurrentUser()
        map[Const.CHILD_USER_NAME_COMMENT]          = userName
        map[Const.CHILD_IMAGE_USER_POST_COMMENT]    = image

        commentReference.child(postId).push().setValue(map)

        etComment.value = ""
    }

    // function fro read comment on post.
    fun readComment( postId: String){
        commentList = ArrayList()
        commentReference.child(postId).addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    commentList.clear()
                    for (ds in snapshot.children){
                        val comment = ds.getValue(CommentModel::class.java)!!
                        commentList.add(comment)
                    }
                    commentAdapter?.notifyDataSetChanged()
                    commentLiveData.value = commentList
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Const.constToast(BaseApp.appContext,error.message)
            }
        })
    }


}
package com.example.instagram.ui.fragment.notificationsfragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagram.model.NotificationModel
import com.example.instagram.ui.adapter.NotificationAdapter
import com.example.instagram.utils.Const
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class NotificationsViewModel : ViewModel() {

    var notificationLiveData = MutableLiveData<List<NotificationModel>>()
    var notificationList    : List<NotificationModel>?  = null
    var notificationAdapter : NotificationAdapter?      = null


    // firebase instance...
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val notificationReference = firebaseDatabase.getReference(Const.NOTIFICATION_REFERENCE)

    fun readNotification(){
        notificationList = ArrayList()

        notificationReference.child(Const.getCurrentUser()).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (notificationList as ArrayList<NotificationModel>).clear()

                for (ds in snapshot.children){

                    val notification = ds.getValue(NotificationModel::class.java)!!
                    (notificationList as ArrayList<NotificationModel>).add(notification)
                }
                Collections.reverse(notificationList)
                //notificationAdapter!!.notifyDataSetChanged()
                notificationLiveData.value = notificationList as ArrayList<NotificationModel>
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}
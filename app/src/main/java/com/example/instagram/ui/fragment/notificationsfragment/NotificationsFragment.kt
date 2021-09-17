package com.example.instagram.ui.fragment.notificationsfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.instagram.R
import com.example.instagram.databinding.FragmentNotificationsBinding
import com.example.instagram.model.NotificationModel
import com.example.instagram.model.PostModel
import com.example.instagram.model.UserModel
import com.example.instagram.onclickinterface.NotificationOnClick
import com.example.instagram.ui.adapter.NotificationAdapter
import com.example.instagram.utils.Const
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class NotificationsFragment : Fragment() , NotificationOnClick{

    lateinit var binding : FragmentNotificationsBinding
    private val notificationsViewModel : NotificationsViewModel by viewModels()
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with view model.
        binding.lifecycleOwner = this
        binding.notificationsFragment = notificationsViewModel

        notificationsViewModel.readNotification()
        notificationsViewModel.notificationLiveData.observe(viewLifecycleOwner, Observer {
            binding.recyclerViewNotifications.adapter = NotificationAdapter( it , this)
        })
    }

    override fun onClick( viewHolder: NotificationAdapter.ViewHolder, notificationModel: NotificationModel,petition: Int ) {

        if( notificationModel.text == "started following you"){
            viewHolder.binding.tvNotification.text = "started following you"
        }else if(notificationModel.text == "liked your post"){
            viewHolder.binding.tvNotification.text = "liked your post"
        }else if (notificationModel.text.contains("commented:") ){
            viewHolder.binding.tvNotification.text = notificationModel.text.replace("commented:" , "commented: ")
        }else{
            viewHolder.binding.tvNotification.text = notificationModel.text
        }


        // firebase instance.
        val firebaseDatabase    = FirebaseDatabase.getInstance()

        // retrieve data from user reference.
        firebaseDatabase.getReference(Const.USER_REFERENCE).child( notificationModel.userid).addValueEventListener( object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    val user = snapshot.getValue(UserModel::class.java)!!
                    Picasso.get().load(user.image).into(viewHolder.binding.ivNotificationProfileImage)
                    viewHolder.binding.tvNotificationUserName.text   = user.userName

                    // open post image in notification page...
                    viewHolder.itemView.setOnClickListener {
                        if(notificationModel.isPost){ // when isPost true will open post image ...
                            val bundle = bundleOf(Const.BUNDLE_POST_ID to notificationModel.postId)
                            findNavController().navigate(R.id.action_notificationsFragment_to_homeFragment,bundle)
                        }else{ // when isPost false will open user profile..
                            val bundle = Bundle()
                            bundle.putSerializable(Const.BUNDLE_USER_MODEL,user)
                            findNavController().navigate(R.id.action_notificationsFragment_to_profileFragment,bundle)
                        }
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Const.constToast(viewHolder.itemView.context,error.message)
            }
        })


        // when user click like or write comment on any post will show image post .. and when start following another user will gone image for show post..
        if( notificationModel.isPost){

            viewHolder.binding.ivNotificationPostImage.visibility = View.VISIBLE
            firebaseDatabase.getReference(Const.ADD_POST_REFERENCE).child(notificationModel.postId).addValueEventListener( object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val image = snapshot.child(Const.CHILD_POST_IMAGE).value.toString()
                        Picasso.get().load(image).into(viewHolder.binding.ivNotificationPostImage)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }else{
            viewHolder.binding.ivNotificationPostImage.visibility = View.GONE
        }
    }
}
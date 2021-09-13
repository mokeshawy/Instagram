package com.example.instagram.ui.fragment.homefragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.instagram.R
import com.example.instagram.adapter.PostAdapter
import com.example.instagram.baseapp.BaseApp
import com.example.instagram.databinding.FragmentHomeBinding
import com.example.instagram.model.PostModel
import com.example.instagram.model.UserModel
import com.example.instagram.onclickinterface.PostOnClickListener
import com.example.instagram.utils.Const
import com.example.instagram.utils.CustomProgressDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeFragment : Fragment() , PostOnClickListener{

    lateinit var binding        : FragmentHomeBinding
    private val homeViewModel   : HomeViewModel by viewModels()
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with view model.
        binding.lifecycleOwner  = this
        binding.homeFragment    = homeViewModel


        try {
            // call fun for check followings when open app will show direct.
            homeViewModel.checkFollowings()
            binding.swapLayout.setOnRefreshListener { // swap refresh for data.
                homeViewModel.checkFollowings() // call fun for check followings when refresh layout.
                activity?.recreate()
                GlobalScope.launch {
                    binding.swapLayout.isRefreshing = false
                    binding.swapLayout.setColorSchemeResources(R.color.colorPrimary)
                   delay(3000)
                }
            }
        }catch (e:Exception){

        }

        // when internet connection will be show data.
        if(Const.isNetworkAvailable(requireActivity())){
            homeViewModel.postAdapterLiveData.observe(viewLifecycleOwner, Observer {
                CustomProgressDialog.show(requireActivity(),resources.getString(R.string.msg_please_waite))
                binding.recyclerViewHome.adapter = PostAdapter(it,this)
                CustomProgressDialog.hideProgressDialog()
            })
        }else{
            binding.recyclerViewHome.visibility = View.GONE
            binding.tvNoInternet.visibility = View.VISIBLE
        }

    }

    // on click for post adapter.
    override fun onClick(viewHolder: PostAdapter.ViewHolder, postModel: PostModel, position: Int) {

        // firebase instance.
        val firebaseDatabase = FirebaseDatabase.getInstance()

        // contents of the view with that element
        Picasso.get().load(postModel.postImage).into(viewHolder.binding.ivPostImageHome)
        if( postModel.description == ""){
            viewHolder.binding.tvDescription!!.visibility = View.GONE
        }else{
            viewHolder.binding.tvDescription!!.visibility = View.VISIBLE
            viewHolder.binding.tvDescription!!.text = postModel.description
        }

        // retrieve data from user reference.
        firebaseDatabase.getReference(Const.USER_REFERENCE).child(postModel.publishre).addValueEventListener( object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val user = snapshot.getValue(UserModel::class.java)!!
                    Picasso.get().load(user.image).into(viewHolder.binding.ivUserProfileImageSearch)
                    viewHolder.binding.tvUserNameSearch.text   = user.userName
                    viewHolder.binding.tvPublisher.text   = user.fullName

                    // comment button.
                    viewHolder.binding.ivPostImageCommentBtn.setOnClickListener {

                        // send photo for post and profile image for user to comment fragment by bundle.
                        val bundle = bundleOf(Const.BUNDLE_IMAGE_POST to postModel.postImage ,
                            Const.BUNDLE_POST_ID  to postModel.postId ,
                            Const.BUNDLE_IMAGE_PROFILE to user.image ,
                            Const.BUNDLE_USER_NAME to user.userName ,
                            Const.BUNDLE_IMAGE_USER_POST_COMMENT to user.image)

                       findNavController().navigate(R.id.action_homeFragment_to_commentFragment,bundle)
                    }

                    viewHolder.binding.tvComments.setOnClickListener {
                        // send photo for post and profile image for user to comment fragment by bundle.
                        val bundle = bundleOf(Const.BUNDLE_IMAGE_POST to postModel.postImage , Const.BUNDLE_POST_ID  to postModel.postId , Const.BUNDLE_IMAGE_PROFILE to user.image )
                        findNavController().navigate(R.id.action_homeFragment_to_commentFragment,bundle)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Const.constToast(viewHolder.itemView.context,error.message)
            }
        })

        // call function for is likes from home viewModel.
        homeViewModel.isLikes(postModel.postId , viewHolder.binding.ivPostImageLikeBtn)

        // call function for get likes number.
        homeViewModel.numberOfLikes(viewHolder.binding.tvLikes,postModel.postId)

        // call function for get comment number from home viewModel.
        homeViewModel.numberOfComment(viewHolder.binding.tvComments,postModel.postId)
        // like button.
        viewHolder.binding.ivPostImageLikeBtn.setOnClickListener {
            if( viewHolder.binding.ivPostImageLikeBtn.tag == "Like"){
                firebaseDatabase.getReference(Const.LIKES_REFERENCE)
                    .child(postModel.postId).child(Const.getCurrentUser()).setValue(true)
            }else{
                firebaseDatabase.getReference(Const.LIKES_REFERENCE)
                    .child(postModel.postId).child(Const.getCurrentUser()).removeValue()
            }
        }
    }
}
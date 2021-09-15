package com.example.instagram.ui.fragment.profilefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.instagram.R
import com.example.instagram.databinding.FragmentProfileBinding
import com.example.instagram.model.PostModel
import com.example.instagram.model.UserModel
import com.example.instagram.onclickinterface.MyPhotoOnClickLisrener
import com.example.instagram.ui.adapter.MyPhotoAdapter
import com.example.instagram.ui.fragment.signinfragment.SignInViewModel
import com.example.instagram.utils.Const
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collect

class ProfileFragment : Fragment() , MyPhotoOnClickLisrener{

    lateinit var binding            : FragmentProfileBinding
    private val profileViewModel    : ProfileViewModel by viewModels()
    private var mUserModel          : UserModel?= null
    private val signInViewModel     : SignInViewModel by activityViewModels()
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with view model
        binding.lifecycleOwner = this
        binding.profileFragment = profileViewModel


        // get user model object by bundle when user entry from search page.
        if( arguments?.containsKey(Const.BUNDLE_USER_MODEL) == true){

            mUserModel = arguments?.getSerializable(Const.BUNDLE_USER_MODEL) as UserModel

            if( mUserModel!!.uid == Const.getCurrentUser()){

                // show edit profile
                profileViewModel.tvAccountSetting.value = resources.getString(R.string.text_edit_profile)

            }else if(mUserModel!!.uid != Const.getCurrentUser()){
                profileViewModel.checkFollowAndFollowingButtonsStatus(mUserModel!!)
            }
            // call function getFollowing.
            profileViewModel.getFollowings(mUserModel!!.uid)

            // call function getFollowers.
            profileViewModel.getFollowers(mUserModel!!.uid)

            // show info user show from search.
            profileViewModel.tvShowFullName.value   = mUserModel!!.fullName
            profileViewModel.tvShowUserName.value   = mUserModel!!.userName
            profileViewModel.tvShowBio.value        = mUserModel!!.bio
            Picasso.get().load(mUserModel!!.image).into(binding.ivUserProfile)

        }else{ // when user entry from profile page.

            // call function show user info in profile page.
            showUserInfo()

            // show edit profile
            profileViewModel.tvAccountSetting.value = resources.getString(R.string.text_edit_profile)

            // go to account setting.
            binding.tvAccountSetting.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_accountSettingFragment)
            }

            // call function getFollowing.
            profileViewModel.getFollowings(Const.getCurrentUser())

            // call function getFollowers.
            profileViewModel.getFollowers(Const.getCurrentUser())
        }

        binding.tvAccountSetting.setOnClickListener {
            when (profileViewModel.tvAccountSetting.value) {
                resources.getString(R.string.text_edit_profile) -> {// when text view show edit text will go setting page
                    findNavController().navigate(R.id.action_profileFragment_to_accountSettingFragment)
                }
                resources.getString(R.string.text_follow) -> { // when text view show follow will go follow user
                    profileViewModel.follow(mUserModel!!)
                }
                resources.getString(R.string.text_remove) -> { // when text view show remove will go remove user
                    profileViewModel.unFollow(mUserModel!!)
                }
            }
        }

        // btn for show photo of post..
        binding.ivBtnViewPic.setOnClickListener {
            binding.rvShowImagePost.visibility = View.VISIBLE
            binding.rvShowImageSave.visibility = View.GONE
        }

        // btn for show save photo of post..
        binding.ivBtnSaveImage.setOnClickListener {
            binding.rvShowImageSave.visibility = View.VISIBLE
            binding.rvShowImagePost.visibility = View.GONE
        }

        // show photo post..
        profileViewModel.myPhoto()
        profileViewModel.postListLiveData.observe(viewLifecycleOwner, Observer {
            binding.rvShowImagePost.adapter = MyPhotoAdapter(it,this)
        })

        // show data of saved post..
        profileViewModel.postListSaved()
        profileViewModel.postSavedLiveData.observe(viewLifecycleOwner, Observer {
            binding.rvShowImageSave.adapter = MyPhotoAdapter(it,this)
        })

        // call function for get number of post.
        profileViewModel.getTotalNumberOfPost()

        // get all following list...
        binding.tvTotalFollowing.setOnClickListener {
            val bundle = bundleOf(Const.BUNDLE_TITLE to resources.getString(R.string.title_following), Const.BUNDLE_ID to Const.getCurrentUser())
            findNavController().navigate(R.id.action_profileFragment_to_searchFragment,bundle)
        }

        // get all followers list ...
        binding.tvTotalFollowers.setOnClickListener {
            val bundle = bundleOf(Const.BUNDLE_TITLE to resources.getString(R.string.title_followers), Const.BUNDLE_ID to Const.getCurrentUser())
            findNavController().navigate(R.id.action_profileFragment_to_searchFragment,bundle)
        }
    }

    // fun show user info from dataStore.
    private fun showUserInfo(){

        // show data for user from dataStore.
        lifecycleScope.launchWhenCreated {
            signInViewModel.userName.collect {
                profileViewModel.tvShowUserName.value = it
            }
        }

        lifecycleScope.launchWhenCreated {
            signInViewModel.fullName.collect {
                profileViewModel.tvShowFullName.value = it
            }
        }

        lifecycleScope.launchWhenCreated {
            signInViewModel.bio.collect {
               profileViewModel.tvShowBio.value = it
            }
        }

        lifecycleScope.launchWhenCreated {
            signInViewModel.image.collect {
                Picasso.get().load(it).into(binding.ivUserProfile)
            }
        }
    }

    // on click for my photo adapter
    override fun onClick( viewHolder: MyPhotoAdapter.ViewHolder, postModel: PostModel, position: Int) {

        // when you need show details for your post from profile page.
        viewHolder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(Const.BUNDLE_POST_MODEL,postModel)
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment,bundle)
        }
    }
}
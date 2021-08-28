package com.example.instagram.ui.fragment.profilefragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.instagram.R
import com.example.instagram.databinding.FragmentProfileBinding
import com.example.instagram.model.UserModel
import com.example.instagram.utils.Const
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    lateinit var binding : FragmentProfileBinding
    private val profileViewModel : ProfileViewModel by viewModels()
    private var mUserModel : UserModel?= null

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

        // get shared preference data.
        val pref        = requireActivity().getSharedPreferences(Const.SHARED_PREF_KEY,Context.MODE_PRIVATE)
        val uid         = pref.getString(Const.PUT_UID_PREF,"")
        val userName    = pref.getString(Const.PUT_USER_NAME_PREF,"")
        val fullName    = pref.getString(Const.PUT_FULL_NAME_PREF,"")
        val bio         = pref.getString(Const.PUT_BIO_PREF,"")
        val image       = pref.getString(Const.PUT_IMAGE_PREF,"")






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

            // show info for user
            profileViewModel.tvShowFullName.value   = fullName
            profileViewModel.tvShowUserName.value   = userName
            profileViewModel.tvShowBio.value        = bio
            Picasso.get().load(image).into(binding.ivUserProfile)

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
    }

}
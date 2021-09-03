package com.example.instagram.ui.fragment.accountsettingfragemnt

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.instagram.R
import com.example.instagram.databinding.FragmentAccountSettingBinding
import com.example.instagram.datastore.DataStoreRepository
import com.example.instagram.ui.fragment.signinfragment.SignInViewModel
import com.example.instagram.utils.Const
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AccountSettingFragment : Fragment() {

    lateinit var binding : FragmentAccountSettingBinding
    lateinit var profileUri : Uri
    private val accountSettingViewModel : AccountSettingViewModel by viewModels()
    private val signInViewModel : SignInViewModel by activityViewModels()
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with view model
        binding.lifecycleOwner = this
        binding.accountSettingFragment = accountSettingViewModel


        // default url for image.
        profileUri = Const.DEFAULT_IMAGE_PROFILE.toUri()

        // call fun show user info.
        //accountSettingViewModel.showUserInfo()

        // show data for user from dataStore.
        lifecycleScope.launchWhenCreated {
            signInViewModel.userName.collect {
                accountSettingViewModel.etUserName.value = it
            }
        }

        lifecycleScope.launchWhenCreated {
            signInViewModel.fullName.collect {
                accountSettingViewModel.etFullName.value = it
            }
        }

        lifecycleScope.launchWhenCreated {
            signInViewModel.bio.collect {
                accountSettingViewModel.etBio.value = it
            }
        }

        lifecycleScope.launchWhenCreated {
            signInViewModel.image.collect {
                Picasso.get().load(it).into(binding.ivProfile)
            }
        }

        // show image on imageView using live data.
        accountSettingViewModel.image.observe(viewLifecycleOwner, Observer {
            //Picasso.get().load(it).into(binding.ivProfile)
        })

        // make log out from app.
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_accountSettingFragment_to_signInFragment)
        }

        binding.btnDeleteAccount.setOnClickListener {
            FirebaseAuth.getInstance().currentUser!!.delete()
            FirebaseDatabase.getInstance().getReference(Const.USER_REFERENCE).child(FirebaseAuth.getInstance().currentUser?.uid.toString()).removeValue()
        }

        // btn for select image.
        binding.ivProfile.setOnClickListener {
            pickImage()
        }

        // confirm update info for user.
        binding.ivBtnSaveInfoProfile.setOnClickListener {
            accountSettingViewModel.updateProfile(profileUri)
        }
    }

    @Suppress("DEPRECATION")
    fun pickImage(){
        if(ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK){
            profileUri = data?.data!!
            binding.ivProfile.setImageURI(profileUri)
        }
    }
}
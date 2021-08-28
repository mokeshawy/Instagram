package com.example.instagram.ui.fragment.accountsettingfragemnt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.instagram.R
import com.example.instagram.databinding.FragmentAccountSettingBinding
import com.example.instagram.utils.Const
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AccountSettingFragment : Fragment() {

    lateinit var binding : FragmentAccountSettingBinding
    private val accountSettingViewModel : AccountSettingViewModel by viewModels()
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


        // make log out from app.
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_accountSettingFragment_to_signInFragment)
        }

        binding.btnDeleteAccount.setOnClickListener {
            FirebaseAuth.getInstance().currentUser!!.delete()
            FirebaseDatabase.getInstance().getReference(Const.USER_REFERENCE).child(FirebaseAuth.getInstance().currentUser?.uid.toString()).removeValue()
        }
    }
}
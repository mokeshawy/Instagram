package com.example.instagram.accountsettingfragemnt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.instagram.R
import com.example.instagram.databinding.FragmentAccountSettingBinding

class AccountSettingFragment : Fragment() {

    lateinit var binding : FragmentAccountSettingBinding
    private val accountSettingViewModel : AccountSettingViewModel by viewModels()
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with view model
        binding.lifecycleOwner = this
        binding.accountSettingFragment = accountSettingViewModel
    }
}
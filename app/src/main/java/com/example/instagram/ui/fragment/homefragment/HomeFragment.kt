package com.example.instagram.ui.fragment.homefragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.instagram.R
import com.example.instagram.adapter.PostAdapter
import com.example.instagram.baseapp.BaseApp
import com.example.instagram.databinding.FragmentHomeBinding
import com.example.instagram.model.PostModel
import com.example.instagram.utils.Const
import com.example.instagram.utils.CustomProgressDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class HomeFragment : Fragment() {

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
            homeViewModel.checkFollowings()
        }catch (e:Exception){

        }
        homeViewModel.postAdapterLiveData.observe(viewLifecycleOwner, Observer {
            CustomProgressDialog.show(requireActivity(),resources.getString(R.string.msg_please_waite))
                binding.recyclerViewHome.adapter = PostAdapter(it)
                CustomProgressDialog.hideProgressDialog()
        })
//        if(Const.isNetworkAvailable(requireActivity())){
//
//        }else{
//            Const.constToast(requireActivity(),"Please check internet connection")
//            CustomProgressDialog.hideProgressDialog()
//            binding.recyclerViewHome.visibility = View.GONE
//        }

    }
}
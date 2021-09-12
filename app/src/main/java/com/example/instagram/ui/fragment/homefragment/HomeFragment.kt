package com.example.instagram.ui.fragment.homefragment

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
import com.example.instagram.utils.Const
import com.example.instagram.utils.CustomProgressDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
                binding.recyclerViewHome.adapter = PostAdapter(it,homeViewModel)
                CustomProgressDialog.hideProgressDialog()
            })
        }else{
            binding.recyclerViewHome.visibility = View.GONE
            binding.tvNoInternet.visibility = View.VISIBLE
        }

    }
}
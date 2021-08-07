package com.example.instagram.ui.signinfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.instagram.R
import com.example.instagram.databinding.FragmentSigninBinding
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {

    lateinit var binding : FragmentSigninBinding
    private val signInVieModel : SignInViewModel by viewModels()
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with viewModel
        binding.lifecycleOwner = this
        binding.signInFragment = signInVieModel

        // got to singUp
        binding.tvGoToSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        // signIn.
        binding.btnLogin.setOnClickListener {
            signInVieModel.login(requireActivity(),view)
        }

    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser != null){
            findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
        }
    }
}
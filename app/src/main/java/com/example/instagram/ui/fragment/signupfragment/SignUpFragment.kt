package com.example.instagram.ui.fragment.signupfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.instagram.R
import com.example.instagram.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    lateinit var binding : FragmentSignUpBinding
    private val signUpViewModel : SignUpViewModel by viewModels()
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with viewModel
        binding.lifecycleOwner = this
        binding.signUpFragment = signUpViewModel


        // go to signIn
        binding.tvGoToSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        //create new account.
        binding.btnSignUp.setOnClickListener {
            signUpViewModel.signUp()

            // call observer fun.
            observer()
        }
    }

    private fun observer(){
        signUpViewModel.state.observe(viewLifecycleOwner, Observer { signUp ->
            if(signUp){
                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
            }else{
                return@Observer
            }
        })
    }
}
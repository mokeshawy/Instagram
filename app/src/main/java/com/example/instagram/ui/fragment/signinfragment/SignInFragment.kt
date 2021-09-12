package com.example.instagram.ui.fragment.signinfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.instagram.R
import com.example.instagram.databinding.FragmentSigninBinding
import com.example.instagram.utils.Const
import com.example.instagram.utils.CustomProgressDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignInFragment : Fragment() {

    lateinit var binding : FragmentSigninBinding
    private val signInVieModel : SignInViewModel by activityViewModels()
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

            // show progress bar.
            CustomProgressDialog.show(requireActivity(),resources.getString(R.string.msg_please_waite))

            // call fun login from view model.
            signInVieModel.login()

            // clear user email and password from input after 60 min.
            CoroutineScope(Dispatchers.Main).launch {
                delay(600000)
                signInVieModel.clearCashEmailAndPassword()
            }

            // call fun observer.
            observe()
        }

        // call function show email and password in input login.
        showUserEmailAndPassword()

    }

    override fun onStart() {
        super.onStart()
        try{
            if(FirebaseAuth.getInstance().currentUser != null){
                if(FirebaseAuth.getInstance().currentUser!!.isEmailVerified){
                    findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                }else{
                    Const.constToast(requireActivity(), resources.getString(R.string.err_msg_confirm_email))
                }
            }
        }catch (e:Exception){

        }
    }

    // fun observer.
    private fun observe(){
        signInVieModel.state.observe(viewLifecycleOwner, Observer {
            if(it){
              findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
            }else{
                Const.constToast(requireActivity(), resources.getString(R.string.err_msg_confirm_email))
            }
        })
    }

    // show user email and password from dataStore.
    private fun showUserEmailAndPassword(){
        lifecycleScope.launchWhenCreated {
            signInVieModel.readEmail.collect {
                signInVieModel.etEmail.value = it
            }
        }
        lifecycleScope.launchWhenCreated {
            signInVieModel.readPassword.collect {
                signInVieModel.etPassword.value = it
            }
        }
    }
}
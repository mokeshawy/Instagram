package com.example.instagram.ui.fragment.signinfragment

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.instagram.R
import com.example.instagram.databinding.FragmentSigninBinding
import com.example.instagram.utils.Const
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
            // call fun login from view model.
            signInVieModel.login()

            // call fun observer.
            observe()
        }

        // save email and password in input log in and clear it after 1 minute.
//        val pref = requireActivity().getSharedPreferences(Const.SHARED_CASH_PREF_NAME, Context.MODE_PRIVATE)
//        if(pref.getLong("Time",-1)> System.currentTimeMillis()){
//            signInVieModel.etEmail.value    = pref.getString(Const.EMAIL_KEY,"")
//            signInVieModel.etPassword.value = pref.getString(Const.PASSWORD_KEY,"")
//        }else{
//            pref.edit().clear().apply()
//        }

    }

    override fun onStart() {
        super.onStart()
        try{
            if(FirebaseAuth.getInstance().currentUser!!.uid != null){
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
}
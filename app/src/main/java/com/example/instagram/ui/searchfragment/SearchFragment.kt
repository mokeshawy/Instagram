package com.example.instagram.ui.searchfragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.instagram.`interface`.OnClick
import com.example.instagram.adapter.UserAdapter
import com.example.instagram.databinding.FragmentSearchBinding
import com.example.instagram.model.UserModel
import com.example.instagram.utils.Const
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class SearchFragment : Fragment() , OnClick{

    lateinit var binding : FragmentSearchBinding
    private val searchViewModel : SearchViewModel by viewModels()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with view model.
        binding.lifecycleOwner = this
        binding.searchFragment = searchViewModel


        searchViewModel.mUser.observe(viewLifecycleOwner, Observer {
            binding.recyclerViewSearch.adapter = UserAdapter(requireActivity() , it ,true,this)
        })

        binding.searchEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if( searchViewModel.etSearchText.value.toString() == ""){

                }else{
                    binding.recyclerViewSearch.visibility = View.VISIBLE
                    searchViewModel.retrieveUser(requireActivity())
                    searchViewModel.searchUser(requireActivity(),s.toString().lowercase(Locale.getDefault()))
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    // implement for fun from interface onClick on item userAdapter.
    override fun onClick(viewHolder: UserAdapter.ViewHolder, userModel: UserModel, position: Int) {

        // call fun for check following status.
        searchViewModel.checkFollowingStatus(userModel.uid, viewHolder.binding.followBtnSearch)

        // firebase instance.
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val followReference = firebaseDatabase.getReference(Const.FOLLOW_REFERENCE)

        viewHolder.binding.followBtnSearch.setOnClickListener {
            if( viewHolder.binding.followBtnSearch.text.toString() == Const.BTN_FOLLOW){
                followReference.child(Const.getCurrentUser())
                    .child(Const.CHILD_FOLLOWING).child(userModel.uid).setValue(true).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        followReference.child(Const.CHILD_FOLLOWERS).child(userModel.uid).child(Const.getCurrentUser()).setValue(true)
                    }
                }
            }else{
                followReference.child(Const.getCurrentUser())
                    .child(Const.CHILD_FOLLOWING).child(userModel.uid).removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            followReference.child(Const.CHILD_FOLLOWERS).child(userModel.uid)
                                .child(Const.getCurrentUser()).removeValue()
                        }
                    }
            }
        }
    }
}
package com.example.instagram.ui.searchfragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.instagram.adapter.UserAdapter
import com.example.instagram.databinding.FragmentSearchBinding
import com.example.instagram.model.UserModel
import com.example.instagram.utils.Const
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {

    lateinit var binding : FragmentSearchBinding
    private val searchViewModel : SearchViewModel by viewModels()
    private var mUser : MutableList<UserModel>? = null
    private var userAdapter : UserAdapter? = null

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
            binding.recyclerViewSearch.adapter = UserAdapter(requireActivity() , it ,true)
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
}
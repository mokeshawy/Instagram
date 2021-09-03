package com.example.instagram.ui.fragment.searchfragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.instagram.adapter.UserAdapter
import com.example.instagram.databinding.FragmentSearchBinding
import com.example.instagram.utils.Const


class SearchFragment : Fragment() {

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
            binding.recyclerViewSearch.adapter = UserAdapter(it,true, searchViewModel,this)
        })

        binding.searchEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if( searchViewModel.etSearchText.value.toString() == ""){

                }else{
                    binding.recyclerViewSearch.visibility = View.VISIBLE
                    searchViewModel.retrieveUser()
                    searchViewModel.searchUser(s.toString().toLowerCase())
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    // implement for fun from interface onClick on item userAdapter.
//    override fun onClick(viewHolder: UserAdapter.ViewHolder, userModel: UserModel, position: Int) {
//
//        if(FirebaseAuth.getInstance().currentUser!!.uid == userModel.uid){
//            viewHolder.binding.followBtnSearch.visibility = View.GONE
//        }else{
//            viewHolder.binding.followBtnSearch.visibility = View.VISIBLE
//        }
//        // call fun for check following status.
//        searchViewModel.checkFollowingStatus(userModel.uid,viewHolder.binding.followBtnSearch)
//
//        // click follow and unFollow.
//        viewHolder.binding.followBtnSearch.setOnClickListener {
//            // call fun follow and unFollow
//            searchViewModel.followAndUnFollow(viewHolder.binding.followBtnSearch,userModel)
//        }
//
//        viewHolder.itemView.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putSerializable(Const.BUNDLE_USER_MODEL , userModel)
//            findNavController().navigate(R.id.action_searchFragment_to_profileFragment,bundle)
//        }
//    }
}
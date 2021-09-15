package com.example.instagram.ui.fragment.searchfragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.instagram.R
import com.example.instagram.ui.adapter.UserAdapter
import com.example.instagram.databinding.FragmentSearchBinding
import com.example.instagram.model.UserModel
import com.example.instagram.onclickinterface.UserOnClickListener
import com.example.instagram.utils.Const
import com.google.firebase.auth.FirebaseAuth


class SearchFragment : Fragment() , UserOnClickListener{

    lateinit var binding : FragmentSearchBinding
    private val searchViewModel : SearchViewModel by viewModels()

    // for get data likes and following and followers
    var id      : String = ""
    var title   : String = ""

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



        // show data fro user when start search.
        searchViewModel.mUser.observe(viewLifecycleOwner, Observer {
            binding.recyclerViewSearch.adapter = UserAdapter(it,this,true)
        })

        // handle search edit text.
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




        // for operation show all likes and following and followers...
        if( arguments?.containsKey("title") == true || arguments?.containsKey("id") == true ){

            binding.relativeToolBar.visibility = View.GONE

                    title   = arguments?.getString(Const.BUNDLE_TITLE).toString()
            id      = arguments?.getString(Const.BUNDLE_ID).toString()


            binding.searchToolbar.setNavigationIcon(R.drawable.ic_vector_back)
            binding.searchToolbar.title = title
            binding.searchToolbar.setNavigationOnClickListener {
                activity?.finish()
            }

            when(title){
                resources.getString(R.string.title_following) -> searchViewModel.getFollowing(id)

                resources.getString(R.string.title_followers) -> searchViewModel.getFollowers(id)

                resources.getString(R.string.title_likes) -> searchViewModel.getLikes(id)
            }

            searchViewModel.userLIstLiveData.observe(viewLifecycleOwner, Observer {
                binding.recyclerViewSearch.visibility = View.VISIBLE
                binding.recyclerViewSearch.adapter = UserAdapter(it,this)
            })
        }


    }

    // onClick for user adapter.
    override fun onClick(viewHolder: UserAdapter.ViewHolder, userModel: UserModel, position: Int) {

        // all operation form search viewModel.
        if(FirebaseAuth.getInstance().currentUser!!.uid == userModel.uid){
            viewHolder.binding.followBtnSearch.visibility = View.GONE
        }else{
            viewHolder.binding.followBtnSearch.visibility = View.VISIBLE
        }
        // call fun for check following status.
        searchViewModel.checkFollowingStatus(userModel.uid,viewHolder.binding.followBtnSearch)

        // click follow and unFollow.
        viewHolder.binding.followBtnSearch.setOnClickListener {
            // call fun follow and unFollow
            searchViewModel.followAndUnFollow(viewHolder.binding.followBtnSearch,userModel)
        }

        viewHolder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(Const.BUNDLE_USER_MODEL , userModel)
            findNavController().navigate(R.id.action_searchFragment_to_profileFragment,bundle)
        }
    }
}
package com.example.instagram.ui.fragment.commentfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.instagram.R
import com.example.instagram.adapter.CommentAdapter
import com.example.instagram.databinding.FragmentCommentBinding
import com.example.instagram.model.PostModel
import com.example.instagram.utils.Const
import com.squareup.picasso.Picasso

class CommentFragment : Fragment() {

    lateinit var binding : FragmentCommentBinding
    private val commentViewModel : CommentViewModel by viewModels()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with view model
        binding.lifecycleOwner  = this
        binding.commentFragment = commentViewModel


        // get photo for post from post adapter by bundle.
        val imagePost = arguments?.getString(Const.BUNDLE_IMAGE_POST)
        Picasso.get().load(imagePost).into(binding.ivPostComment)

        // get photo for user profile from post adapter by bundle.
        val imageProfile = arguments?.getString(Const.BUNDLE_IMAGE_PROFILE)
        Picasso.get().load(imageProfile).into(binding.ivUserProfileImageComment)

        // get postId from post adapter by bundle.
        val postId = arguments?.getString(Const.BUNDLE_POST_ID)

        // get userName from post adapter by bundle.
        val userName = arguments?.getString(Const.BUNDLE_USER_NAME)

        val image = arguments?.getString(Const.BUNDLE_IMAGE_USER_POST_COMMENT)

        // btn add comment on post.
        binding.tvPostComment.setOnClickListener {
            commentViewModel.addComment(postId.toString(),userName.toString(),image.toString())
        }

        commentViewModel.readComment(postId.toString())
        commentViewModel.commentLiveData.observe(viewLifecycleOwner, Observer {
            binding.rvComment.adapter = CommentAdapter(it)
        })
    }
}
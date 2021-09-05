package com.example.instagram.ui.fragment.addpostfragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.example.instagram.R
import com.example.instagram.databinding.FragmentAddPostBinding
import com.example.instagram.utils.Const
import com.example.instagram.utils.CustomProgressDialog

class AddPostFragment : Fragment() {

    lateinit var binding : FragmentAddPostBinding
    private val addPostViewModel : AddPostViewModel by viewModels()
    lateinit var imageUri : Uri
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with viewModel.
        binding.lifecycleOwner = this
        binding.addPostFragment = addPostViewModel

        // initialize image uri
        imageUri = Const.DEFAULT_IMAGE_ADD_POST.toUri()

        binding.ivAddPost.setOnClickListener {
            // show progress bar.
            CustomProgressDialog.show(requireActivity(),resources.getString(R.string.msg_please_waite))
            pickImageForAddPost()
        }

        binding.ivBtnAddPost.setOnClickListener {
            // show progress bar.
            CustomProgressDialog.show(requireActivity(),resources.getString(R.string.msg_please_waite))
            addPostViewModel.addPost(imageUri)
        }
    }

    @Suppress("DEPRECATION")
    fun pickImageForAddPost(){

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,2)
        CustomProgressDialog.hideProgressDialog()
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 2 && resultCode == AppCompatActivity.RESULT_OK){
            imageUri = data?.data!!
            binding.ivAddPost.setImageURI(imageUri)
        }
    }
}
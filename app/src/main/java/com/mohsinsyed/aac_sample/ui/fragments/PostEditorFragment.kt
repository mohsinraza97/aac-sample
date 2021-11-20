package com.mohsinsyed.aac_sample.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mohsinsyed.aac_sample.R
import com.mohsinsyed.aac_sample.data.models.entities.Post
import com.mohsinsyed.aac_sample.data.enums.EditorMode
import com.mohsinsyed.aac_sample.databinding.FragmentPostEditorBinding
import com.mohsinsyed.aac_sample.ui.view_models.PostViewModel
import com.mohsinsyed.aac_sample.utils.constants.AppConstants
import com.mohsinsyed.aac_sample.utils.extensions.onTextChanged
import com.mohsinsyed.aac_sample.utils.extensions.setToolBarTitle
import com.mohsinsyed.aac_sample.utils.extensions.showSnackBar
import com.mohsinsyed.aac_sample.utils.extensions.toggleError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostEditorFragment : Fragment() {

    private var binding: FragmentPostEditorBinding? = null
    private val postViewModel by activityViewModels<PostViewModel>()
    private var post: Post? = null
    private var mode = EditorMode.ADD

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_editor, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListeners()
        setUpObservers()
    }

    private fun init() {
        post = arguments?.getSerializable(AppConstants.IntentConstants.EXTRA_KEY_POST) as? Post?
        val toolbarTitle = if (post == null) {
            mode = EditorMode.ADD
            getString(R.string.add_post)
        } else {
            mode = EditorMode.EDIT
            getString(R.string.edit_post)
        }
        setToolBarTitle(toolbarTitle, true)

        binding?.etTitle?.setText(post?.title)
        binding?.etDescription?.setText(post?.description)
    }

    private fun setListeners() {
        binding?.etTitle?.onTextChanged { binding?.inputLayoutTitle?.toggleError() }
        binding?.etDescription?.onTextChanged { binding?.inputLayoutDescription?.toggleError() }
        binding?.btnCancel?.setOnClickListener { findNavController().popBackStack() }
        binding?.btnSubmit?.setOnClickListener { onSubmit() }
    }

    private fun onSubmit() {
        val title = binding?.etTitle?.text?.toString()
        val description = binding?.etDescription?.text?.toString()

        if (isValid(title, description)) {
            val p = Post(title = title, description = description)
            if (mode == EditorMode.ADD) {
                postViewModel.create(p)
            } else if (mode == EditorMode.EDIT) {
                p.id = post?.id
                postViewModel.update(p)
            }
        }
    }

    private fun isValid(title: String?, body: String?): Boolean {
        var valid = false
        when {
            title?.isEmpty() == true -> {
                binding?.inputLayoutTitle?.toggleError(getString(R.string.error_required))
            }
            body?.isEmpty() == true -> {
                binding?.inputLayoutDescription?.toggleError(getString(R.string.error_required))
            }
            else -> valid = true
        }
        return valid
    }

    private fun setUpObservers() {
        postViewModel.events.observe(viewLifecycleOwner, { event ->
            when (event) {
                is PostViewModel.PostEvents.Added -> {
                    binding?.root?.showSnackBar(getString(R.string.post_action_message, "added"))
                    findNavController().popBackStack()
                }
                is PostViewModel.PostEvents.Updated -> {
                    binding?.root?.showSnackBar(getString(R.string.post_action_message, "updated"))
                    findNavController().popBackStack()
                }
            }
        })
    }
}
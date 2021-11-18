package com.mohsinsyed.aac_sample.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.mohsinsyed.aac_sample.R
import com.mohsinsyed.aac_sample.data.entities.Post
import com.mohsinsyed.aac_sample.databinding.FragmentPostDetailBinding
import com.mohsinsyed.aac_sample.utils.extensions.setToolBarTitle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailFragment : Fragment() {
    private var binding: FragmentPostDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setToolBarTitle(getString(R.string.fragment_title_post_detail), true)
        val post = arguments?.getSerializable("post") as? Post?
        binding?.tvTitle?.text = post?.title
        binding?.tvBody?.text = post?.body
    }
}
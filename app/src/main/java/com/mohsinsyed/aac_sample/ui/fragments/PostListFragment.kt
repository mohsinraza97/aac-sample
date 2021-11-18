package com.mohsinsyed.aac_sample.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohsinsyed.aac_sample.R
import com.mohsinsyed.aac_sample.data.entities.Post
import com.mohsinsyed.aac_sample.databinding.FragmentPostListBinding
import com.mohsinsyed.aac_sample.ui.adapters.PostAdapter
import com.mohsinsyed.aac_sample.ui.view_models.PostViewModel
import com.mohsinsyed.aac_sample.utils.extensions.addDivider
import com.mohsinsyed.aac_sample.utils.extensions.navigateTo
import com.mohsinsyed.aac_sample.utils.extensions.setToolBarTitle
import com.mohsinsyed.aac_sample.utils.extensions.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostListFragment : Fragment() {

    private var binding: FragmentPostListBinding? = null
    private val postViewModel by activityViewModels<PostViewModel>()
    private val postAdapter by lazy {
        PostAdapter(
            requireContext(),
            ::onPostClicked,
            onEditClicked = ::onEditClicked,
            onDeleteClicked = ::onDeleteClicked
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_list, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setUpObservers()
        loadData()
    }

    private fun init() {
        setToolBarTitle(getString(R.string.fragment_title_post_list))
        binding?.rvPosts?.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(context)
            addDivider()
        }
        binding?.fabAdd?.setOnClickListener {
            binding?.root?.showSnackBar(getString(R.string.not_implemented_yet))
        }
    }

    private fun setUpObservers() {
        postViewModel.posts.observe(viewLifecycleOwner, { postAdapter.submitList(it) })
    }

    private fun loadData() {
        postViewModel.fetchAll()
    }

    private fun onPostClicked(post: Post?) {
        bundleOf("post" to post).let {
            navigateTo(R.id.action_post_list_to_post_detail, it)
        }
    }

    private fun onEditClicked(post: Post?) {
        binding?.root?.showSnackBar(getString(R.string.not_implemented_yet))
    }

    private fun onDeleteClicked(post: Post?) {
        binding?.root?.showSnackBar(getString(R.string.not_implemented_yet))
    }
}
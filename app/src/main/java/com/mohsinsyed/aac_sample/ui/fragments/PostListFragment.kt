package com.mohsinsyed.aac_sample.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohsinsyed.aac_sample.R
import com.mohsinsyed.aac_sample.data.models.Post
import com.mohsinsyed.aac_sample.databinding.FragmentPostListBinding
import com.mohsinsyed.aac_sample.ui.adapters.PostAdapter
import com.mohsinsyed.aac_sample.ui.view_models.PostViewModel
import com.mohsinsyed.aac_sample.utils.constants.AppConstants
import com.mohsinsyed.aac_sample.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostListFragment : Fragment() {

    private var binding: FragmentPostListBinding? = null
    private val postViewModel by activityViewModels<PostViewModel>()
    private val postAdapter by lazy {
        PostAdapter(
            requireContext(),
            postViewModel.posts.value as ArrayList<Post>?,
            onPostClicked = ::onPostClicked,
            onEditClicked = ::onEditClicked,
            onDeleteClicked = ::onDeleteClicked
        )
    }
    private var deletedPostPosition = RecyclerView.NO_POSITION

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
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
        binding?.viewModel = postViewModel
        binding?.rvPosts?.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(context)
            addDivider()
        }
        binding?.fabAdd?.setOnClickListener {
            navigateTo(R.id.action_destination_post_list_to_post_editor)
        }
        binding?.swipeRefresh?.apply {
            setColorSchemeColors(requireContext().getThemeColor(R.attr.colorPrimary))
            setOnRefreshListener { loadData(showLoading = false) }
        }
    }

    private fun setUpObservers() {
        postViewModel.posts.observe(viewLifecycleOwner, {
            if (binding?.swipeRefresh?.isRefreshing == true) {
                binding?.swipeRefresh?.isRefreshing = false
            }
            postAdapter.updateList(it as? ArrayList<Post>?)
        })
        postViewModel.events.observe(viewLifecycleOwner, { event ->
            when (event) {
                is PostViewModel.PostEvents.Deleted -> {
                    postAdapter.removeItem(deletedPostPosition) {
                        if (it) {
                            binding?.root?.showSnackBar(getString(R.string.post_action_message, "deleted"))
                        }
                    }
                }
            }
        })
    }

    private fun loadData(showLoading: Boolean = true) {
        postViewModel.fetchAll(showLoading)
    }

    private fun onPostClicked(post: Post?) {
        bundleOf(AppConstants.EXTRA_KEY_POST to post).let {
            navigateTo(R.id.action_post_list_to_post_detail, it)
        }
    }

    private fun onEditClicked(post: Post?) {
        bundleOf(AppConstants.EXTRA_KEY_POST to post).let {
            navigateTo(R.id.action_destination_post_list_to_post_editor, it)
        }
    }

    private fun onDeleteClicked(position: Int) {
        deletedPostPosition = position
        val id = postViewModel.posts.value?.get(position)?.id
        postViewModel.delete(id)
    }
}
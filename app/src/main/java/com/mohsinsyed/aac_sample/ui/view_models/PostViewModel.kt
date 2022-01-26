package com.mohsinsyed.aac_sample.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mohsinsyed.aac_sample.data.models.entities.Post
import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.data.repository.outbox.IOutboxRepository
import com.mohsinsyed.aac_sample.data.repository.outbox.OutboxRepository
import com.mohsinsyed.aac_sample.data.repository.post.IPostRepository
import com.mohsinsyed.aac_sample.ui.helpers.SingleLiveEvent
import com.mohsinsyed.aac_sample.utils.constants.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: IPostRepository,
    private val outboxRepository: IOutboxRepository? = null,
) : BaseViewModel() {

    private val _posts = MutableLiveData<List<Post>?>()
    val posts: LiveData<List<Post>?> get() = _posts

    private val _events = SingleLiveEvent<PostEvents>()
    val events: LiveData<PostEvents> get() = _events

    fun create(post: Post?) {
        setUIEvent(UIEvents.HideKeyboard)
        viewModelScope.launch {
            toggleLoading(true)
            when (val response = postRepository.create(post)) {
                is Response.Success -> {
                    _events.value = PostEvents.Added(response.data)
                    outboxRepository?.add(post, AppConstants.SyncConstants.SYNC_TAG_CREATE_POST)
                }
                is Response.Error -> setUIEvent(UIEvents.ShowMessage(response.message))
            }
            toggleLoading(false)
        }
    }

    fun update(post: Post?) {
        setUIEvent(UIEvents.HideKeyboard)
        viewModelScope.launch {
            toggleLoading(true)
            when (val response = postRepository.update(post)) {
                is Response.Success -> {
                    _events.value = PostEvents.Updated(response.data)
                    outboxRepository?.add(post, AppConstants.SyncConstants.SYNC_TAG_UPDATE_POST)
                }
                is Response.Error -> setUIEvent(UIEvents.ShowMessage(response.message))
            }
            toggleLoading(false)
        }
    }

    fun delete(id: Long?) {
        viewModelScope.launch {
            toggleLoading(true)
            when (val response = postRepository.delete(id)) {
                is Response.Success -> {
                    _events.value = PostEvents.Deleted
                    outboxRepository?.add(id, AppConstants.SyncConstants.SYNC_TAG_DELETE_POST)
                }
                is Response.Error -> setUIEvent(UIEvents.ShowMessage(response.message))
            }
            toggleLoading(false)
        }
    }

    fun fetchAll(showLoading: Boolean = true) {
        viewModelScope.launch {
            if (showLoading) {
                toggleLoading(true)
            }
            when (val response = postRepository.fetchAll()) {
                is Response.Success -> _posts.value = response.data
                is Response.Error -> setUIEvent(UIEvents.ShowMessage(response.message))
            }
            toggleLoading(false)
        }
    }

    sealed class PostEvents {
        data class Added(val post: Post?) : PostEvents()
        data class Updated(val post: Post?) : PostEvents()
        object Deleted : PostEvents()
    }
}
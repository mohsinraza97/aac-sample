package com.mohsinsyed.aac_sample.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mohsinsyed.aac_sample.data.models.entities.Post
import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.data.repository.PostRepository
import com.mohsinsyed.aac_sample.ui.helpers.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
) : BaseViewModel() {

    private val _posts = MutableLiveData<List<Post>?>()
    val posts: LiveData<List<Post>?> get() = _posts

    private val _events =
        SingleLiveEvent<PostEvents>()
    val events: LiveData<PostEvents> get() = _events

    fun create(post: Post?) {
        setUIEvent(UIEvents.HideKeyboard)
        viewModelScope.launch {
            toggleLoading(true)
            when (val response = repository.create(post)) {
                is Response.Success -> _events.value = PostEvents.Added(response.value)
                is Response.Error -> setUIEvent(UIEvents.Message(response.message))
                is Response.Failed -> setUIEvent(UIEvents.Message(response.message))
            }
            toggleLoading(false)
        }
    }

    fun update(post: Post?) {
        setUIEvent(UIEvents.HideKeyboard)
        viewModelScope.launch {
            toggleLoading(true)
            when (val response = repository.update(post)) {
                is Response.Success -> _events.value = PostEvents.Updated(response.value)
                is Response.Error -> setUIEvent(UIEvents.Message(response.message))
                is Response.Failed -> setUIEvent(UIEvents.Message(response.message))
            }
            toggleLoading(false)
        }
    }

    fun delete(id: Long?) {
        viewModelScope.launch {
            toggleLoading(true)
            when (val response = repository.delete(id)) {
                is Response.Success -> _events.value = PostEvents.Deleted
                is Response.Error -> setUIEvent(UIEvents.Message(response.message))
                is Response.Failed -> setUIEvent(UIEvents.Message(response.message))
            }
            toggleLoading(false)
        }
    }

    fun fetchAll(showLoading: Boolean = true) {
        viewModelScope.launch {
            if (showLoading) {
                toggleLoading(true)
            }
            when (val response = repository.fetchAll()) {
                is Response.Success -> _posts.value = response.value
                is Response.Error -> setUIEvent(UIEvents.Message(response.message))
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
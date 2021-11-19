package com.mohsinsyed.aac_sample.ui.view_models

import android.provider.CalendarContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mohsinsyed.aac_sample.data.entities.Post
import com.mohsinsyed.aac_sample.data.remote.Response
import com.mohsinsyed.aac_sample.data.repository.PostRepository
import com.mohsinsyed.aac_sample.utils.utilities.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
) : BaseViewModel() {

    private val _posts = MutableLiveData<List<Post>?>()
    val posts: LiveData<List<Post>?> get() = _posts

    private val _events = SingleLiveEvent<PostEvents>()
    val events: LiveData<PostEvents> get() = _events

    fun create(post: Post?) {
        setUIEvent(UIEvents.HideKeyboard)
        viewModelScope.launch {
            toggleLoading(true)
            when (val response = repository.create(post)) {
                is Response.Success -> _events.value = PostEvents.Added(response.value)
                is Response.Failed -> setUIEvent(UIEvents.Message(response.message))
            }
            toggleLoading(false)
        }
    }

    fun update(post: Post?) {
        setUIEvent(UIEvents.HideKeyboard)
        viewModelScope.launch {
            toggleLoading(true)
            when (val response = repository.update(post, post?.id)) {
                is Response.Success -> _events.value = PostEvents.Edited(response.value)
                is Response.Failed -> setUIEvent(UIEvents.Message(response.message))
            }
            toggleLoading(false)
        }
    }

    fun delete(id: Int?) {
        viewModelScope.launch {
            toggleLoading(true)
            when (val response = repository.delete(id)) {
                is Response.Success -> _events.value = PostEvents.Deleted
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
                is Response.Failed -> setUIEvent(UIEvents.Message(response.message))
            }
            toggleLoading(false)
        }
    }

    sealed class PostEvents {
        data class Added(val post: Post?) : PostEvents()
        data class Edited(val post: Post?) : PostEvents()
        object Deleted : PostEvents()
    }
}
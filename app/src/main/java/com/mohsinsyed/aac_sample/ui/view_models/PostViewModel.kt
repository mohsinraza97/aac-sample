package com.mohsinsyed.aac_sample.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohsinsyed.aac_sample.data.entities.Post
import com.mohsinsyed.aac_sample.data.remote.Response
import com.mohsinsyed.aac_sample.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository
) : BaseViewModel() {

    private val _posts = MutableLiveData<List<Post>?>()
    val posts: LiveData<List<Post>?> get() = _posts

    fun fetchAll() {
        viewModelScope.launch {
            toggleLoading(true)
            when (val response = repository.fetchAll()) {
                is Response.Success -> _posts.value = response.value
                is Response.Failed -> setUiError(response.message)
            }
            toggleLoading(false)
        }
    }
}
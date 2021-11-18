package com.mohsinsyed.aac_sample.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohsinsyed.aac_sample.data.entities.Post
import com.mohsinsyed.aac_sample.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

open class BaseViewModel (
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _uiError = MutableLiveData<String?>()
    val uiError: LiveData<String?> get() = _uiError

    protected fun toggleLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
        // Reset error message when loader starts
        if (isLoading) {
            _uiError.value = null
        }
    }

    protected fun setUiError(message: String?) {
        _uiError.value = message
    }
}
package com.mohsinsyed.aac_sample.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohsinsyed.aac_sample.ui.helpers.SingleLiveEvent

open class BaseViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _uiEvents =
        SingleLiveEvent<UIEvents>()
    val uiEvents: LiveData<UIEvents> get() = _uiEvents

    protected fun toggleLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    protected fun setUIEvent(event: UIEvents) {
        _uiEvents.value = event
    }

    sealed class UIEvents {
        data class ShowMessage(val message: String?) : UIEvents()
        object HideKeyboard : UIEvents()
    }
}
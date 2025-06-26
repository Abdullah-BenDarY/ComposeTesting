package com.example.mycompose.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycompose.ui.utils.networkHandling.ConnectionError
import com.example.mycompose.ui.utils.networkHandling.ServerError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


open class BaseViewModel : ViewModel() {
    private val _uiMessage = MutableSharedFlow<String>()
    val uiMessage = _uiMessage.asSharedFlow()


    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    fun handleError(message: String) {
        viewModelScope.launch {
            _uiMessage.emit(message)
        }

    }

    fun handleError(throwable: Throwable) {
        val errorMessage = when (throwable) {
            is ConnectionError -> throwable.message ?: "Connection error"
            is ServerError -> throwable.message ?: "Server error"
            else -> throwable.message ?: "Unknown error"
        }
        viewModelScope.launch {
            _uiMessage.emit(errorMessage)
        }    }


    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }


}

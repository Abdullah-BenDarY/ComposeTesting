package com.example.mycompose.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    // Using mutableStateOf for Compose UI updates
    var state2 by mutableStateOf(CounterState())
        private set

    private val state = MutableStateFlow(CounterState())
    val count = state.asStateFlow()
    private val _toastEvent = MutableSharedFlow<CounterState>()
    val toastEvent = _toastEvent.asSharedFlow()

    fun increment() {
        if (state.value.count == 10) {
            viewModelScope.launch {
                _toastEvent.emit(CounterState(toastMessage = "Maximum"))
            }
        } else {
            state.update { it.copy(count = it.count + 1) }

            // Alternatively, if you want to use state2: ( for compose )
            state2 = state2.copy(count = state2.count + 1)
        }
    }

    fun decrement() {
        if (state.value.count == 0) {
            viewModelScope.launch {
                _toastEvent.emit(CounterState(toastMessage = "Minimum"))
            }
        } else {
            state.update { it.copy(count = it.count - 1) }
        }
    }


    fun reset() {
        if (state.value.count > 0) {
            viewModelScope.launch {
                _toastEvent.emit(CounterState(toastMessage = "Reseted"))
            }
            state.update { it.copy(count = 0) }
        } else {
            viewModelScope.launch {
                _toastEvent.emit(CounterState(toastMessage = "Already 0"))
            }
        }
    }
}

data class CounterState(
    val count: Int = 0,
    val toastMessage: String? = null,
    val imageURL: String? = "https://picsum.photos/id/237/200/300"
)
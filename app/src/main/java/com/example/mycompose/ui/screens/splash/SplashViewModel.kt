package com.example.mycompose.ui.screens.splash

import androidx.lifecycle.viewModelScope
import com.example.mycompose.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel() {

    private val _images = MutableStateFlow(ModelImages(strings = emptyList(), images = emptyList()))
    val images = _images.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            setLoading(true)
            try {
                delay(3000) // Simulating a long loading time
                val images = ModelImages(
                    images=
                    listOf(
                        "https://picsum.photos/id/237/200/300",
                        "https://picsum.photos/seed/picsum/200/300",
                        "https://picsum.photos/200/300?grayscale",
                        "https://picsum.photos/200/300/?blur",
                        "https://picsum.photos/200/300/?blur=2",
                        "https://picsum.photos/id/870/200/300?grayscale&blur=2",
                        "https://picsum.photos/200/300",
                        "https://picsum.photos/200/300/?blur=3"
                    ),
                    strings =
                        listOf(
                            "Hello",            // English
                            "Bonjour",          // French
                            "Hola",             // Spanish
                            "Hallo",            // German
                            "Ciao",             // Italian
                            "Olá",              // Portuguese
                            "Привет",           // Russian
                            "こんにちは",         // Japanese
                            "你好",              // Chinese (Simplified)
                            "안녕하세요",          // Korean
                            "नमस्ते",            // Hindi
                            "สวัสดี",           // Thai
                            "Selam",            // Turkish
                            "Hej",              // Swedish
                            "Hei",              // Norwegian / Finnish
                            "Halló",            // Icelandic
                            "Aloha",            // Hawaiian
                            "Yassou",           // Greek (casual)
                            "Salam",            // Persian
                            "Merhaba",          // Turkish (alternative)
                            "Szia",             // Hungarian
                            "Halo",             // Indonesian
                            "Sawubona",         // Zulu
                            "Habari",           // Swahili
                            "Dzień dobry",      // Polish
                            "Sveiki",           // Latvian
                            "Tere",             // Estonian
                            "God dag"           // Danish
                        )
                )
                _images.value = images
                setLoading(false)


            } catch (e: Exception) {
                handleError(e)

            }
        }
    }
}

data class ModelImages(
    val strings: List<String>,
    val images: List<String>
)

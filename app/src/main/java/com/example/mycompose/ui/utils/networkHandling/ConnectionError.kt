package com.example.mycompose.ui.utils.networkHandling

class ConnectionError(message: String = "No internet connection please check your internet") :
    Throwable(message) {
}

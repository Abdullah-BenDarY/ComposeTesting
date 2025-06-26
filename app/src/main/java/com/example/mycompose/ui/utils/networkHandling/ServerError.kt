package com.example.mycompose.ui.utils.networkHandling


class ServerError (
    serverMessage:String? = "Something went wrong") : Throwable(serverMessage)
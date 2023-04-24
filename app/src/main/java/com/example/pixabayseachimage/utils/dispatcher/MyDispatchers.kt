package com.example.pixabayseachimage.utils.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

interface MyDispatchers {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val computation: CoroutineDispatcher
}
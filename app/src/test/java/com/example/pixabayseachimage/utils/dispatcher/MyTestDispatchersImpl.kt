package com.example.pixabayseachimage.utils.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

internal class MyTestDispatchersImpl: MyDispatchers{
    override val io: CoroutineDispatcher = TestCoroutineDispatcher()
    override val main: CoroutineDispatcher = TestCoroutineDispatcher()
    override val computation: CoroutineDispatcher = TestCoroutineDispatcher()
}
package com.jerry.companies.util

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

interface CoroutineContextProvider {
    val main: CoroutineContext
    val uiImmediate: CoroutineContext
    val io: CoroutineContext
    val commonPool: CoroutineContext

    object MainCoroutineContext : CoroutineContextProvider {
        override val main = Dispatchers.Main
        override val uiImmediate = Dispatchers.Main.immediate
        override val io = Dispatchers.IO
        override val commonPool = Dispatchers.Default
    }
}
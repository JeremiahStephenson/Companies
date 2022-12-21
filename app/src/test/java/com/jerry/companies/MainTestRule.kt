package com.jerry.companies

import androidx.annotation.NonNull
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.jerry.companies.util.CoroutineContextProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MainTestRule : TestWatcher() {

    val dispatcher = UnconfinedTestDispatcher()
    val contextProvider = object : CoroutineContextProvider {
        override val main = dispatcher
        override val uiImmediate = dispatcher
        override val io = dispatcher
        override val commonPool = dispatcher
    }

    override fun starting(description: Description) {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(@NonNull runnable: Runnable) = runnable.run()
            override fun postToMainThread(@NonNull runnable: Runnable) = runnable.run()
            override fun isMainThread() = true
        })
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        ArchTaskExecutor.getInstance().setDelegate(null)
        Dispatchers.resetMain()
    }
}
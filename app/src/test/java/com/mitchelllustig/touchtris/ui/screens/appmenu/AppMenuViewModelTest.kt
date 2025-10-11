package com.mitchelllustig.touchtris.ui.screens.appmenu

import com.mitchelllustig.touchtris.settings.AppSettings
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppMenuViewModelTest {

    private lateinit var appSettings: AppSettings
    private lateinit var viewModel: AppMenuViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        appSettings = mockk(relaxed = true)
        viewModel = AppMenuViewModel(appSettings)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onCleared saves app settings`() {
        // Since onCleared is protected, we invoke it with reflection for this test.
        val onClearedMethod = viewModel.javaClass.getDeclaredMethod("onCleared")
        onClearedMethod.isAccessible = true
        onClearedMethod.invoke(viewModel)

        // Assert that the save method was called on our mock
        verify { appSettings.saveSettings() }
    }
}

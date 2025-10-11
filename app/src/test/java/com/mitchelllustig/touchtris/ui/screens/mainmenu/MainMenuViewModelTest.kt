package com.mitchelllustig.touchtris.ui.screens.mainmenu

import com.mitchelllustig.touchtris.settings.AppSettings
import com.mitchelllustig.touchtris.settings.ContinueSettings
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainMenuViewModelTest {

    private lateinit var appSettings: AppSettings
    private lateinit var continueSettings: ContinueSettings
    private lateinit var viewModel: MainMenuViewModel

    @Before
    fun setUp() {
        appSettings = mockk(relaxed = true)
        continueSettings = mockk(relaxed = true)
        viewModel = MainMenuViewModel(appSettings, continueSettings)
    }

    @Test
    fun `viewModel holds correct continueSettings instance`() {
        // Assert
        assertEquals(continueSettings, viewModel.continueSettings)
    }
}

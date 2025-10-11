package com.mitchelllustig.touchtris.ui.screens.mainmenu

import com.mitchelllustig.touchtris.settings.AppSettings
import com.mitchelllustig.touchtris.settings.ContinueSettings
import com.mitchelllustig.touchtris.ui.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    appSettings: AppSettings,
    val continueSettings: ContinueSettings
): BaseViewModel(appSettings) {

}
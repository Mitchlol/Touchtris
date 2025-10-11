package com.mitchelllustig.touchtris.ui.screens.appmenu

import com.mitchelllustig.touchtris.settings.AppSettings
import com.mitchelllustig.touchtris.ui.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppMenuViewModel @Inject constructor(
    appSettings: AppSettings,
): BaseViewModel(appSettings) {

    override fun onCleared() {
        super.onCleared()
        saveAppSettings()
    }
}
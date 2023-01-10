package software.ehsan.newsfeed.ui.common

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import software.ehsan.newsfeed.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val context: Application) : BaseViewModel()
package software.ehsan.newsfeed.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import software.ehsan.newsfeed.data.model.Resource
import software.ehsan.newsfeed.data.model.User
import software.ehsan.newsfeed.domain.profile.GetCurrentUserUseCase
import software.ehsan.newsfeed.domain.profile.LogOutUseCase
import software.ehsan.newsfeed.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logOutUseCase: LogOutUseCase
) : BaseViewModel() {

    private val _userLiveData = MutableLiveData<Resource<User>>()
    val userLiveData: LiveData<Resource<User>> = _userLiveData

    fun checkLoggedInUser() {
        _userLiveData.value = Resource.success(getCurrentUserUseCase())
    }

    fun logOut() {
        logOutUseCase()
        checkLoggedInUser()
    }
}
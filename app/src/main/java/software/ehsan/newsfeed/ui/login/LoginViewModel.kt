package software.ehsan.newsfeed.ui.login

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import software.ehsan.newsfeed.data.model.Resource
import software.ehsan.newsfeed.domain.profile.LoginByEmailPasswordUseCase
import software.ehsan.newsfeed.domain.profile.LoginByGoogleUseCase
import software.ehsan.newsfeed.ui.base.BaseViewModel
import software.ehsan.newsfeed.util.Event
import software.ehsan.newsfeed.util.Validator
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val loginByEmailPasswordUseCase: LoginByEmailPasswordUseCase,
    val loginByGoogleUseCase: LoginByGoogleUseCase
) : BaseViewModel() {

    private val _loginLiveData = MutableLiveData<Event<LoginEvent>>()
    val loginLiveData: LiveData<Event<LoginEvent>> = _loginLiveData

    private val _emailValidationLiveData = MutableLiveData<Event<Resource<Boolean>>>()
    val emailValidationLiveData: LiveData<Event<Resource<Boolean>>> = _emailValidationLiveData

    private val _loginValidationLiveData = MutableLiveData<Event<Resource<Boolean>>>()
    val loginValidationLiveData: LiveData<Event<Resource<Boolean>>> = _loginValidationLiveData

    fun loginByEmailPassword(email: String, password: String) {
        _loginLiveData.value = Event(LoginEvent.Loading)
        viewModelScope.launch {
            loginByEmailPasswordUseCase(email = email, password = password).collect {
                if (it.isSuccessful) {
                    Logger.d(TAG, "Login by Email is Successful")
                    _loginLiveData.value = Event(LoginEvent.LoginSuccessfully)
                } else {
                    Logger.d(TAG, "Login by Email is Failed")
                    Logger.d(TAG, it.exception!!.toString())
                    _loginLiveData.value = Event(LoginEvent.LoginFailed(it.exception))
                }
            }
        }
    }

    fun validateEmail(email: String) {
        _emailValidationLiveData.value = Event(Resource.success(Validator.isValidEmail(email)))
    }

    fun checkLoginValidation(email: String, password: String) {
        _loginValidationLiveData.value = Event(
            Resource.success(
                Validator.isValidEmail(email) && Validator.isValidPassword(password)
            )
        )
    }

    fun initLoginByGoogle(activityResultLauncher: ActivityResultLauncher<Intent>) {
        val intent = loginByGoogleUseCase()
        activityResultLauncher.launch(intent)
    }

    fun loginByGoogle(activityResult: ActivityResult) {
        viewModelScope.launch {
            loginByGoogleUseCase(activityResult = activityResult).catch {
                _loginLiveData.value = Event(LoginEvent.LoginFailed(Exception(it)))
            }.collect {
                if (it.isSuccessful) {
                    val user = it.result.user
                    Logger.d(TAG, user?.toString())
                    _loginLiveData.value = Event(LoginEvent.LoginSuccessfully)
                } else {
                    Logger.d(TAG, "Login by Google Failed")
                    _loginLiveData.value = Event(LoginEvent.LoginFailed(it.exception))
                }
            }
        }

    }
}

sealed class LoginEvent {
    object Loading : LoginEvent()
    object LoginSuccessfully : LoginEvent()
    data class LoginFailed(val exception: java.lang.Exception?) : LoginEvent()
}
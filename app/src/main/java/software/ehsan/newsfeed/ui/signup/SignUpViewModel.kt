package software.ehsan.newsfeed.ui.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import software.ehsan.newsfeed.data.model.Resource
import software.ehsan.newsfeed.domain.profile.SignUpWithEmailPasswordCase
import software.ehsan.newsfeed.ui.base.BaseViewModel
import software.ehsan.newsfeed.util.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(val signUpUseWithEmailPasswordCase: SignUpWithEmailPasswordCase) :
    BaseViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var emailIsWrong by mutableStateOf(true)
    var passwordIsShort by mutableStateOf(true)

    private val _signUpWithEmailPasswordLiveData = MutableLiveData<Resource<Boolean>>()
    val signUpWithEmailPasswordLiveData: LiveData<Resource<Boolean>> =
        _signUpWithEmailPasswordLiveData

    fun validateEmail() {
        emailIsWrong = !Validator.isValidEmail(email)
    }

    fun validatePassword() {
        passwordIsShort = password.length < 6
    }

    fun signUp() {
        _signUpWithEmailPasswordLiveData.value = Resource.loading()
        viewModelScope.launch {
            signUpUseWithEmailPasswordCase(email = email, password = password).catch {
                _signUpWithEmailPasswordLiveData.value = Resource.error(Exception(it))
            }.collect {
                if (it.isSuccessful) {
                    val user = it.result.user
                    Log.d(TAG, user.toString())
                    _signUpWithEmailPasswordLiveData.value = Resource.success(true)
                } else {
                    Log.d(TAG, "FALSE")
                    _signUpWithEmailPasswordLiveData.value =
                        Resource.error(it.exception)
                }
            }
        }
    }
}
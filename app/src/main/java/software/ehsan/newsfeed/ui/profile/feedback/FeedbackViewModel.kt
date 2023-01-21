package software.ehsan.newsfeed.ui.profile.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import software.ehsan.newsfeed.data.model.Feedback
import software.ehsan.newsfeed.data.model.Resource
import software.ehsan.newsfeed.data.model.UserAppProperties
import software.ehsan.newsfeed.domain.profile.GetUserAppPropertiesUseCase
import software.ehsan.newsfeed.domain.profile.SendFeedbackUseCase
import software.ehsan.newsfeed.ui.base.BaseViewModel
import software.ehsan.newsfeed.util.Event
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val getUserAppPropertiesUseCase: GetUserAppPropertiesUseCase,
    private val sendFeedbackUseCase: SendFeedbackUseCase
) : BaseViewModel() {

    private val _userAppPropertiesLiveData = MutableLiveData<Resource<UserAppProperties>>()
    val userAppPropertiesLiveData: LiveData<Resource<UserAppProperties>> =
        _userAppPropertiesLiveData

    private val _feedbackLiveData = MutableLiveData<Event<Resource<Void>>>()
    val feedbackLiveData: LiveData<Event<Resource<Void>>> = _feedbackLiveData


    fun getUserAppProperties() {
        _userAppPropertiesLiveData.value = Resource.success(getUserAppPropertiesUseCase())
    }

    fun sendFeedback(email: String?, feedback: String) {
        sendFeedbackUseCase(
            feedback = Feedback(
                email = email,
                feedback = feedback,
                userAppProperties = getUserAppPropertiesUseCase()
            )
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                _feedbackLiveData.value = Event(Resource.success(null))
            } else {
                _feedbackLiveData.value = Event(Resource.error(it.exception))
            }
        }
    }

}
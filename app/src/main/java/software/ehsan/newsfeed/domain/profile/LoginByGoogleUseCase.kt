package software.ehsan.newsfeed.domain.profile

import android.content.Intent
import androidx.activity.result.ActivityResult
import software.ehsan.newsfeed.data.repository.UserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginByGoogleUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(activityResult: ActivityResult): Flow<Task<AuthResult>> {
        return userRepository.loginByGoogle(activityResult)
    }

    operator fun invoke(): Intent {
        return userRepository.initializeLoginByGoogle()
    }
}
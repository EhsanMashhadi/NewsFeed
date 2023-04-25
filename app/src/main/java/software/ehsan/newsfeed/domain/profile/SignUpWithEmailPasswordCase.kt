package software.ehsan.newsfeed.domain.profile

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import software.ehsan.newsfeed.data.repository.UserRepository
import javax.inject.Inject

class SignUpWithEmailPasswordCase @Inject constructor(
    private val userRepository: UserRepository,
    private val sendVerificationEmailUseCase: SendVerificationEmailUseCase
) {

    suspend operator fun invoke(email: String, password: String): Flow<Task<AuthResult>> {
        return userRepository.signUpWithEmailPassword(email = email, password = password)
            .map { task ->
                if (task.isSuccessful) {
                    val task = sendVerificationEmailUseCase(task.result.user!!)
                    Logger.d(task)
                }
                task
            }
    }
}
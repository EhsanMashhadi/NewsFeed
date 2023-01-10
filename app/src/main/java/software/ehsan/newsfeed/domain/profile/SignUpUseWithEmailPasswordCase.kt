package software.ehsan.newsfeed.domain.profile

import software.ehsan.newsfeed.data.repository.UserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseWithEmailPasswordCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(email: String, password: String): Flow<Task<AuthResult>> {
        return userRepository.signUpWithEmailPassword(email = email, password = password)
    }
}
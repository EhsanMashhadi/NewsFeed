package software.ehsan.newsfeed.domain.profile

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import software.ehsan.newsfeed.data.repository.UserRepository
import javax.inject.Inject

class SendVerificationEmailUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(firebaseUser: FirebaseUser): Task<Void> {
        return userRepository.sendEmailVerification(firebaseUser = firebaseUser)
    }
}
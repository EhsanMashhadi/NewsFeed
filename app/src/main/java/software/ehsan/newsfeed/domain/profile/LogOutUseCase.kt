package software.ehsan.newsfeed.domain.profile

import software.ehsan.newsfeed.data.repository.UserRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(private val userRepository: UserRepository) {


    operator fun invoke() {
        userRepository.logOut()
    }
}
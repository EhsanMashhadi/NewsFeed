package software.ehsan.newsfeed.domain.profile

import software.ehsan.newsfeed.data.model.User
import software.ehsan.newsfeed.data.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    operator fun invoke(): User {
        return userRepository.getCurrentUser()
    }
}
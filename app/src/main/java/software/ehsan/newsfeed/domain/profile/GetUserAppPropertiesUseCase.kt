package software.ehsan.newsfeed.domain.profile

import software.ehsan.newsfeed.data.model.UserAppProperties
import javax.inject.Inject

class GetUserAppPropertiesUseCase @Inject constructor(
    private val getOsVersionUseCase: GetOsVersionUseCase,
    private val getAppVersionUseCase: GetAppVersionUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) {

    operator fun invoke(): UserAppProperties {
        return UserAppProperties(
            osVersion = getOsVersionUseCase(),
            appVersion = getAppVersionUseCase(),
            user = getCurrentUserUseCase()
        )
    }
}
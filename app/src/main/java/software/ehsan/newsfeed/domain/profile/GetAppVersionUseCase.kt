package software.ehsan.newsfeed.domain.profile

import software.ehsan.newsfeed.data.repository.DeviceRepository
import javax.inject.Inject

class GetAppVersionUseCase @Inject constructor(private val deviceRepository: DeviceRepository) {

    operator fun invoke(): String? {
        return deviceRepository.getAppVersion()
    }
}
package software.ehsan.newsfeed.domain.ad

import software.ehsan.newsfeed.data.repository.AdRepository
import javax.inject.Inject

class GetAdLastShownTimeUseCase @Inject constructor(private val adRepository: AdRepository) {

    suspend operator fun invoke(): String? {
        return adRepository.getLastAdShownTime()
    }
}
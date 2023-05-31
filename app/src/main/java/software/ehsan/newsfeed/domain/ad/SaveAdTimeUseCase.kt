package software.ehsan.newsfeed.domain.ad

import software.ehsan.newsfeed.data.repository.AdRepository
import javax.inject.Inject

class SaveAdTimeUseCase @Inject constructor(private val adRepository: AdRepository) {

    suspend operator fun invoke(): Boolean {
        return adRepository.insertAdShownTime()
    }
}
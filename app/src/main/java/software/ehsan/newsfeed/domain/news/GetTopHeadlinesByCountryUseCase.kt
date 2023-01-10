package software.ehsan.newsfeed.domain.news

import androidx.paging.PagingData
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopHeadlinesByCountryUseCase @Inject constructor(private val newsRepository: NewsRepository) {

    suspend operator fun invoke(country: String, userId: String): Flow<PagingData<Article>> {
        return newsRepository.getTopHeadlineNewsByCountry(country = country, userId = userId)
    }
}
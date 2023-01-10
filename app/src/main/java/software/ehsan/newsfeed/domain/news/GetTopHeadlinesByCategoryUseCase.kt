package software.ehsan.newsfeed.domain.news

import androidx.paging.PagingData
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.model.News
import software.ehsan.newsfeed.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class GetTopHeadlinesByCategoryUseCase @Inject constructor(private val newsRepository: NewsRepository) {

    suspend operator fun invoke(category:String, userId:String): Flow<PagingData<Article>> {
        return newsRepository.getTopHeadlineNewsByCategory(category = category, userId = userId)
    }
}
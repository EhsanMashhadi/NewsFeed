package software.ehsan.newsfeed.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import software.ehsan.newsfeed.data.model.Article

interface NewsRepository {
    suspend fun getNews(keyword: String, userId: String): Flow<PagingData<Article>>
    suspend fun getTopHeadlineNewsByCategory(
        category: String,
        userId: String
    ): Flow<PagingData<Article>>
    suspend fun getTopHeadlineNewsByCountry(
        country: String,
        userId: String
    ): Flow<PagingData<Article>>

}
package software.ehsan.newsfeed.data.repository

import androidx.paging.*
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.source.database.firebase.FirebaseRealTimeDatabase
import software.ehsan.newsfeed.data.source.remote.paging.TopHeadlinesByCountryPagingSource
import software.ehsan.newsfeed.data.source.remote.RetrofitService
import software.ehsan.newsfeed.data.source.remote.paging.BaseNewsPagingSource.Companion.PAGE_SIZE
import software.ehsan.newsfeed.data.source.remote.paging.SearchedNewsPagingSource
import software.ehsan.newsfeed.data.source.remote.paging.TopHeadlinesByCategoryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val retrofitService: RetrofitService, private val database: FirebaseRealTimeDatabase
) : BaseRepository() {

    suspend fun getNews(keyword: String, userId: String): Flow<PagingData<Article>> {
        val flow = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
            SearchedNewsPagingSource(retrofitService, keyword = keyword)
        }.flow.map {
            it.map {
                val newsWithSavedState = checkSavedArticlesPaging(article = it, userId = userId)
                newsWithSavedState
            }
        }
        return flow
    }

    suspend fun getTopHeadlineNewsByCategory(
        category: String,
        userId: String
    ): Flow<PagingData<Article>> {
        val flow = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
            TopHeadlinesByCategoryPagingSource(retrofitService, category = category)
        }.flow.map {
            it.map {
                val newsWithSavedState = checkSavedArticlesPaging(article = it, userId = userId)
                newsWithSavedState
            }
        }
        return flow
    }

    suspend fun getTopHeadlineNewsByCountry(
        country: String,
        userId: String
    ): Flow<PagingData<Article>> {
        val flow = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
            TopHeadlinesByCountryPagingSource(retrofitService, country)
        }.flow.map {
            it.map {
                val newsWithSavedState = checkSavedArticlesPaging(article = it, userId = userId)
                newsWithSavedState
            }
        }
        return flow
    }

    private suspend fun checkSavedArticlesPaging(article: Article, userId: String): Article {
        if(userId.isNotEmpty()){
            val savedArticle = database.getSavedArticle(userId = userId, articleId = article.id)
            savedArticle?.let {
                article.isSaved = true
            }
        }
        return article
    }
}
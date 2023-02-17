package software.ehsan.newsfeed.data.repository

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import software.ehsan.newsfeed.data.model.Article

interface ArticleRepository {
    fun insertArticle(article: Article, userId: String): Task<Void>
    fun removeArticle(article: Article, userId: String): Task<Void>
    suspend fun getSavedArticles(userId: String): Flow<List<Article>>
}
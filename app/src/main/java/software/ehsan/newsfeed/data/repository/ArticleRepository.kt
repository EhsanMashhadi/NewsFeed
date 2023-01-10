package software.ehsan.newsfeed.data.repository

import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.source.database.firebase.FirebaseRealTimeDatabase
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArticleRepository @Inject constructor(private val database: FirebaseRealTimeDatabase) :
    BaseRepository() {

    fun insertArticle(article: Article, userId: String): Task<Void> {
        return database.insertArticle(article = article, userId = userId)
    }

    fun removeArticle(article: Article, userId: String): Task<Void> {
        return database.removeArticle(article = article, userId = userId)
    }

    suspend fun getSavedArticles(userId: String): Flow<List<Article>> {
        return database.getSavedArticles(userId)
    }
}
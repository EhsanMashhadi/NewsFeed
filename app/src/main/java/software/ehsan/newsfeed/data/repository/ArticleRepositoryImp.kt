package software.ehsan.newsfeed.data.repository

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.source.database.firebase.FirebaseRealTimeDatabase
import javax.inject.Inject

class ArticleRepositoryImp @Inject constructor(private val database: FirebaseRealTimeDatabase) :
    ArticleRepository, BaseRepository() {

    override fun insertArticle(article: Article, userId: String): Task<Void> {
        return database.insertArticle(article = article, userId = userId)
    }

    override fun removeArticle(article: Article, userId: String): Task<Void> {
        return database.removeArticle(article = article, userId = userId)
    }

    override suspend fun getSavedArticles(userId: String): Flow<List<Article>> {
        return database.getSavedArticles(userId)
    }
}
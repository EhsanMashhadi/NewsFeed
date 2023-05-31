package software.ehsan.newsfeed.repository

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.repository.ArticleRepositoryImp
import software.ehsan.newsfeed.data.source.database.firebase.FirebaseRealTimeDatabase

@RunWith(MockitoJUnitRunner::class)
class ArticleRepositoryImpTest {

    @Mock
    private lateinit var database: FirebaseRealTimeDatabase

    @Mock
    private lateinit var task: Task<Void>

    @Mock
    private lateinit var mockArticle: Article

    @Test
    fun testGetSavedArticles() = runTest {
        val articleRepositoryImp = ArticleRepositoryImp(database)
        articleRepositoryImp.getSavedArticles(Mockito.anyString())
        Mockito.verify(database).getSavedArticles(Mockito.anyString())
    }

    @Test
    fun testInsertArticles() = runTest {
        val articleRepositoryImp = ArticleRepositoryImp(database)
//        Mockito.`when`(database.insertArticle(any(), any())).thenReturn(task)
        articleRepositoryImp.insertArticle(any<Article>(Article::class.java), any<String>(String::class.java))
        Mockito.verify(database).insertArticle(any(Article::class.java), any(String::class.java))
    }

    @Test
    fun testRemoveArticles() = runTest {
        val articleRepositoryImp = ArticleRepositoryImp(database)
        Mockito.`when`(database.removeArticle(mockArticle,"id")).thenReturn(task)
        articleRepositoryImp.removeArticle(mockArticle, "id")
        Mockito.verify(database).removeArticle(mockArticle, "id")
    }
}

private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)


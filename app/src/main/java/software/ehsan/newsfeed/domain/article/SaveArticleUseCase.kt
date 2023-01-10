package software.ehsan.newsfeed.domain.article

import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.repository.ArticleRepository
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveArticleUseCase @Inject constructor(private val articleRepository: ArticleRepository) {

    operator fun invoke(article: Article, userId:String): Task<Void> {
        return articleRepository.insertArticle(article=article, userId = userId)
    }
}
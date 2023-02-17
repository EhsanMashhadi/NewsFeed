package software.ehsan.newsfeed.domain.article

import com.google.android.gms.tasks.Task
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.repository.ArticleRepository
import javax.inject.Inject

class UnSaveArticleUseCase @Inject constructor(private val articleRepository: ArticleRepository) {

    operator fun invoke(article: Article, userId: String): Task<Void> {
        return articleRepository.removeArticle(article = article, userId = userId)
    }
}
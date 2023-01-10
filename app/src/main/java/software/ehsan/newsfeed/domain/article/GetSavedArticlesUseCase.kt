package software.ehsan.newsfeed.domain.article

import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedArticlesUseCase @Inject constructor(private val articleRepository: ArticleRepository) {

    suspend operator fun invoke(userId:String): Flow<List<Article>> {
        return articleRepository.getSavedArticles(userId = userId)
    }
}
package software.ehsan.newsfeed.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import software.ehsan.newsfeed.data.exception.UnauthorizedAccessException
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.domain.article.SaveArticleUseCase
import software.ehsan.newsfeed.domain.article.UnSaveArticleUseCase
import software.ehsan.newsfeed.domain.profile.GetCurrentUserUseCase
import software.ehsan.newsfeed.ui.base.BaseViewModel
import software.ehsan.newsfeed.util.Event
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

open class BaseArticleViewModel : BaseViewModel() {

    @Inject
    lateinit var saveArticleUseCase: SaveArticleUseCase

    @Inject
    lateinit var unSaveArticleUseCase: UnSaveArticleUseCase

    @Inject
    lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    private val _saveArticleLiveData = MutableLiveData<Event<ArticleEvent>>()
    val saveArticleLiveData: LiveData<Event<ArticleEvent>> = _saveArticleLiveData

    fun saveOrUnSaveArticle(article: Article) {
        if (article.isSaved) {
            unSaveArticle(article = article)
        } else {
            saveArticle(article = article)
        }
    }

    private fun saveArticle(article: Article) {
        if (!getCurrentUserUseCase().isAuthenticated) {
            Logger.d("User is not authenticated!")
            _saveArticleLiveData.value = Event(ArticleEvent.SaveArticleError(UnauthorizedAccessException()))
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val user = getCurrentUserUseCase.invoke()
            saveArticleUseCase(article = article, userId = user.id).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    article.isSaved = true
                    _saveArticleLiveData.value =
                        Event(ArticleEvent.SavedArticleSuccessfully(article))
                    Logger.d("Save article successfully")
                } else {
                    Logger.e("Save article failed %s", task.exception)
                    _saveArticleLiveData.value = Event(ArticleEvent.SaveArticleError(task.exception))
                }
            }
        }
    }

    private fun unSaveArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = getCurrentUserUseCase.invoke()
            unSaveArticleUseCase(
                article = article, userId = user.id
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    article.isSaved = false
                    Logger.d("Unsave article successfully")
                    _saveArticleLiveData.value =
                        Event(ArticleEvent.UnSaveArticleSuccessfully(article))
                } else {
                    Logger.e("Unsave article failed %s", task.exception)
                    _saveArticleLiveData.value = Event(ArticleEvent.UnSaveArticleError(task.exception))
                }
            }
        }
    }
}

sealed class ArticleEvent {
    data class SavedArticleSuccessfully(val article: Article) : ArticleEvent()
    data class UnSaveArticleSuccessfully(val article: Article) : ArticleEvent()
    data class SaveArticleError(val exception: java.lang.Exception?) : ArticleEvent()
    data class UnSaveArticleError(val exception: java.lang.Exception?) : ArticleEvent()

}
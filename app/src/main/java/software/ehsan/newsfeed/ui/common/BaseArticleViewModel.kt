package software.ehsan.newsfeed.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import software.ehsan.newsfeed.data.exception.UnauthorizedAccessException
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.model.Resource
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

    private val _saveArticleLiveData = MutableLiveData<Event<Resource<SaveEvent>>>()
    val saveArticleLiveData: LiveData<Event<Resource<SaveEvent>>> = _saveArticleLiveData

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
            _saveArticleLiveData.value = Event(Resource.error(UnauthorizedAccessException()))
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val user = getCurrentUserUseCase.invoke()
            saveArticleUseCase(article = article, userId = user.id).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    article.isSaved = true
                    _saveArticleLiveData.value =
                        Event(Resource.success(SaveEvent.SavedArticleSuccessfully(article)))
                    Logger.d("Save article successfully")
                } else {
                    Logger.e("Save article failed %s", task.exception)
                    _saveArticleLiveData.value = Event(Resource.error(task.exception))
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
                        Event(Resource.success(SaveEvent.UnSaveArticleSuccessfully(article)))
                } else {
                    Logger.e("Unsave article failed %s", task.exception)
                    _saveArticleLiveData.value = Event(Resource.error())
                }
            }
        }
    }
}

sealed class SaveEvent {
    data class SavedArticleSuccessfully(val article: Article) : SaveEvent()
    data class UnSaveArticleSuccessfully(val article: Article) : SaveEvent()
}
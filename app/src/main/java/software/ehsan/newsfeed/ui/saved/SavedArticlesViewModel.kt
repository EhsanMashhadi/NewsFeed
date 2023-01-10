package software.ehsan.newsfeed.ui.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.model.Resource
import software.ehsan.newsfeed.domain.article.GetSavedArticlesUseCase
import software.ehsan.newsfeed.ui.common.BaseArticleViewModel
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SavedArticlesViewModel @Inject constructor(
    val getSavedArticlesUseCase: GetSavedArticlesUseCase
) : BaseArticleViewModel() {

    private val _savedArticlesLiveData = MutableLiveData<Resource<List<Article>>>()
    val savedArticlesLiveData: LiveData<Resource<List<Article>>> = _savedArticlesLiveData

    fun getSavedArticles() {
        val user = getCurrentUserUseCase()
        viewModelScope.launch(Dispatchers.IO) {
            getSavedArticlesUseCase(userId = user.id).onStart {
                Logger.d("Start fetching saved article")
                withContext(Dispatchers.Main) {
                    _savedArticlesLiveData.value = Resource.loading()
                }
            }.catch {
                Logger.d("Saved article exception")
                withContext(kotlinx.coroutines.Dispatchers.Main) {
                    _savedArticlesLiveData.value =
                        Resource.error(it as Exception)
                }
            }.collect {
                Logger.d("Collecting saved article")
                withContext(Dispatchers.Main) {
                    _savedArticlesLiveData.value = Resource.success(it)
                }
            }
        }
    }
}
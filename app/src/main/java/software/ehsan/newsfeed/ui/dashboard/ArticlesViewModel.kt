package software.ehsan.newsfeed.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.model.Resource
import software.ehsan.newsfeed.domain.news.GetTopHeadlinesByCategoryUseCase
import software.ehsan.newsfeed.domain.news.GetTopHeadlinesByCountryUseCase
import software.ehsan.newsfeed.ui.common.BaseArticleViewModel
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
open class ArticlesViewModel @Inject constructor(
    val getTopHeadlinesByCategoryUseCase: GetTopHeadlinesByCategoryUseCase,
    val getTopHeadlinesByCountryUseCase: GetTopHeadlinesByCountryUseCase
) : BaseArticleViewModel() {

    private val _newsPagingLiveData = MutableLiveData<Resource<PagingData<Article>>>()
    val newsPagingLiveData: LiveData<Resource<PagingData<Article>>> = _newsPagingLiveData

    fun getTopHeadlineNewsByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = getCurrentUserUseCase.invoke()
            getTopHeadlinesByCategoryUseCase(category = category, userId = user.id).cachedIn(
                viewModelScope
            ).catch {
                Logger.e(it.toString())
            }.collect { articles ->
                withContext(Dispatchers.Main) {
                    _newsPagingLiveData.value = Resource.success(articles)
                }
            }
        }
    }

    fun getTopHeadlineNewsByCountry(country: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = getCurrentUserUseCase.invoke()
            getTopHeadlinesByCountryUseCase(country = country, userId = user.id)
                .cachedIn(viewModelScope)
                .catch {
                Logger.e(it.toString())
            }.collect { articles ->
                withContext(Dispatchers.Main) {
                    _newsPagingLiveData.value = Resource.success(articles)
                }
            }
        }
    }
}
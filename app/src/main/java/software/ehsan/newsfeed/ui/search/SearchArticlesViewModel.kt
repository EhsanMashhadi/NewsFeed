package software.ehsan.newsfeed.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.model.Resource
import software.ehsan.newsfeed.domain.news.GetNewsByKeywordUseCase
import software.ehsan.newsfeed.ui.common.BaseArticleViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchArticlesViewModel @Inject constructor(val getNewsByKeywordUseCase: GetNewsByKeywordUseCase) :
    BaseArticleViewModel() {

    private val _newsLiveData = MutableLiveData<Resource<PagingData<Article>>>()
    val newsLiveData: LiveData<Resource<PagingData<Article>>> = _newsLiveData

    fun getNews(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = getCurrentUserUseCase.invoke()
            getNewsByKeywordUseCase(keyword = keyword, userId = user.id).cachedIn(
                viewModelScope
            ).collect { articles ->
                withContext(Dispatchers.Main) {
                    _newsLiveData.value = Resource.success(articles)
                }
            }
        }
    }
}
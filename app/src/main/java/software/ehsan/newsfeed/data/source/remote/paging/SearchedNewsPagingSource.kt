package software.ehsan.newsfeed.data.source.remote.paging

import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.source.remote.RetrofitService
import com.orhanobut.logger.Logger

class SearchedNewsPagingSource(
    private val backend: RetrofitService, private val keyword: String
) : BaseNewsPagingSource() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        try {
            val currentPageNumber = params.key ?: STARTING_PAGE_INDEX
            val response = backend.getNews(keyword = keyword, page = currentPageNumber)
            Logger.d("Loading data current page {}".format(currentPageNumber))
            return loadResult(response, currentPageNumber, params.loadSize)
        } catch (e: Exception) {
            Logger.e(e.toString())
            return LoadResult.Error(e)
        }
    }
}
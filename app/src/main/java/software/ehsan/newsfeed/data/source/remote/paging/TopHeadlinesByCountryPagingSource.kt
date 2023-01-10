package software.ehsan.newsfeed.data.source.remote.paging

import android.util.Log
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.source.remote.RetrofitService

class TopHeadlinesByCountryPagingSource(
    private val backend: RetrofitService, private val country: String
) : BaseNewsPagingSource() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        try {
            val currentPageNumber = params.key ?: STARTING_PAGE_INDEX
            val response =
                backend.getTopHeadlinesByCountry(country = country, page = currentPageNumber)
            Log.d(TAG, "Loading data current page {}".format(currentPageNumber))
            return loadResult(response, currentPageNumber, params.loadSize)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            return LoadResult.Error(e)
        }
    }
}
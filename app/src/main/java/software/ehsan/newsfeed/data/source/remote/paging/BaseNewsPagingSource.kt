package software.ehsan.newsfeed.data.source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.model.News
import retrofit2.Response

abstract class BaseNewsPagingSource : PagingSource<Int, Article>() {

    companion object {
        const val STARTING_PAGE_INDEX = 1
        const val PAGE_SIZE = 10
    }

    protected val TAG = this.javaClass.simpleName

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private fun getNextKey(currentPageNumber: Int, loadSize: Int): Int {
        return currentPageNumber + 1
    }

    protected fun loadResult(
        response: Response<News>, currentPageNumber: Int, loadSize: Int
    ): LoadResult<Int, Article> {
        if (response.isSuccessful && response.body()!!.status == "ok") {
            return LoadResult.Page(
                data = response.body()!!.articles,
                prevKey = null,
                nextKey = getNextKey(currentPageNumber = currentPageNumber, loadSize = loadSize)
            )
        } else {
            return LoadResult.Error(Exception(response.errorBody()!!.string()))
        }
    }
}
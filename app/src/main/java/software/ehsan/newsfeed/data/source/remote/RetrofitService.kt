package software.ehsan.newsfeed.data.source.remote

import software.ehsan.newsfeed.data.model.News
import software.ehsan.newsfeed.data.source.remote.paging.BaseNewsPagingSource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("everything")
    suspend fun getNews(
        @Query("q") keyword: String,
        @Query("language") language: String = "en",
        @Query("pageSize") pageSize: Int = 10,
        @Query("page") page: Int = 1
    ): Response<News>

    @GET("top-headlines")
    suspend fun getTopHeadlinesByCategory(
        @Query("category") category: String,
        @Query("language") language: String = "en",
        @Query("pageSize") pageSize: Int = BaseNewsPagingSource.PAGE_SIZE,
        @Query("page") page: Int = 1
    ): Response<News>

    @GET("top-headlines")
    suspend fun getTopHeadlinesByCountry(
        @Query("country") country: String,
        @Query("pageSize") pageSize: Int = BaseNewsPagingSource.PAGE_SIZE,
        @Query("page") page: Int = 1
    ): Response<News>

}
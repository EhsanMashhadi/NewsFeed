package software.ehsan.newsfeed.data.source.remote.interceptor

import software.ehsan.newsfeed.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = chain.request().url
        val urlBuilder = originalUrl.newBuilder()
            .addQueryParameter("apiKey", BuildConfig.API_KEY)
        val requestBuilder = originalRequest.newBuilder().url(urlBuilder.build())
        return chain.proceed(requestBuilder.build())
    }
}
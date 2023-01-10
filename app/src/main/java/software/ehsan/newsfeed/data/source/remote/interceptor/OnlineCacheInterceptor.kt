package software.ehsan.newsfeed.data.source.remote.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response


class OnlineCacheInterceptor(val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        //Read from the cache for 60 seconds EVEN if there is an internet connection
        val maxAge = 60
        return response.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAge")
            .removeHeader("Pragma")
            .build()
    }
}
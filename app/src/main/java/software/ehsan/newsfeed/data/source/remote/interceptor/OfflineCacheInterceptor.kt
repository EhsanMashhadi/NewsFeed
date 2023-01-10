package software.ehsan.newsfeed.data.source.remote.interceptor

import android.content.Context
import software.ehsan.newsfeed.util.ConnectivityUtil
import okhttp3.Interceptor
import okhttp3.Response

class OfflineCacheInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Read from cache for 30 days when there is no internet connection
        val maxStale = 60 * 60 * 24 * 30
        var request = chain.request()
        if (ConnectivityUtil.hasNetwork(context = context) == false) {
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .removeHeader("Pragma").build()
        }
        return chain.proceed(request)
    }
}
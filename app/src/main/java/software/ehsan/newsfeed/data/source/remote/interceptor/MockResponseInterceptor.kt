package software.ehsan.newsfeed.data.source.remote.interceptor

import android.content.Context
import software.ehsan.newsfeed.BuildConfig
import software.ehsan.newsfeed.R
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONArray
import org.json.JSONObject

class MockResponseInterceptor(val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (request.url.toString().contains(BuildConfig.API_BASE_URL)) {
            if (!response.isSuccessful && response.code == 429) {
                val coroutineScope = CoroutineScope(Dispatchers.IO)
                val deferred = coroutineScope.async {
                    val mockedResponseBody = getPagedMockedResponse(
                        request.url.queryParameter("page")!!.toInt(),
                        request.url.queryParameter("pageSize")!!.toInt()
                    )
                    mockedResponseBody
                }
                runBlocking {
                    deferred.await()
                }.let {
                    coroutineScope.cancel()
                    return Response.Builder().code(200).message("").request(request)
                        .protocol(Protocol.HTTP_2).body(it).build()
                }
            }
        }
        return response
    }

    private suspend fun getPagedMockedResponse(page: Int, pageSize: Int): ResponseBody {
        delay(getDelay(page))
        var mockedNews: String
        withContext(Dispatchers.IO) {
            mockedNews = context.resources.openRawResource(R.raw.mocked_news).bufferedReader()
                .use { it.readText() }
        }
        var json: JSONObject
        withContext(Dispatchers.Main) {
            json = JSONObject(mockedNews)
            val articles = json.getJSONArray("articles")
            val mockedArticles = JSONArray()
            for (i in (0..articles.length())) {
                if (i in (page - 1) * pageSize until page * pageSize) {
                    articles.getJSONObject(i).put("mocked", true)
                    mockedArticles.put(articles.getJSONObject(i))
                }
            }
            json.remove("articles")
            json.put("articles", mockedArticles)
        }
        return json.toString().toResponseBody("application/json".toMediaType())
    }

    private fun getDelay(page:Int):Long{
        return if(page == 1){
            1000L
        } else{
            500L
        }
    }
}
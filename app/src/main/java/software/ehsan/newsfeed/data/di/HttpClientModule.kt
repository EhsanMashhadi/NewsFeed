package software.ehsan.newsfeed.data.di

import android.content.Context
import software.ehsan.newsfeed.data.source.remote.interceptor.ApiKeyInterceptor
import software.ehsan.newsfeed.data.source.remote.interceptor.MockResponseInterceptor
import software.ehsan.newsfeed.data.source.remote.interceptor.OfflineCacheInterceptor
import software.ehsan.newsfeed.data.source.remote.interceptor.OnlineCacheInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HttpClientModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor,
        onlineCacheInterceptor: OnlineCacheInterceptor,
        offlineCacheInterceptor: OfflineCacheInterceptor,
        mockResponseInterceptor: MockResponseInterceptor,
        cache: Cache
    ): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().cache(cache)
            .addInterceptor(offlineCacheInterceptor)
            .addNetworkInterceptor(onlineCacheInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(mockResponseInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache {
        //10 MB cache size
        val cacheSize = (10 * 1024 * 1024).toLong()
        return Cache(context.cacheDir, cacheSize)
    }
}
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class InterceptorModule {

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): ApiKeyInterceptor {
        return ApiKeyInterceptor()
    }

    @Provides
    @Singleton
    fun provideCacheInterceptor(@ApplicationContext context: Context): OnlineCacheInterceptor {
        return OnlineCacheInterceptor(context)
    }

    @Provides
    @Singleton
    fun provideOfflineCacheInterceptor(@ApplicationContext context: Context): OfflineCacheInterceptor {
        return OfflineCacheInterceptor(context = context)
    }

    @Provides
    @Singleton
    fun provideMockedResponseInterceptor(@ApplicationContext context: Context): MockResponseInterceptor {
        return MockResponseInterceptor(context)
    }
}
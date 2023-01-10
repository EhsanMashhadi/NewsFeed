package software.ehsan.newsfeed.data.di

import software.ehsan.newsfeed.BuildConfig
import software.ehsan.newsfeed.data.source.remote.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): RetrofitService {
        return Retrofit.Builder().baseUrl(BuildConfig.API_BASE_URL).client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create()).build()
            .create(RetrofitService::class.java)
    }
}
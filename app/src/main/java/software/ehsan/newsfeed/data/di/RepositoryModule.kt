package software.ehsan.newsfeed.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import software.ehsan.newsfeed.data.repository.*
import software.ehsan.newsfeed.data.source.database.firebase.FirebaseRealTimeDatabase
import software.ehsan.newsfeed.data.source.remote.LoginManager
import software.ehsan.newsfeed.data.source.remote.RetrofitService
import software.ehsan.newsfeed.data.source.shared_preference.SharedPreferenceInterface
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideDeviceRepository(@ApplicationContext context: Context): DeviceRepository {
        return DeviceRepositoryImp(context = context)
    }

    @Provides
    @Singleton
    fun provideFeedbackRepository(firebaseRealTimeDatabase: FirebaseRealTimeDatabase): FeedbackRepository {
        return FeedbackRepositoryImp(firebaseRealTimeDatabase)
    }

    @Provides
    @Singleton
    fun provideAdsRepository(sharedPreferenceInterface: SharedPreferenceInterface): AdRepository {
        return AdRepositoryImp(sharedPreference = sharedPreferenceInterface)
    }

    @Provides
    @Singleton
    fun provideArticleRepository(database: FirebaseRealTimeDatabase): ArticleRepository {
        return ArticleRepositoryImp(database = database)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        retrofitService: RetrofitService,
        database: FirebaseRealTimeDatabase
    ): NewsRepository {
        return NewsRepositoryImp(retrofitService = retrofitService, database = database)
    }

    @Provides
    @Singleton
    fun provideUserRepository(loginManager: LoginManager): UserRepository {
        return UserRepositoryImp(loginManager = loginManager)
    }
}
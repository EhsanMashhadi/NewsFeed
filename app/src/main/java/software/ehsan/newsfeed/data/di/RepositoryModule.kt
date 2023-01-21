package software.ehsan.newsfeed.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import software.ehsan.newsfeed.data.repository.DeviceRepository
import software.ehsan.newsfeed.data.repository.DeviceRepositoryImp
import software.ehsan.newsfeed.data.repository.FeedbackRepository
import software.ehsan.newsfeed.data.repository.FeedbackRepositoryImp
import software.ehsan.newsfeed.data.source.database.firebase.FirebaseRealTimeDatabase
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
}
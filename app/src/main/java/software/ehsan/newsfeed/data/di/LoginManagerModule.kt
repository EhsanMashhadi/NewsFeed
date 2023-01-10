package software.ehsan.newsfeed.data.di

import android.content.Context
import software.ehsan.newsfeed.data.source.remote.LoginManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LoginManagerModule {

    @Provides
    @Singleton
    fun providesLoginManager(@ApplicationContext appContext: Context): LoginManager {
        return LoginManager(context = appContext, Dispatchers.IO)
    }
}
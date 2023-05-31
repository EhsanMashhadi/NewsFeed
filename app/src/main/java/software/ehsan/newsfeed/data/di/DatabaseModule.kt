package software.ehsan.newsfeed.data.di

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import software.ehsan.newsfeed.data.source.shared_preference.SharedPreferenceImp
import software.ehsan.newsfeed.data.source.shared_preference.SharedPreferenceInterface
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideFirebaseRTDatabase(): FirebaseDatabase {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        return Firebase.database
    }

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferenceInterface {
        return SharedPreferenceImp(context = context)
    }
}
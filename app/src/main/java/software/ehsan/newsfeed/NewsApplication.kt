package software.ehsan.newsfeed

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(AndroidLogAdapter())
        MobileAds.initialize(this)
    }
}
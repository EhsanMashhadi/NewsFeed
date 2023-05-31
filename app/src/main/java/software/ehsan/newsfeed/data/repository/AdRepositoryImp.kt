package software.ehsan.newsfeed.data.repository

import software.ehsan.newsfeed.data.source.shared_preference.SharedPreferenceInterface

class AdRepositoryImp(private val sharedPreference: SharedPreferenceInterface) : AdRepository,
    BaseRepository() {

    companion object{
        const val AD_LAST_TIME_SHOWN_KEY = "interstitial_ad_last_shown"
    }

    override suspend fun insertAdShownTime(): Boolean {
        val value = System.currentTimeMillis()
        return sharedPreference.insertStringSync(AD_LAST_TIME_SHOWN_KEY, value.toString())
    }

    override suspend fun getLastAdShownTime():String? {
        return sharedPreference.getString(AD_LAST_TIME_SHOWN_KEY)
    }
}
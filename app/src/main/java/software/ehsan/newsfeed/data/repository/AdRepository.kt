package software.ehsan.newsfeed.data.repository

interface AdRepository {

    suspend fun insertAdShownTime():Boolean
    suspend fun getLastAdShownTime():String?
}
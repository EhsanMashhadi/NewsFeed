package software.ehsan.newsfeed.data.repository

interface DeviceRepository {
    fun getOsVersion():String
    fun getAppVersion():String?
}
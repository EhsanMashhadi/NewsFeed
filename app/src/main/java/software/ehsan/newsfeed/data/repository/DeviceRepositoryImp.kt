package software.ehsan.newsfeed.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

class DeviceRepositoryImp(val context: Context) : DeviceRepository {

    override fun getOsVersion(): String {
        val versionName = Build.VERSION.RELEASE
        val sdkVersion = Build.VERSION.SDK_INT.toString()
        val codeName = Build.VERSION.CODENAME
        return "$versionName($sdkVersion-$codeName)"
    }

    override fun getAppVersion(): String? {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val versionName: String = packageInfo.versionName
            val versionCode = packageInfo.versionCode
            return "$versionName($versionCode)"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }
}
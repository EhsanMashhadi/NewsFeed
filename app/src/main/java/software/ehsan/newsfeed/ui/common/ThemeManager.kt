package software.ehsan.newsfeed.ui.common

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import software.ehsan.newsfeed.R

class ThemeManager {
    companion object {
        fun setFontSize(font_size: String, context: Context) {
            var theme = R.style.MediumTheme
            when (font_size) {
                "Small" -> theme = R.style.SmallTheme
                "Medium" -> theme = R.style.MediumTheme
                "Large" -> theme = R.style.LargeTheme
                "XLarge" -> theme = R.style.ExtraLargeTheme
            }
            context.setTheme(theme)
        }

        fun initFontSize(context: Context) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val fontSize = sharedPreferences.getString("font_size", "")
            setFontSize(fontSize!!, context)
        }

        fun setTheme(theme: String) {
            var mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            when (theme) {
                "Day" -> mode = AppCompatDelegate.MODE_NIGHT_NO
                "Night" -> mode = AppCompatDelegate.MODE_NIGHT_YES
                "Automatic (Follow device theme)" -> mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            AppCompatDelegate.setDefaultNightMode(mode)
        }

        fun initTheme(context: Context) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val theme = sharedPreferences.getString("theme", "Automatic (Follow device theme)")
            setTheme(theme = theme!!)
        }
    }

}
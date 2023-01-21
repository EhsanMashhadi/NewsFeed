package software.ehsan.newsfeed.ui.base

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat

abstract class BasePreferenceFragment : PreferenceFragmentCompat(), Initializers {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUiComponents()
        subscribeLiveData()
    }
}
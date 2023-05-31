package software.ehsan.newsfeed.ui.profile

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import software.ehsan.newsfeed.R
import software.ehsan.newsfeed.ui.base.BasePreferenceFragment

class AboutApplicationFragment : BasePreferenceFragment() {


    companion object {
        const val PREFERENCE_SEND_FEEDBACK_KEY = "send_feedback"
    }

    override fun initUiComponents() {
        initSendFeedback()
    }

    private fun initSendFeedback() {
        val prefSendFeedback = findPreference<Preference>(PREFERENCE_SEND_FEEDBACK_KEY)
        prefSendFeedback!!.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_aboutApplicationFragment_to_sendFeedbackFragment)
            true
        }
    }

    override fun subscribeLiveData() {

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_about_application, rootKey)
    }


}
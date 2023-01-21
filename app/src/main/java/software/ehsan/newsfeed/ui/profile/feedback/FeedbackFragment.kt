package software.ehsan.newsfeed.ui.profile.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import software.ehsan.newsfeed.R
import software.ehsan.newsfeed.data.model.Status
import software.ehsan.newsfeed.databinding.FragmentFeedbackBinding
import software.ehsan.newsfeed.ui.base.BaseBottomSheetDialogFragment

@AndroidEntryPoint
class FeedbackFragment : BaseBottomSheetDialogFragment() {

    private var _binding: FragmentFeedbackBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FeedbackViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedbackBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserAppProperties()
    }


    override fun initUiComponents() {

        binding.buttonSendFeedbackSend.setOnClickListener {
            viewModel.sendFeedback(
                email = binding.textInputEditTextSendFeedbackFragmentEmail.text.toString(),
                feedback = binding.textInputEditTextSendFeedbackFragmentFeedback.text.toString()
            )
        }
    }

    override fun subscribeLiveData() {
        subscribeUserAppProperties()
        subscribeFeedback()
    }


    private fun subscribeUserAppProperties() {
        viewModel.userAppPropertiesLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> binding.textViewSendFeedbackFragmentUserAppProperties.text =
                    getString(
                        R.string.feedbackFragment_userProperties,
                        it.data!!.osVersion,
                        it.data.appVersion,
                        it.data.user.email ?: getString(R.string.all_na)
                    )
                else -> {}
            }
        }
    }

    private fun subscribeFeedback() {
        viewModel.feedbackLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Logger.d("Feedback sent!")
                        showSuccessMessage(message = getString(R.string.feedbackFragment_success))
                        dismiss()
                    }
                    Status.ERROR -> {
                        Logger.e("Feedback ERROR")
                        showFailedMessage(message = getString(R.string.feedbackFragment_failed))
                    }
                    else -> {}
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
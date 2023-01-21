package software.ehsan.newsfeed.ui.base

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.crashlytics.FirebaseCrashlytics
import software.ehsan.newsfeed.ui.common.message.ErrorMessageSnackbar
import software.ehsan.newsfeed.ui.common.message.InformativeMessageSnackbar

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment(), Initializers {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUiComponents()
        subscribeLiveData()
    }

    open fun showSuccessMessage(view: View? = null, message: String) {
        var requestedView = view
        if(requestedView==null){
            requestedView = getRootView()
        }
        requestedView?.let {
            InformativeMessageSnackbar(it, message).show()
        }
    }

    open fun showFailedMessage(view: View?=null, message: String) {
        var requestedView = view
        if(requestedView==null){
            requestedView = getRootView()
        }
        requestedView?.let {
            ErrorMessageSnackbar(it, message).show()
        }
    }

    private fun getRootView(): View? {
        return try {
            requireActivity().findViewById(android.R.id.content)
        } catch (exception: IllegalStateException) {
            FirebaseCrashlytics.getInstance().log(exception.toString())
            null
        }
    }
}
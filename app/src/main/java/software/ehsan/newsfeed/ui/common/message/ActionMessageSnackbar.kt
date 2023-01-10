package software.ehsan.newsfeed.ui.common.message

import android.view.View
import com.google.android.material.snackbar.Snackbar

class ActionMessageSnackbar(
    private val view: View,
    private val message: String,
    private val duration: Int,
    private val actionText: String,
    private val actionListener: ((View)) -> Unit
) {

    fun show() {
        val snackbar = Snackbar.make(view, message, duration)
        snackbar.setAction(actionText, actionListener)
        snackbar.show()
    }
}
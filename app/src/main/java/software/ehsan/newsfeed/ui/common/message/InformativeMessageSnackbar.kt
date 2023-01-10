package software.ehsan.newsfeed.ui.common.message

import android.view.View
import com.google.android.material.snackbar.Snackbar

class InformativeMessageSnackbar(
    private val view: View, private val message: String, private val short: Boolean = false
) {

    fun show() {
        val snackbar =
            Snackbar.make(view, message, if (short) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}
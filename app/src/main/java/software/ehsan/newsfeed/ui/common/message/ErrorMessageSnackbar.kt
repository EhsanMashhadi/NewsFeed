package software.ehsan.newsfeed.ui.common.message

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import software.ehsan.newsfeed.R

class ErrorMessageSnackbar(
    private val view: View, private val message: String, private val short: Boolean = false
) {

    fun show() {
        val snackbar =
            Snackbar.make(view, message, if (short) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.pink_600))
        snackbar.show()
    }
}
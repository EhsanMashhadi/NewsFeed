package software.ehsan.newsfeed.ui.common.button

import android.graphics.Color
import android.view.Gravity
import com.google.android.gms.common.SignInButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable

lateinit var originalText: String

fun MaterialButton.showLoading() {
    isClickable = false
    originalText = text.toString()
    val spec = CircularProgressIndicatorSpec(
        this.context,
        null,
        0,
        com.google.android.material.R.style.Widget_Material3_CircularProgressIndicator_ExtraSmall
    )
    spec.trackColor = Color.WHITE
    val progressIndicatorDrawable = IndeterminateDrawable.createCircularDrawable(this.context, spec)
    icon = progressIndicatorDrawable
    text = ""
}

fun MaterialButton.showOriginal() {
    isClickable = true
    if (::originalText.isInitialized) {
        text = originalText
        icon = null
    }
}
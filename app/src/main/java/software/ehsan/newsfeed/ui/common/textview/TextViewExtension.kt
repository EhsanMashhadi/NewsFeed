package software.ehsan.newsfeed.ui.common.textview

import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.widget.TextView


fun TextView.justifyIfPossible() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        this.justificationMode = JUSTIFICATION_MODE_INTER_WORD
    }
}


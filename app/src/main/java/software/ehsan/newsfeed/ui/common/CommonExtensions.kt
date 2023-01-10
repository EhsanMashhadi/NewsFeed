package software.ehsan.newsfeed.ui.common

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment

fun Resources.getRawDimensionInDp(@DimenRes dimenResId: Int): Float {
    val value = TypedValue()
    getValue(dimenResId, value, true)
    return TypedValue.complexToFloat(value.data)
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    try{
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)}
    catch (exception:java.lang.Exception){

    }
}
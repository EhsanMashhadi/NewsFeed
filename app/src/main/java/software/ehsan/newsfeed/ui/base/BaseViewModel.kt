package software.ehsan.newsfeed.ui.base

import androidx.lifecycle.ViewModel


open class BaseViewModel : ViewModel() {

    protected val TAG: String = this.javaClass.simpleName
}
package software.ehsan.newsfeed.ui.base

import androidx.lifecycle.ViewModel
import com.orhanobut.logger.Logger


open class BaseViewModel : ViewModel() {

    protected val TAG: String = this.javaClass.simpleName
}
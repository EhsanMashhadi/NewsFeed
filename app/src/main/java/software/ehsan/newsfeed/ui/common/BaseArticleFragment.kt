package software.ehsan.newsfeed.ui.common

import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import software.ehsan.newsfeed.R
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.ui.base.BaseFragment

open class BaseArticleFragment : BaseFragment() {

    override fun initUiComponents() {

    }

    override fun subscribeLiveData() {
    }

    open fun shareArticle(article: Article) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, article.url)
            putExtra(Intent.EXTRA_TITLE, article.title)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    open fun showFullArticle(url: String) {
        (activity as MainActivity).showAds()
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
    }

    protected fun showSuccessSaveMessage() {
        showSuccessMessage(message = getString(R.string.all_savedSuccessfully))
    }

    protected fun showSuccessUnSaveMessage() {
        showSuccessMessage(message = getString(R.string.all_unsavedSuccessfully))
    }

    protected fun showFailedSaveMessage() {
        showFailedMessage(message = getString(R.string.all_savedError))
    }

    protected fun showInternetError() {
        showFailedMessage(message = getString(R.string.all_internetError))
    }

    protected fun showRateLimitedError() {
        showFailedMessage(message = getString(R.string.all_rateLimitError))
    }
}
package software.ehsan.newsfeed.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import software.ehsan.newsfeed.R
import software.ehsan.newsfeed.data.exception.UnauthorizedAccessException
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.model.Status
import software.ehsan.newsfeed.databinding.FragmentArticlesBinding
import software.ehsan.newsfeed.ui.common.BaseArticleFragment
import software.ehsan.newsfeed.ui.common.SaveEvent
import software.ehsan.newsfeed.ui.common.message.ActionMessageSnackbar
import software.ehsan.newsfeed.ui.profile.ProfileFragment
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.UnknownHostException

@AndroidEntryPoint
class ArticlesFragment : BaseArticleFragment() {

    private val articlesViewModel: ArticlesViewModel by viewModels()

    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!

    private var _articlePagingAdapter: ArticlePagingAdapter? = null
    private val articlePagingAdapter get() = _articlePagingAdapter!!

    private var category: String? = null

    companion object {
        const val HEADLINE_KEY = "headline_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.arguments?.let {
            this.category = it.getString(HEADLINE_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticlesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initUiComponents() {

        initRecycleView()
    }

    override fun subscribeLiveData() {
        subscribeNewsPaging()
        subscribeSavedArticle()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchNews()
    }

    private fun showLoading() {
        binding.shimmerViewContainer.startShimmer()
        binding.shimmerViewContainer.visibility = View.VISIBLE
        binding.recyclerviewArticlesList.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.shimmerViewContainer.visibility = View.GONE
        binding.recyclerviewArticlesList.visibility = View.VISIBLE
        binding.shimmerViewContainer.stopShimmer()
    }

    private fun fetchNews() {
        category?.let {
            getTopHeadlinesByCategory(it)
        } ?: run {
            val preferenceManager = PreferenceManager.getDefaultSharedPreferences(requireActivity())
            val country = preferenceManager.getString(ProfileFragment.PREFERENCE_COUNTRY_KEY, null)
            getTopHeadlinesByCountry(country!!)
        }
    }

    private fun initRecycleView() {
        _articlePagingAdapter = ArticlePagingAdapter(ArticleComparator)
        articlePagingAdapter.setOnItemClickListener(object :
            ArticlePagingAdapter.OnItemClickListener {
            override fun onItemClick(url: String) {
                showFullArticle(url)
            }

            override fun onItemSave(article: Article) {
                articlesViewModel.saveOrUnSaveArticle(article)
            }

            override fun onItemShare(article: Article) {
                shareArticle(article)
            }
        })

        binding.recyclerviewArticlesList.let {
            val layoutManager = LinearLayoutManager(context)
            it.layoutManager = layoutManager
            it.adapter = articlePagingAdapter
            it.addItemDecoration(
                MaterialDividerItemDecoration(
                    it.context, layoutManager.orientation
                )
            )
        }
    }


    private fun getTopHeadlinesByCategory(category: String) {
        articlesViewModel.getTopHeadlineNewsByCategory(category)
    }

    private fun getTopHeadlinesByCountry(country: String) {
        articlesViewModel.getTopHeadlineNewsByCountry(country)
    }

    private fun subscribeNewsPaging() {

        viewLifecycleOwner.lifecycleScope.launch {
            articlePagingAdapter.addLoadStateListener { loadState ->
                if (loadState.refresh is LoadState.Loading) {
                    showLoading()
                } else if (loadState.refresh is LoadState.Error) {
                    hideLoading()
                    showError(Exception((loadState.refresh as LoadState.Error).error))
                } else {
                    hideLoading()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            articlesViewModel.newsPagingLiveData.asFlow().catch {
                Logger.e(it.toString())
            }.collectLatest {
                hideLoading()
                articlePagingAdapter.submitData(it.data!!)
            }
        }
    }

    private fun showError(exception: Exception?) {
        exception?.let {
            when (it) {
                is UnauthorizedAccessException -> showUnauthorizedSaveMessage()
                is UnknownHostException -> showInternetError()
                else -> {
                    if (it.message?.contains("rateLimited") == true) {
                        showRateLimitedError()
                    }
                    else{
                        showFailedSaveMessage()
                    }
                }
            }
        }
    }

    private fun subscribeSavedArticle() {
        articlesViewModel.saveArticleLiveData.observe(viewLifecycleOwner) { resource ->
            resource.getContentIfNotHandled()?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { saveEvent ->
                            when (saveEvent) {
                                is SaveEvent.SavedArticleSuccessfully -> {
                                    showSuccessSaveMessage()
                                    this.articlePagingAdapter.updateArticle(saveEvent.article)
                                }
                                is SaveEvent.UnSaveArticleSuccessfully -> {
                                    showSuccessUnSaveMessage()
                                    this.articlePagingAdapter.updateArticle(saveEvent.article)
                                }
                            }
                        }
                    }
                    Status.ERROR -> showError(it.exception)
                    Status.LOADING -> {}
                }
            }
        }
    }

    private fun showUnauthorizedSaveMessage() {
        ActionMessageSnackbar(
            binding.root,
            getString(R.string.articlesFragment_unauthorizedMessage),
            duration = Snackbar.LENGTH_LONG,
            getString(R.string.loginFragment_login)
        ) {
            findNavController().navigate(R.id.loginFragment)
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _articlePagingAdapter = null
    }
}
package software.ehsan.newsfeed.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import software.ehsan.newsfeed.R
import software.ehsan.newsfeed.data.exception.UnauthorizedAccessException
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.databinding.FragmentArticleSearchBinding
import software.ehsan.newsfeed.ui.common.BaseArticleFragment
import software.ehsan.newsfeed.ui.common.ArticleEvent
import software.ehsan.newsfeed.ui.common.message.ActionMessageSnackbar
import software.ehsan.newsfeed.ui.dashboard.ArticleComparator
import software.ehsan.newsfeed.ui.dashboard.ArticlePagingAdapter

@AndroidEntryPoint
class SearchArticleFragment : BaseArticleFragment() {

    private var _binding: FragmentArticleSearchBinding? = null
    private val binding get() = _binding!!

    private var _searchArticleAdapter: ArticlePagingAdapter? = null
    private val searchArticleAdapter get() = _searchArticleAdapter!!

    private val viewModel: SearchArticlesViewModel by viewModels()
    private val args: SearchArticleFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun initUiComponents() {
        binding.shimmerViewContainer.startShimmer()
        initRecycleView()
        getNews(args.query)
    }

    override fun subscribeLiveData() {
        subscribeNews()
        subscribeSavedArticle()
    }

    private fun initRecycleView() {
        _searchArticleAdapter = ArticlePagingAdapter(ArticleComparator)
        this.searchArticleAdapter.setOnItemClickListener(object :
            ArticlePagingAdapter.OnItemClickListener {
            override fun onItemClick(url: String) {
                showFullArticle(url = url)
            }

            override fun onItemSave(article: Article) {
                viewModel.saveOrUnSaveArticle(article)
            }

            override fun onItemShare(article: Article) {
                shareArticle(article = article)
            }
        })

        this.binding.recyclerviewSearchArticlesList.let {
            val layoutManager = LinearLayoutManager(context)
            it.layoutManager = layoutManager
            it.adapter = searchArticleAdapter
            it.addItemDecoration(
                MaterialDividerItemDecoration(
                    it.context, layoutManager.orientation
                )
            )
        }
    }

    private fun getNews(keyword: String) {
        viewModel.getNews(keyword = keyword)
    }

    private fun subscribeNews() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.newsLiveData.asFlow().collectLatest {
                binding.recyclerviewSearchArticlesList.visibility = View.VISIBLE
                searchArticleAdapter.submitData(it.data!!)
            }
        }
    }

    private fun subscribeSavedArticle() {
        viewModel.saveArticleLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    is ArticleEvent.SavedArticleSuccessfully -> {
                        showSuccessSaveMessage()
                        this.searchArticleAdapter.updateArticle(event.article)
                    }
                    is ArticleEvent.UnSaveArticleSuccessfully -> {
                        showSuccessUnSaveMessage()
                        this.searchArticleAdapter.updateArticle(event.article)
                    }
                    is ArticleEvent.SaveArticleError -> {
                        showError(event.exception)
                    }
                    is ArticleEvent.UnSaveArticleError -> {
                        showError(event.exception)
                    }
                }
            }
        }
    }

    private fun showUnauthorizedSaveMessage() {
        ActionMessageSnackbar(
            binding.root,
            getString(R.string.all_shouldLogin),
            duration = Snackbar.LENGTH_LONG,
            getString(R.string.loginFragment_login)
        ) {
            findNavController().navigate(R.id.loginFragment)
        }.show()
    }

    private fun showError(exception: java.lang.Exception?) {
        exception?.let {
            when (it) {
                is UnauthorizedAccessException -> showUnauthorizedSaveMessage()
                else -> showFailedSaveMessage()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _searchArticleAdapter = null
    }
}
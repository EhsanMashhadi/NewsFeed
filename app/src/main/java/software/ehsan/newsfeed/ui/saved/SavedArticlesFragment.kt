package software.ehsan.newsfeed.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import software.ehsan.newsfeed.R
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.data.model.Status
import software.ehsan.newsfeed.databinding.FragmentArticleSavedBinding
import software.ehsan.newsfeed.ui.common.BaseArticleFragment
import software.ehsan.newsfeed.ui.common.SaveEvent
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.firebase.database.DatabaseException
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedArticlesFragment : BaseArticleFragment() {

    private var _binding: FragmentArticleSavedBinding? = null
    private val binding get() = _binding!!

    private var _newsAdapter: SavedArticleAdapter? = null
    private val newsAdapter get() = _newsAdapter!!

    private val viewModel: SavedArticlesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleSavedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSavedArticles()
    }

    override fun initUiComponents() {
        initRecycleView()
    }

    private fun hideRecyclerView() {
        binding.recyclerviewSavedArticlesFragmentArticleList.visibility = View.GONE
    }

    private fun showRecyclerView() {
        binding.recyclerviewSavedArticlesFragmentArticleList.visibility = View.VISIBLE
    }

    private fun showShimmer() {
        binding.shimmerLayoutSavedArticlesFragment.shimmerViewContainer.run {
            this.visibility = View.VISIBLE
            this.startShimmer()
        }
    }

    private fun hideShimmer() {
        binding.shimmerLayoutSavedArticlesFragment.shimmerViewContainer.run {
            this.visibility = View.GONE
            this.stopShimmer()
        }
    }

    private fun initRecycleView() {
        _newsAdapter = SavedArticleAdapter()
        this.newsAdapter.setOnItemClickListener(object : SavedArticleAdapter.OnItemClickListener {
            override fun onItemClick(url: String) {
                showFullArticle(url = url)
            }

            override fun onItemSave(article: Article) {
                viewModel.saveOrUnSaveArticle(article = article)
            }

            override fun onItemShare(article: Article) {
                shareArticle(article = article)
            }
        })

        binding.recyclerviewSavedArticlesFragmentArticleList.let {
            val layoutManager = LinearLayoutManager(context)
            it.layoutManager = layoutManager
            it.adapter = newsAdapter
            it.addItemDecoration(
                MaterialDividerItemDecoration(
                    it.context, layoutManager.orientation
                )
            )
        }
    }

    override fun subscribeLiveData() {
        subscribeNews()
        subscribeSavedArticle()
    }

    private fun subscribeSavedArticle() {
        viewModel.saveArticleLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { saveEvent ->
                            when (saveEvent) {
                                is SaveEvent.SavedArticleSuccessfully -> {
                                    showSuccessSaveMessage()
                                }
                                is SaveEvent.UnSaveArticleSuccessfully -> {
                                    showSuccessUnSaveMessage()
                                }
                            }
                        }
                    }
                    Status.ERROR -> showError(resource.exception)
                    Status.LOADING -> {}
                }
            }
        }
    }

    private fun subscribeNews() {
        viewModel.savedArticlesLiveData.observe(viewLifecycleOwner) {
            Logger.d(it)
            when (it.status) {
                Status.SUCCESS -> {
                    hideShimmer()
                    showRecyclerView()
                    showSavedNews(it.data)
                }
                Status.ERROR -> {
                    hideShimmer()
                    showError(it.exception)
                }
                Status.LOADING -> {
                    hideRecyclerView()
                    showShimmer()
                }
            }
        }
    }


    private fun showSavedNews(data: List<Article>?) {
        if (data!!.isNotEmpty()) {
            newsAdapter.updateNews(articles = data)
            binding.recyclerviewSavedArticlesFragmentArticleList.visibility = View.VISIBLE
            binding.textViewSavedArticlesFragmentInfoText.visibility = View.GONE
        } else {
            showEmptyListError()
        }
    }


    private fun showError(exception: Exception?) {
        when (exception?.cause) {
            is DatabaseException -> showNotLoginError()
        }
    }

    private fun showNotLoginError() {
        binding.textViewSavedArticlesFragmentInfoText.text =
            getString(R.string.all_shouldLogin)
        binding.recyclerviewSavedArticlesFragmentArticleList.visibility = View.GONE
        binding.textViewSavedArticlesFragmentInfoText.visibility = View.VISIBLE
    }

    private fun showEmptyListError() {
        binding.textViewSavedArticlesFragmentInfoText.text =
            getString(R.string.savedArticlesFragment_noArticle)
        binding.recyclerviewSavedArticlesFragmentArticleList.visibility = View.GONE
        binding.textViewSavedArticlesFragmentInfoText.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _newsAdapter = null
    }
}
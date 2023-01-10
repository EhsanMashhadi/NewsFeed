package software.ehsan.newsfeed.ui.saved

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import software.ehsan.newsfeed.R
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.databinding.RowArticleBinding
import software.ehsan.newsfeed.util.DateConvertor
import com.orhanobut.logger.Logger

class SavedArticleAdapter : RecyclerView.Adapter<SavedArticleAdapter.NewsViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(url: String)
        fun onItemSave(article: Article)
        fun onItemShare(article: Article)
    }

    private var articles = ArrayList<Article>()
    private lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val newsRowItemBinding =
            RowArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(newsRowItemBinding, this.onItemClickListener)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun updateNews(articles: List<Article>) {
        val articleDiffUtilCallback = ArticleDiffUtilCallback(this.articles, articles)
        val diff = DiffUtil.calculateDiff(articleDiffUtilCallback)
        this.articles.clear()
        this.articles.addAll(articles)
        diff.dispatchUpdatesTo(this)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class NewsViewHolder(
        private val newsRowItemBinding: RowArticleBinding,
        private val onItemClickListener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(newsRowItemBinding.root) {

        init {
            setClickListener()
        }

        private fun setClickListener() {
            newsRowItemBinding.root.setOnClickListener {
                Logger.d("Click on article position: %s", bindingAdapterPosition)
                onItemClickListener.onItemClick(articles[bindingAdapterPosition].url)
            }
            newsRowItemBinding.imageViewArticleRowItemSave.setOnClickListener {
                onItemClickListener.onItemSave(articles[bindingAdapterPosition])
            }
            newsRowItemBinding.imageViewArticleRowItemShare.setOnClickListener {
                onItemClickListener.onItemShare(articles[bindingAdapterPosition])
            }
        }

        fun bind(article: Article) {
            newsRowItemBinding.textViewArticleRowItemTitle.text = article.title
            newsRowItemBinding.textViewArticleRowItemSource.text = article.source.name
            newsRowItemBinding.textViewArticleRowItemDescription.text = article.description
            newsRowItemBinding.textViewArticleRowItemDate.text =
                DateConvertor.formatDate(article.date)
            if (article.isSaved) {
                newsRowItemBinding.imageViewArticleRowItemSave.setImageResource(R.drawable.ic_save_filled)
            } else {
                newsRowItemBinding.imageViewArticleRowItemSave.setImageResource(R.drawable.ic_save)
            }
            if (article.imageUrl == null) {
                newsRowItemBinding.imageViewArticleRowItemImage.visibility = View.GONE
            } else {
                newsRowItemBinding.imageViewArticleRowItemImage.visibility = View.VISIBLE
                Glide.with(newsRowItemBinding.root.context).load(article.imageUrl).centerCrop()
                    .error(R.drawable.ic_launcher_foreground)
                    .into(newsRowItemBinding.imageViewArticleRowItemImage)
            }
        }
    }
}
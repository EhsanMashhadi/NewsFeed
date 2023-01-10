package software.ehsan.newsfeed.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import software.ehsan.newsfeed.R
import software.ehsan.newsfeed.data.model.Article
import software.ehsan.newsfeed.databinding.RowArticleBinding
import software.ehsan.newsfeed.ui.common.textview.justifyIfPossible
import software.ehsan.newsfeed.util.DateConvertor
import com.orhanobut.logger.Logger

class ArticlePagingAdapter(diffCallback: DiffUtil.ItemCallback<Article>) :
    PagingDataAdapter<Article, ArticlePagingAdapter.NewsViewHolder>(diffCallback) {

    interface OnItemClickListener {
        fun onItemClick(url: String)
        fun onItemSave(article: Article)
        fun onItemShare(article: Article)
    }

    private lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val newsRowItemBinding =
            RowArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(newsRowItemBinding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    fun updateArticle(article: Article) {
        notifyItemChanged(snapshot().items.indexOf(article))
    }


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class NewsViewHolder(private val binding: RowArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            setClickListener()
        }

        private fun setClickListener() {
            binding.root.setOnClickListener {
                Logger.d("Click on %s", "" + bindingAdapterPosition)
                onItemClickListener.onItemClick(getItem(bindingAdapterPosition)!!.url)
            }
            binding.imageViewArticleRowItemSave.setOnClickListener {
                onItemClickListener.onItemSave(getItem(bindingAdapterPosition)!!)
            }
            binding.imageViewArticleRowItemShare.setOnClickListener {
                onItemClickListener.onItemShare(getItem(bindingAdapterPosition)!!)
            }
        }

        fun bind(article: Article) {
            binding.textViewArticleRowItemTitle.text = article.title
            binding.textViewArticleRowItemSource.text = article.source.name
            binding.textViewArticleRowItemDescription.text = article.description
            binding.textViewArticleRowItemDescription.justifyIfPossible()
            binding.textViewArticleRowItemDate.text =
                DateConvertor.formatDate(article.date)
            if(article.mocked){
                binding.textViewArticleRowItemMocked.visibility = View.VISIBLE
            }
            else{
                binding.textViewArticleRowItemMocked.visibility = View.GONE
            }
            if (article.isSaved) {
                binding.imageViewArticleRowItemSave.setImageResource(R.drawable.ic_save_filled)
            } else {
                binding.imageViewArticleRowItemSave.setImageResource(R.drawable.ic_save)
            }
            if (article.imageUrl == null) {
                binding.imageViewArticleRowItemImage.visibility = View.GONE
            } else {
                binding.imageViewArticleRowItemImage.visibility = View.VISIBLE
                Glide.with(binding.root.context).load(article.imageUrl).centerCrop()
                    .error(R.drawable.ic_launcher_foreground)
                    .into(binding.imageViewArticleRowItemImage)
            }
        }
    }
}
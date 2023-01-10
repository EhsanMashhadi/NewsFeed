package software.ehsan.newsfeed.ui.dashboard

import androidx.recyclerview.widget.DiffUtil
import software.ehsan.newsfeed.data.model.Article

object ArticleComparator : DiffUtil.ItemCallback<Article>() {

    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}
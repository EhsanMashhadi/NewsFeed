package software.ehsan.newsfeed.ui.headlines

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import software.ehsan.newsfeed.ui.dashboard.ArticlesFragment

class HeadlinesAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val headlines =
        listOf("business", "entertainment", "general", "health", "science", "sports", "technology")

    override fun getItemCount(): Int {
        return headlines.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = ArticlesFragment()
        fragment.arguments = Bundle().apply {
            putString(ArticlesFragment.HEADLINE_KEY, headlines[position])
        }
        return fragment
    }
}
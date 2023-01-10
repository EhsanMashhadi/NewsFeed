package software.ehsan.newsfeed.ui.headlines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import software.ehsan.newsfeed.R
import software.ehsan.newsfeed.databinding.FragmentHeadlinesBinding
import software.ehsan.newsfeed.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator


class HeadlinesFragment : BaseFragment() {

    private lateinit var fragmentHeadlinesBinding: FragmentHeadlinesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        fragmentHeadlinesBinding = FragmentHeadlinesBinding.inflate(layoutInflater)
        fragmentHeadlinesBinding.viewPagerHeadlinesFragment.adapter = HeadlinesAdapter(this)
        return fragmentHeadlinesBinding.root
    }

    override fun initUiComponents() {
        initTabLayout()
    }

    private fun initTabLayout() {
        TabLayoutMediator(
            this.fragmentHeadlinesBinding.tabLayoutHeadlinesFragment,
            this.fragmentHeadlinesBinding.viewPagerHeadlinesFragment
        ) { tab, position ->
            tab.text =
                requireContext().resources.getStringArray(R.array.tabLayout_headlines)[position]
        }.attach()
    }

    override fun subscribeLiveData() {
    }
}
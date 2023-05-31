package software.ehsan.newsfeed.ui.common

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import software.ehsan.newsfeed.R
import software.ehsan.newsfeed.databinding.ActivityMainBinding
import software.ehsan.newsfeed.ui.dashboard.ArticlesFragmentDirections
import software.ehsan.newsfeed.ui.profile.ProfileFragment


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var mainToolbar: MenuProvider
    private var isMainToolbarVisible = true
    private val viewModel: MainViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Logger.d("No permission")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initSizeTheme()
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView_mainActivity) as NavHostFragment
        navController = navHostFragment.navController
        setSupportActionBar(activityMainBinding.toolbar)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        activityMainBinding.toolbar.setupWithNavController(navController, appBarConfiguration)
        activityMainBinding.bottomNavigationMainActivity.setupWithNavController(navController)
        initMainToolbar()
        checkNotificationPermission()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.adLiveData.observe(this){event->
            event.getContentIfNotHandled()?.let {
                when(it){
                    is MainEvent.ShowAds -> {
                        it.interstitialAd.show(this)
                    }
                }
            }
        }
    }

    private fun initSizeTheme() {
        MainScope().launch(Dispatchers.IO) {
            PreferenceManager.setDefaultValues(
                applicationContext,
                R.xml.preference_profile,
                false
            )
        }
        ThemeManager.initFontSize(context = this@MainActivity)
        ThemeManager.initTheme(context = this@MainActivity)
    }

    private fun initMainToolbar() {
        mainToolbar = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_appbar_main, menu)
                val searchItem: MenuItem? = menu.findItem(R.id.item_main_toolbar_search)
                val searchView = searchItem!!.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        Log.d("SEARCH", query)
                        val action =
                            ArticlesFragmentDirections.actionGlobalSearchArticleFragment(query)
                        navController.navigate(action)
                        return true
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        return false
                    }
                })

                val profile = menu.findItem(R.id.item_main_toolbar_profile)
                profile.setOnMenuItemClickListener {
                    navigateToProfile()
                    true
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }
        addMenuProvider(mainToolbar)
    }

    override fun onResume() {
        super.onResume()
        findNavController(R.id.fragmentContainerView_mainActivity).addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id !in arrayOf(
                    R.id.articlesFragment, R.id.headlinesFragment, R.id.savedArticlesFragment
                )
            ) {
                removeMenuProvider(mainToolbar)
                activityMainBinding.bottomNavigationMainActivity.visibility = View.GONE
                isMainToolbarVisible = false
            } else {
                if (!isMainToolbarVisible) addMenuProvider(mainToolbar)
                isMainToolbarVisible = true
                activityMainBinding.bottomNavigationMainActivity.visibility = View.VISIBLE
            }
        }
    }

    fun navigateToProfile() {
        findNavController(R.id.fragmentContainerView_mainActivity).navigate(R.id.action_global_profileFragment)
    }

    fun showAds(){
        viewModel.showAds()
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        when (pref.key) {
            ProfileFragment.PREFERENCE_ABOUT_APPLICATION_KEY -> {
                findNavController(R.id.fragmentContainerView_mainActivity).navigate(
                    R.id.action_global_aboutApplicationFragment,
                    pref.extras
                )
            }
        }

        return true
    }

    private fun checkNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
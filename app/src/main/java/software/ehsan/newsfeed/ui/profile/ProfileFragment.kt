package software.ehsan.newsfeed.ui.profile

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import software.ehsan.newsfeed.R
import software.ehsan.newsfeed.data.model.Status
import software.ehsan.newsfeed.data.model.User
import software.ehsan.newsfeed.ui.base.BasePreferenceFragment
import software.ehsan.newsfeed.ui.common.ThemeManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : BasePreferenceFragment() {

    companion object {
        const val PREFERENCE_LOGIN_REGISTER_KEY = "login_register"
        const val PREFERENCE_FONT_SIZE_KEY = "font_size"
        const val PREFERENCE_THEME_KEY = "theme"
        const val PREFERENCE_COUNTRY_KEY = "country"
    }

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var toolbarMenu: Menu
    private lateinit var profileToolbarProvider: MenuProvider
    private lateinit var loginRegisterPreference: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_profile, rootKey)
    }

    override fun initUiComponents() {
        initToolbar()
        initLoginRegisterItem()
        initFontSizeItem()
        initThemeItem()
    }

    override fun subscribeLiveData() {
        subscribeUserLiveData()
    }

    private fun initToolbar() {
        profileToolbarProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_appbar_profile, menu)
                toolbarMenu = menu
                viewModel.checkLoggedInUser()
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.item_profile_toolbar_logout) {
                    logout()
                }
                return true
            }
        }
        requireActivity().addMenuProvider(profileToolbarProvider)
    }

    private fun initLoginRegisterItem() {
        loginRegisterPreference = findPreference(PREFERENCE_LOGIN_REGISTER_KEY)!!
        loginRegisterPreference.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            true
        }
    }

    private fun initFontSizeItem() {
        val prefFontSize = findPreference<ListPreference>(PREFERENCE_FONT_SIZE_KEY)
        prefFontSize!!.setOnPreferenceChangeListener { _, _ ->
            requireActivity().recreate()
            true
        }
    }

    private fun initThemeItem() {
        val prefTheme = findPreference<ListPreference>(PREFERENCE_THEME_KEY)
        prefTheme!!.setOnPreferenceChangeListener { _, newValue ->
            ThemeManager.setTheme(newValue as String)
            true
        }
    }

    private fun logout() {
        MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.profileFragment_logOut))
            .setMessage(getString(R.string.profileFragment_logOutMessage))
            .setNegativeButton(getString(R.string.all_cancel), null)
            .setPositiveButton(getString(R.string.profileFragment_logOut)) { _, _ ->
                viewModel.logOut()
            }.show()
    }

    override fun onStop() {
        super.onStop()
        requireActivity().removeMenuProvider(profileToolbarProvider)
    }

    private fun subscribeUserLiveData() {
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> showUserInformation(it.data)
                Status.ERROR -> showUserInformation(null)
                Status.LOADING -> showLoading()
            }
        }
    }

    private fun showUserInformation(user: User?) {
        if (user == null || !user.isAuthenticated) {
            showLoginView()
            hideLogOut()
        } else {
            showUserInformationView(user)
            showLogOUt()
        }
    }

    private fun showLoading() {

    }


    private fun showLogOUt() {
        if (this::toolbarMenu.isInitialized) this.toolbarMenu.findItem(R.id.item_profile_toolbar_logout)?.isVisible =
            true

    }

    private fun hideLogOut() {
        if (this::toolbarMenu.isInitialized) this.toolbarMenu.findItem(R.id.item_profile_toolbar_logout)?.isVisible =
            false
    }

    private fun showUserInformationView(user: User) {
        loginRegisterPreference.title = user.email
        if(!user.isEmailVerified){
            loginRegisterPreference.summary = getString(R.string.profileFragment_notVerified)
        }
        else{
            loginRegisterPreference.summary = getString(R.string.all_empty)
        }
        loginRegisterPreference.isEnabled = false
    }

    private fun showLoginView() {
        loginRegisterPreference.title = getString(R.string.profileFragment_loginRegister)
        loginRegisterPreference.summary = getString(R.string.all_empty)
        loginRegisterPreference.isEnabled = true
    }

}
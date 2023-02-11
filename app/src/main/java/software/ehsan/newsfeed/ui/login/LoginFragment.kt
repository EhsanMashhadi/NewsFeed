package software.ehsan.newsfeed.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import software.ehsan.newsfeed.R
import software.ehsan.newsfeed.databinding.FragmentLoginBinding
import software.ehsan.newsfeed.ui.base.BaseFragment
import software.ehsan.newsfeed.ui.common.button.showLoading
import software.ehsan.newsfeed.ui.common.button.showOriginal
import software.ehsan.newsfeed.ui.common.hideKeyboard
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding?=null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun initUiComponents() {
        initCreateAccount()
        initLogin()
        initEmailValidation()
        initPasswordValidation()
        disableLoginButton()
        initLoginByGoogle()
    }

    private fun initCreateAccount() {
        binding.textViewLoginFragmentCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }
    }

    private fun initLogin() {
        binding.buttonLoginFragmentLogin.setOnClickListener {
            hideKeyboard()
            clearErrorMessages()
            viewModel.loginByEmailPassword(
                binding.textInputEditTextLoginFragmentEmail.text.toString(),
                binding.textInputEditTextLoginFragmentPassword.text.toString()
            )
        }
    }

    private fun initEmailValidation() {
        binding.textInputEditTextLoginFragmentEmail.doAfterTextChanged {
            viewModel.validateEmail(it.toString())
            checkLoginValidation(
                email = it.toString(),
                password = binding.textInputEditTextLoginFragmentPassword.text.toString()
            )
        }
    }

    private fun initPasswordValidation() {
        binding.textInputEditTextLoginFragmentPassword.doAfterTextChanged {
            checkLoginValidation(
                email = binding.textInputEditTextLoginFragmentEmail.text.toString(),
                password = it.toString()
            )
        }
    }

    private fun disableLoginButton() {
        binding.buttonLoginFragmentLogin.isEnabled = false
    }

    private fun initLoginByGoogle() {
        binding.buttonLoginFragmentGoogleLogin.setOnClickListener {
            viewModel.initLoginByGoogle(firebaseResult)
        }
    }

    private fun clearErrorMessages() {
        binding.textInputLayoutLoginFragmentPassword.error = ""
        binding.textInputLayoutLoginFragmentEmail.error = ""
    }

    override fun subscribeLiveData() {
        subscribeEmailValidation()
        subscribeLoginValidation()
        subscribeLoginByEmailPassword()
    }

    private fun subscribeEmailValidation() {
        viewModel.emailValidationLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { resource ->
                if (resource.data!!) {
                    binding.textInputLayoutLoginFragmentEmail.error = null
                } else {
                    binding.textInputLayoutLoginFragmentEmail.error =
                        getString(R.string.loginFragment_wrongEmailFormat)
                }
            }
        }
    }

    private fun subscribeLoginValidation() {
        viewModel.loginValidationLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { resource ->
                binding.buttonLoginFragmentLogin.isEnabled = resource.data!!
            }
        }
    }

    private fun subscribeLoginByEmailPassword() {
        viewModel.loginLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    is LoginEvent.Loading  -> showLoginLoading()
                    is LoginEvent.LoginSuccessfully -> showLoginSuccess()
                    is LoginEvent.LoginFailed -> showLoginError(event.exception)
                }
            }
        }
    }

    private fun showLoginError(exception: Exception?) {
        binding.buttonLoginFragmentLogin.showOriginal()
        exception?.let {
            when (it) {
                is FirebaseAuthInvalidUserException, is FirebaseAuthInvalidCredentialsException -> showWrongAccountPassword()
                else -> showGenericError()
            }
        }
    }

    private fun showGenericError() {
        showFailedMessage(binding.root, getString(R.string.all_genericError))
    }

    private fun showWrongAccountPassword() {
        binding.textInputLayoutLoginFragmentPassword.errorIconDrawable = null
        binding.textInputLayoutLoginFragmentPassword.error =
            getString(R.string.loginFragment_wrongPassword)
    }

    private fun showLoginSuccess() {
        binding.buttonLoginFragmentLogin.showOriginal()
        showSuccessMessage(
            binding.root, getString(R.string.loginFragment_successMessage)
        )
        findNavController().navigate(R.id.action_global_profileFragment)
    }

    private fun showLoginLoading() {
        binding.buttonLoginFragmentLogin.showLoading()
    }


    private fun checkLoginValidation(email: String, password: String) {
        viewModel.checkLoginValidation(
            email = email, password = password
        )
    }

    private val firebaseResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.loginByGoogle(it)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
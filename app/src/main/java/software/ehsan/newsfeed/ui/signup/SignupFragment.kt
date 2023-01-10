package software.ehsan.newsfeed.ui.signup

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import software.ehsan.newsfeed.R
import software.ehsan.newsfeed.data.model.Status
import software.ehsan.newsfeed.ui.base.BaseFragment
import software.ehsan.newsfeed.ui.common.MainActivity
import software.ehsan.newsfeed.ui.common.getRawDimensionInDp
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.ui.text.style.TextAlign


@AndroidEntryPoint
class SignupFragment : BaseFragment() {

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    Column(Modifier.width(IntrinsicSize.Max)) {
                        AddEmailAndPasswordComponents()
                        SignUpButton()
                    }

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeLiveData()
    }

    @Composable
    @Preview
    fun AddEmailAndPasswordComponents() {
        Column {
            EmailEditTextComponent()
            PasswordEditTextComponent()
        }
    }


    @Composable
    fun EmailEditTextComponent() {
        Column {
            TextField(
                value = viewModel.email,
                onValueChange = {
                    viewModel.email = it
                    viewModel.validateEmail()
                },
                trailingIcon = {
                    if (viewModel.emailIsWrong) Icon(
                        Icons.Filled.Error, "error", tint = MaterialTheme.colors.error
                    )
                },
                singleLine = true,
                isError = viewModel.emailIsWrong,
                label = { Text(getString(R.string.all_email)) },
                modifier = Modifier.fillMaxWidth().padding(
                    top = resources.getRawDimensionInDp(R.dimen.margin_16).dp,
                    start = resources.getRawDimensionInDp(R.dimen.margin_16).dp,
                    end = resources.getRawDimensionInDp(R.dimen.margin_16).dp
                ),
            )
            if (viewModel.emailIsWrong) {
                Text(
                    text = getString(R.string.loginFragment_wrongEmailFormat),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }

    @Composable
    fun PasswordEditTextComponent() {
        TextField(
            value = viewModel.password,
            onValueChange = {
                viewModel.password = it
                viewModel.validatePassword()
            },
            label = { Text(getString(R.string.all_password)) },
            modifier = Modifier.fillMaxWidth().padding(
                top = resources.getRawDimensionInDp(R.dimen.margin_16).dp,
                start = resources.getRawDimensionInDp(R.dimen.margin_16).dp,
                end = resources.getRawDimensionInDp(R.dimen.margin_16).dp
            ),
        )
    }

    fun getIcon(): IndeterminateDrawable<CircularProgressIndicatorSpec> {
        val spec = CircularProgressIndicatorSpec(
            this.context!!,
            null,
            0,
            com.google.android.material.R.style.Widget_Material3_CircularProgressIndicator_ExtraSmall
        )
        spec.trackColor = Color.WHITE
        val progressIndicatorDrawable =
            IndeterminateDrawable.createCircularDrawable(this.context!!, spec)
        return progressIndicatorDrawable
    }

    @Composable
    @Preview
    fun SignUpButton() {
        Button(
            enabled = !viewModel.passwordIsShort && !viewModel.emailIsWrong,
            onClick = { signUp() },
            modifier = Modifier.fillMaxWidth().padding(
                top = resources.getRawDimensionInDp(R.dimen.margin_16).dp,
                start = resources.getRawDimensionInDp(R.dimen.margin_16).dp,
                end = resources.getRawDimensionInDp(R.dimen.margin_16).dp
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Box(contentAlignment = androidx.compose.ui.Alignment.Companion.CenterStart) {
                viewModel.signUpWithEmailPasswordLiveData.observeAsState().value?.let {
                    if (it.status == software.ehsan.newsfeed.data.model.Status.LOADING) {
                        CircularProgressIndicator(
                            color = androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier.padding(start = 4.dp).size(24.dp)
                        )
                    }
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = getString(R.string.singUpFragment_signUp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    private fun signUp() {
        viewModel.signUp()
    }

    override fun initUiComponents() {
    }

    override fun subscribeLiveData() {
        viewModel.signUpWithEmailPasswordLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    showSuccessMessage(view!!, getString(R.string.singUpFragment_successMessage))
                    navigateToProfileFragment()
                }
                Status.ERROR -> showError()
                Status.LOADING -> kotlin.run {
                    return@run
                }
            }
        }
    }

    private fun showError() {
        showFailedMessage(view!!, getString(R.string.singUpFragment_failedMessage))
    }


    private fun navigateToProfileFragment() {
        findNavController().navigate(R.id.action_global_profileFragment)
    }
}
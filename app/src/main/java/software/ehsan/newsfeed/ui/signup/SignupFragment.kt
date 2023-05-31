package software.ehsan.newsfeed.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.AndroidEntryPoint
import software.ehsan.newsfeed.R
import software.ehsan.newsfeed.data.model.Status
import software.ehsan.newsfeed.ui.base.BaseFragment
import software.ehsan.newsfeed.ui.common.getRawDimensionInDp


@AndroidEntryPoint
class SignupFragment : BaseFragment() {

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {

            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme(colors = getColorPalette()) {
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
    fun getColorPalette(): Colors {
        val lightColorPalette = lightColors(
            primary = colorResource(id = R.color.purple_500),
            primaryVariant = colorResource(id = R.color.purple_700),
            secondary = colorResource(id = R.color.pink_600),
            secondaryVariant = colorResource(id = R.color.pink_700),
            onPrimary = colorResource(id = R.color.white),
            onSecondary = colorResource(id = R.color.black)
        )

        val darkColorPalette = darkColors(
            primary = colorResource(id = R.color.purple_200),
            primaryVariant = colorResource(id = R.color.purple_700),
            secondary = colorResource(id = R.color.pink_600),
            secondaryVariant = colorResource(id = R.color.pink_600),
            onPrimary = colorResource(id = R.color.black),
            onSecondary = colorResource(id = R.color.black)
        )
        return if (isSystemInDarkTheme()) {
            darkColorPalette
        } else {
            lightColorPalette
        }
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
        Surface {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
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
    }

    @Composable
    fun PasswordEditTextComponent() {
        Surface {
            TextField(
                value = viewModel.password,
                onValueChange = {
                    viewModel.password = it
                    viewModel.validatePassword()
                },
                label = { Text(getString(R.string.all_password)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = resources.getRawDimensionInDp(R.dimen.margin_16).dp,
                        start = resources.getRawDimensionInDp(R.dimen.margin_16).dp,
                        end = resources.getRawDimensionInDp(R.dimen.margin_16).dp
                    ),
            )
        }
    }

    @Composable
    @Preview
    fun SignUpButton() {
        Button(
            enabled = !viewModel.passwordIsShort && !viewModel.emailIsWrong,
            onClick = { signUp() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = resources.getRawDimensionInDp(R.dimen.margin_16).dp,
                    start = resources.getRawDimensionInDp(R.dimen.margin_16).dp,
                    end = resources.getRawDimensionInDp(R.dimen.margin_16).dp
                ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Box(contentAlignment = androidx.compose.ui.Alignment.Companion.CenterStart) {
                viewModel.signUpWithEmailPasswordLiveData.observeAsState().value?.let {
                    if (it.status == Status.LOADING) {
                        CircularProgressIndicator(
                            color = androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(24.dp)
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
                    showSuccessMessage(message = getString(R.string.singUpFragment_successMessage))
                    navigateToProfileFragment()
                }
                Status.ERROR -> showError(it.exception)
                Status.LOADING -> kotlin.run {
                    return@run
                }
            }
        }
    }

    private fun showError(exception: Exception?) {
        when (exception) {
            is FirebaseAuthUserCollisionException -> showFailedMessage(message = getString(R.string.signUpFragment_duplicateAccount))
            else -> showFailedMessage(message = getString(R.string.singUpFragment_failedMessage))
        }
    }


    private fun navigateToProfileFragment() {
        findNavController().navigate(R.id.action_global_profileFragment)
    }
}
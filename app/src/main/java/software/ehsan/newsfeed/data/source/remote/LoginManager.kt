package software.ehsan.newsfeed.data.source.remote

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import software.ehsan.newsfeed.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginManager @Inject constructor(
    @ApplicationContext private val context: Context, private val dispatcher: CoroutineDispatcher
) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val TAG = this.javaClass.simpleName

    suspend fun loginByEmailPassword(email: String, password: String) = withContext(dispatcher) {
        callbackFlow<Task<AuthResult>> {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    trySend(it)
                }
            awaitClose {}
        }
    }

    fun initializeLoginByGoogle(): Intent {
        val googleSingInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id)).requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, googleSingInOptions)
        return googleSignInClient.signInIntent
    }

    suspend fun loginByGoogle(result: ActivityResult) = withContext(dispatcher) {
        callbackFlow<Task<AuthResult>> {
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener { taskAuthResult ->
                        trySend(taskAuthResult)
                    }
            } else {
                throw Exception()
            }
            awaitClose {}
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun logOut() {
        firebaseAuth.signOut()
    }

    suspend fun signUpWithEmailPassword(email: String, password: String) = withContext(dispatcher) {
        callbackFlow<Task<AuthResult>> {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    trySend(task)
                }
            awaitClose {}
        }
    }

}
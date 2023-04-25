package software.ehsan.newsfeed.data.repository

import android.content.Intent
import androidx.activity.result.ActivityResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import software.ehsan.newsfeed.data.model.User
import software.ehsan.newsfeed.data.source.remote.LoginManager
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(var loginManager: LoginManager) : UserRepository,
    BaseRepository() {

    override suspend fun login(email: String, password: String): Flow<Task<AuthResult>> {
        return loginManager.loginByEmailPassword(email, password)
    }

    override fun initializeLoginByGoogle(): Intent {
        return loginManager.initializeLoginByGoogle()
    }

    override suspend fun loginByGoogle(result: ActivityResult): Flow<Task<AuthResult>> {
        return loginManager.loginByGoogle(result)
    }

    override fun getCurrentUser(): User {
        val firebaseUser = loginManager.getCurrentUser()
        if (firebaseUser != null) {
            return User(
                email = firebaseUser.email,
                id = firebaseUser.uid,
                isAuthenticated = true,
                isEmailVerified = firebaseUser.isEmailVerified
            )
        }
        return User()
    }

    override suspend fun signUpWithEmailPassword(
        email: String,
        password: String
    ): Flow<Task<AuthResult>> {
        return loginManager.signUpWithEmailPassword(email = email, password = password)
    }


    override suspend fun sendEmailVerification(firebaseUser: FirebaseUser): Task<Void> {
        return loginManager.sendEmailVerification1(firebaseUser)
    }

    override fun logOut() {
        loginManager.logOut()
    }
}
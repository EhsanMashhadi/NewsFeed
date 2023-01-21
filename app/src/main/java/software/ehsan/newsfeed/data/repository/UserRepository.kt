package software.ehsan.newsfeed.data.repository

import android.content.Intent
import androidx.activity.result.ActivityResult
import software.ehsan.newsfeed.data.model.User
import software.ehsan.newsfeed.data.source.remote.LoginManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(var loginManager: LoginManager) : BaseRepository() {

    suspend fun login(email: String, password: String): Flow<Task<AuthResult>> {
        return loginManager.loginByEmailPassword(email, password)
    }

    fun initializeLoginByGoogle(): Intent {
        return loginManager.initializeLoginByGoogle()
    }

    suspend fun loginByGoogle(result: ActivityResult): Flow<Task<AuthResult>> {
        return loginManager.loginByGoogle(result)
    }

    fun getCurrentUser(): User {
        val firebaseUser = loginManager.getCurrentUser()
        if (firebaseUser != null) {
            return User(email = firebaseUser.email, id = firebaseUser.uid, isAuthenticated = true, isEmailVerified = firebaseUser.isEmailVerified)
        }
        return User()
    }

    suspend fun signUpWithEmailPassword(email: String, password: String): Flow<Task<AuthResult>> {
        return loginManager.signUpWithEmailPassword(email = email, password = password)
    }

    suspend fun sendEmailVerification(firebaseUser: FirebaseUser):Flow<Task<Void>>{
        return loginManager.sendEmailVerification(firebaseUser)
    }

    suspend fun sendEmailVerification1(firebaseUser: FirebaseUser): Task<Void> {
        return loginManager.sendEmailVerification1(firebaseUser)
    }

    fun logOut() {
        loginManager.logOut()
    }
}
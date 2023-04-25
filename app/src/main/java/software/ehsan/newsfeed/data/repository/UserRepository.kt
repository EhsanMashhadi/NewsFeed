package software.ehsan.newsfeed.data.repository

import android.content.Intent
import androidx.activity.result.ActivityResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import software.ehsan.newsfeed.data.model.User

interface UserRepository {
    suspend fun login(email: String, password: String): Flow<Task<AuthResult>>
    fun initializeLoginByGoogle(): Intent
    suspend fun loginByGoogle(result: ActivityResult): Flow<Task<AuthResult>>
    fun getCurrentUser(): User
    suspend fun signUpWithEmailPassword(email: String, password: String): Flow<Task<AuthResult>>
    suspend fun sendEmailVerification(firebaseUser: FirebaseUser): Task<Void>
    fun logOut()
}
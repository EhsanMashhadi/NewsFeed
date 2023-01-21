package software.ehsan.newsfeed.data.source.database.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import software.ehsan.newsfeed.data.model.Article
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseRealTimeDatabase @Inject constructor(private val firebaseDatabase: FirebaseDatabase) {

    fun insertArticle(article: Article, userId: String): Task<Void> {
        article.isSaved = true
        val myRef = firebaseDatabase.getReference("article/${userId}/${article.id}")
        return myRef.setValue(article)
    }

    fun removeArticle(article: Article, userId: String): Task<Void> {
        val myRef = firebaseDatabase.getReference("article/${userId}/${article.id}")
        return myRef.removeValue()
    }

    suspend fun getSavedArticles(userId: String): Flow<List<Article>> =
        withContext(Dispatchers.IO) {
            callbackFlow {
                val postListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val articles = dataSnapshot.getValue<Map<String, Article>>()
                        if (articles != null)
                            trySend(articles.values.toList())
                        else
                            trySend(emptyList())
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Logger.e("loadPost:onCancelled %s", databaseError.toException())
                        cancel(
                            CancellationException(
                                "Firebase Cancellation",
                                databaseError.toException()
                            )
                        )
                    }
                }
                val myRef = firebaseDatabase.getReference("article/${userId}")
                myRef.addValueEventListener(postListener)
                awaitClose {}
            }
        }


    suspend fun getSavedArticle(userId: String, articleId: String): Article? =
        suspendCancellableCoroutine { continuation ->
            val myRef = firebaseDatabase.getReference("article").child(userId).child(articleId)
            myRef.get().addOnSuccessListener {
                continuation.resume(it!!.getValue(Article::class.java))
            }.addOnFailureListener {
                Logger.e("Failed to get saved article")
                continuation.resumeWithException(it)
            }
        }
}